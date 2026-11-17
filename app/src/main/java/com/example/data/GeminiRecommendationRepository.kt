package com.example.data

import android.util.Log
import com.example.domain.CalculatedScoreResult
import com.example.i18n.AppLanguage
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

sealed interface GeminiUiState {
    data object Idle : GeminiUiState
    data object Loading : GeminiUiState
    data class Streaming(val text: String) : GeminiUiState
    data class Success(val text: String) : GeminiUiState
    data class Error(val message: String) : GeminiUiState
}

/**
 * Generates instant clinical guidance using Gemini 3.8 Flash via Firebase AI Logic
 * protected by Firebase App Check (no client-side API key).
 */
class GeminiRecommendationRepository {

    private val model by lazy {
        Firebase.ai.generativeModel("gemini-3.8-flash")
    }

    fun streamClinicalRecommendation(
        result: CalculatedScoreResult,
        diagnosis: String,
        interventionType: String?,
        language: AppLanguage
    ): Flow<GeminiUiState> = flow {
        emit(GeminiUiState.Loading)
        val prompt = buildClinicalPrompt(result, diagnosis, interventionType, language)
        val accumulated = StringBuilder()

        try {
            withTimeout(45_000L) {
                model.generateContentStream(prompt).collect { chunk ->
                    val piece = chunk.text.orEmpty()
                    if (piece.isNotEmpty()) {
                        accumulated.append(piece)
                        emit(GeminiUiState.Streaming(accumulated.toString()))
                    }
                }
            }
            val finalText = accumulated.toString().trim()
            if (finalText.isNotBlank()) {
                emit(GeminiUiState.Success(finalText))
            } else {
                emit(GeminiUiState.Error("Gemini returned an empty recommendation."))
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Gemini recommendation timed out", e)
            emit(GeminiUiState.Error("Timeout (45s) lors de la génération des recommandations."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Firebase AI Logic generation failed", e)
            emit(GeminiUiState.Error(e.localizedMessage ?: "Erreur Firebase AI Logic / App Check."))
        }
    }

    private fun buildClinicalPrompt(
        result: CalculatedScoreResult,
        diagnosis: String,
        interventionType: String?,
        language: AppLanguage
    ): String {
        val langInstruction = when (language) {
            AppLanguage.FR -> "Réponds exclusivement en français médical professionnel, concis et structuré (3 à 4 puces actionnables)."
            AppLanguage.EN -> "Respond exclusively in concise, structured professional medical English (3–4 actionable bullet points)."
            AppLanguage.ES -> "Responde exclusivamente en español médico profesional, conciso y estructurado (3–4 puntos accionables)."
        }

        return """
            You are a clinical decision-support assistant for qualified healthcare professionals.
            $langInstruction
            Do not ask for patient identifying details. Do not recalculate the deterministic score.
            
            Score: ${result.scoreName} (${result.specialty})
            Deterministic Result: ${result.displayValue} — ${result.severityLabel}
            Calculation Breakdown:
            ${result.breakdownLines.joinToString("\n")}
            Anonymized Diagnosis: $diagnosis
            ${if (!interventionType.isNullOrBlank()) "Planned/Current Intervention: $interventionType" else ""}
            ${if (result.cardiacRiskPercent != null) "Calculated Cardiac/Stroke Risk: ${result.cardiacRiskPercent}%" else ""}
            
            Provide:
            1. Immediate clinical priorities & monitoring frequency.
            2. Red-flag escalation criteria.
            3. Relevant guideline alignment (ESC / SFAR / NICE / Brain Trauma Foundation as appropriate).
        """.trimIndent()
    }

    private companion object {
        const val TAG = "GeminiRecRepo"
    }
}
