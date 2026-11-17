package com.example.domain

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

data class ScoreOption(
    val value: Int,
    val labelFr: String,
    val labelEn: String,
    val labelEs: String,
    val weightDescription: String = "+$value"
)

sealed class ScoreInputSpec(
    open val key: String,
    open val labelFr: String,
    open val labelEn: String,
    open val labelEs: String
) {
    data class Choice(
        override val key: String,
        override val labelFr: String,
        override val labelEn: String,
        override val labelEs: String,
        val options: List<ScoreOption>,
        val defaultValue: Int
    ) : ScoreInputSpec(key, labelFr, labelEn, labelEs)

    data class Toggle(
        override val key: String,
        override val labelFr: String,
        override val labelEn: String,
        override val labelEs: String,
        val pointsWhenTrue: Int,
        val defaultChecked: Boolean = false
    ) : ScoreInputSpec(key, labelFr, labelEn, labelEs)

    data class NumericSlider(
        override val key: String,
        override val labelFr: String,
        override val labelEn: String,
        override val labelEs: String,
        val min: Double,
        val max: Double,
        val step: Double = 1.0,
        val unit: String = "",
        val defaultValue: Double
    ) : ScoreInputSpec(key, labelFr, labelEn, labelEs)
}

data class SeverityThresholds(
    val severeBoundary: Double,
    val moderateBoundary: Double,
    val higherIsWorse: Boolean = true
)

data class ClinicalScoreDefinition(
    val scoreId: String,
    val name: String,
    val specialtyId: String,
    val specialtyNameFr: String,
    val specialtyNameEn: String,
    val specialtyNameEs: String,
    val descriptionFr: String,
    val descriptionEn: String,
    val descriptionEs: String,
    val source: String,
    val license: String,
    val versionHistory: List<String>,
    val supportsInterventionField: Boolean = true,
    val isCardiacRiskScore: Boolean = false,
    val inputs: List<ScoreInputSpec>,
    val minScore: Double,
    val maxScore: Double,
    val isCustomFromAdmin: Boolean = false,
    val unitDisplay: String = "pts",
    val severityThresholds: SeverityThresholds? = null,
    val formulaKey: String? = null
)

data class MedicalSpecialty(
    val id: String,
    val nameFr: String,
    val nameEn: String,
    val nameEs: String,
    val calculatorCountMcp: Int,
    val iconKey: String,
    val featuredScores: List<ClinicalScoreDefinition>
)

data class CalculatedScoreResult(
    val scoreId: String,
    val scoreName: String,
    val specialty: String,
    val numericValue: Double,
    val displayValue: String,
    val severityLabel: String,
    val severityLevel: SeverityLevel,
    val cardiacRiskPercent: Double? = null,
    val cardiacGuidance: String? = null,
    val breakdownLines: List<String>,
    val serializedInputs: Map<String, Any>
)

enum class SeverityLevel {
    LOW, MODERATE, SEVERE
}

@Keep
@IgnoreExtraProperties
data class UserEvaluationRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val scoreId: String = "",
    val scoreName: String = "",
    val specialty: String = "",
    val clinicalScoreValue: Double = 0.0,
    val clinicalScoreDisplay: String = "",
    val severityLabel: String = "",
    val cardiacRiskPercent: Double? = null,
    val diagnosis: String = "",
    val interventionType: String? = null,
    val breakdownSummary: String = "",
    val geminiRecommendation: String? = null,
    val encryptedClinicalNotesCiphertext: String? = null,
    val encryptedClinicalNotesIv: String? = null,
    val language: String = "fr",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateMap(): Map<String, Any> {
        val raw = mutableMapOf<String, Any?>(
            "userId" to userId,
            "scoreId" to scoreId,
            "scoreName" to scoreName,
            "specialty" to specialty,
            "clinicalScoreValue" to clinicalScoreValue,
            "clinicalScoreDisplay" to clinicalScoreDisplay,
            "severityLabel" to severityLabel,
            "cardiacRiskPercent" to cardiacRiskPercent,
            "diagnosis" to diagnosis,
            "interventionType" to interventionType?.takeIf { it.isNotBlank() },
            "breakdownSummary" to breakdownSummary,
            "geminiRecommendation" to geminiRecommendation?.takeIf { it.isNotBlank() },
            "encryptedClinicalNotesCiphertext" to encryptedClinicalNotesCiphertext?.takeIf { it.isNotBlank() },
            "encryptedClinicalNotesIv" to encryptedClinicalNotesIv?.takeIf { it.isNotBlank() },
            "language" to language,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        @Suppress("UNCHECKED_CAST")
        return raw.filterValues { it != null } as Map<String, Any>
    }
}

@Keep
@IgnoreExtraProperties
data class MedGemmaScoreRecord(
    @DocumentId val id: String = "",
    val evaluationId: String = "",
    val evaluatorUid: String = "",
    val scoreId: String = "",
    val improvementScore: Double = 0.0,
    val confidence: Double = 0.0,
    val clinicalInsights: String = "",
    val modelVersion: String = "medgemma-1.5-4b-4bit",
    @ServerTimestamp val computedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class AiModelConfigRecord(
    @DocumentId val id: String = "",
    val totalEvaluations: Long = 0L,
    val modelMaturity: String = "BOOTSTRAP",
    val awellScoreVersion: String = "1.3.0",
    val medicalCalcMcpVersion: String = "2.1.0",
    val hfSpaceUrl: String? = null,
    val latestSocietyNotice: String? = null,
    val specialtyCounts: Map<String, Long> = emptyMap(),
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class CustomCatalogScore(
    @DocumentId val id: String = "",
    val scoreId: String = "",
    val name: String = "",
    val specialty: String = "",
    val description: String = "",
    val license: String = "MIT",
    val source: String = "Medical Calculator MCP / Admin",
    val version: String = "1.0.0",
    val active: Boolean = true,
    val minScore: Double = 0.0,
    val maxScore: Double = 10.0,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toCreateOrUpdateMap(): Map<String, Any> = mapOf(
        "scoreId" to scoreId,
        "name" to name,
        "specialty" to specialty,
        "description" to description,
        "license" to license,
        "source" to source,
        "version" to version,
        "active" to active,
        "minScore" to minScore,
        "maxScore" to maxScore,
        "updatedAt" to FieldValue.serverTimestamp()
    )
}
