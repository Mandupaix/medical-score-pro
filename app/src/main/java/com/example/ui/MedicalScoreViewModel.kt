package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GeminiRecommendationRepository
import com.example.data.GeminiUiState
import com.example.data.MedicalScoreRepository
import com.example.domain.AiModelConfigRecord
import com.example.domain.CalculatedScoreResult
import com.example.domain.ClinicalScoreDefinition
import com.example.domain.ClinicalScoreEngine
import com.example.domain.CustomCatalogScore
import com.example.domain.MedGemmaScoreRecord
import com.example.domain.MedicalCalculatorCatalog
import com.example.domain.UserEvaluationRecord
import com.example.i18n.AppLanguage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

enum class DateRangeFilter {
    ALL, TODAY, SEVEN_DAYS, THIRTY_DAYS
}

data class ActiveResultContext(
    val evaluationId: String,
    val definition: ClinicalScoreDefinition,
    val calculated: CalculatedScoreResult,
    val diagnosis: String,
    val interventionType: String?
)

@OptIn(ExperimentalCoroutinesApi::class)
class MedicalScoreViewModel(
    private val repository: MedicalScoreRepository,
    private val geminiRepo: GeminiRecommendationRepository,
    private val currentUserId: String
) : ViewModel() {

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    val aiModelConfig: StateFlow<AiModelConfigRecord?> = repository.observeAiModelConfig()
        .catch { error ->
            if (error is CancellationException &&
                generateSequence<Throwable>(error) { it.cause }.none { it is FirebaseFirestoreException }
            ) throw error
            Log.w(TAG, "Failed to observe ai_model_config", error)
            emit(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val customCatalog: StateFlow<List<CustomCatalogScore>> = repository.observeActiveCustomCatalog()
        .catch { error ->
            if (error is CancellationException &&
                generateSequence<Throwable>(error) { it.cause }.none { it is FirebaseFirestoreException }
            ) throw error
            Log.w(TAG, "Failed to observe score_catalog", error)
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val userEvaluationsState: StateFlow<UiState<List<UserEvaluationRecord>>> =
        repository.observeUserEvaluations(currentUserId)
            .map<List<UserEvaluationRecord>, UiState<List<UserEvaluationRecord>>> { UiState.Success(it) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val root = firestoreError ?: error
                Log.w(TAG, "Error observing user evaluations", root)
                emit(UiState.Error(root.message ?: "Failed to load evaluation history"))
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    val medGemmaScoresMap: StateFlow<Map<String, MedGemmaScoreRecord>> =
        repository.observeUserMedGemmaScores(currentUserId)
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                Log.w(TAG, "Error observing MedGemma map", error)
                emit(emptyMap())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyMap())

    // History Filters
    private val _diagnosisFilter = MutableStateFlow("")
    val diagnosisFilter: StateFlow<String> = _diagnosisFilter.asStateFlow()

    private val _dateFilter = MutableStateFlow(DateRangeFilter.ALL)
    val dateFilter: StateFlow<DateRangeFilter> = _dateFilter.asStateFlow()

    val filteredHistory: StateFlow<UiState<List<UserEvaluationRecord>>> = combine(
        userEvaluationsState,
        _diagnosisFilter,
        _dateFilter
    ) { state, query, range ->
        when (state) {
            is UiState.Loading -> UiState.Loading
            is UiState.Error -> state
            is UiState.Success -> {
                val nowSeconds = Timestamp.now().seconds
                val cutoffSeconds = when (range) {
                    DateRangeFilter.ALL -> 0L
                    DateRangeFilter.TODAY -> nowSeconds - 86_400L
                    DateRangeFilter.SEVEN_DAYS -> nowSeconds - 7 * 86_400L
                    DateRangeFilter.THIRTY_DAYS -> nowSeconds - 30 * 86_400L
                }
                val q = query.trim().lowercase()
                val filtered = state.data.filter { item ->
                    val matchesDiag = q.isEmpty() ||
                        item.diagnosis.lowercase().contains(q) ||
                        item.scoreName.lowercase().contains(q) ||
                        item.specialty.lowercase().contains(q)
                    val itemSec = item.createdAt?.seconds ?: nowSeconds
                    val matchesDate = range == DateRangeFilter.ALL || itemSec >= cutoffSeconds
                    matchesDiag && matchesDate
                }.sortedByDescending { it.createdAt?.seconds ?: Long.MAX_VALUE }
                UiState.Success(filtered)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)

    // Active Result & Live MedGemma / Gemini States
    private val _activeResult = MutableStateFlow<ActiveResultContext?>(null)
    val activeResult: StateFlow<ActiveResultContext?> = _activeResult.asStateFlow()

    val activeMedGemmaScore: StateFlow<MedGemmaScoreRecord?> = _activeResult
        .flatMapLatest { ctx ->
            if (ctx == null) flowOf(null)
            else repository.observeMedGemmaScore(ctx.evaluationId)
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    Log.w(TAG, "Error observing active MedGemma score", error)
                    emit(null)
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    private val _geminiState = MutableStateFlow<GeminiUiState>(GeminiUiState.Idle)
    val geminiState: StateFlow<GeminiUiState> = _geminiState.asStateFlow()

    private val _submissionLoading = MutableStateFlow(false)
    val submissionLoading: StateFlow<Boolean> = _submissionLoading.asStateFlow()

    private val _submissionError = MutableStateFlow<String?>(null)
    val submissionError: StateFlow<String?> = _submissionError.asStateFlow()

    private val _adminStatusMessage = MutableStateFlow<String?>(null)
    val adminStatusMessage: StateFlow<String?> = _adminStatusMessage.asStateFlow()

    private var geminiJob: Job? = null

    init {
        viewModelScope.launch {
            _isAdmin.value = repository.checkIsAdmin()
        }
    }

    fun updateDiagnosisFilter(query: String) {
        _diagnosisFilter.value = query
    }

    fun updateDateFilter(filter: DateRangeFilter) {
        _dateFilter.value = filter
    }

    fun clearSubmissionError() {
        _submissionError.value = null
    }

    fun clearAdminStatus() {
        _adminStatusMessage.value = null
    }

    fun findScoreDefinition(scoreId: String): ClinicalScoreDefinition? {
        return MedicalCalculatorCatalog.findScoreById(scoreId, customCatalog.value)
    }

    fun submitScoreEvaluation(
        definition: ClinicalScoreDefinition,
        numericInputs: Map<String, Double>,
        booleanInputs: Map<String, Boolean>,
        diagnosis: String,
        interventionType: String?,
        privateClinicalNotes: String?,
        language: AppLanguage,
        onNavigateToResult: () -> Unit
    ) {
        viewModelScope.launch {
            _submissionLoading.value = true
            _submissionError.value = null
            try {
                val calculated = ClinicalScoreEngine.calculate(
                    definition = definition,
                    numericInputs = numericInputs,
                    booleanInputs = booleanInputs,
                    language = language
                )
                val saveResult = repository.submitEvaluation(
                    result = calculated,
                    diagnosis = diagnosis,
                    interventionType = interventionType,
                    privateClinicalNotes = privateClinicalNotes,
                    language = language,
                    hfSpaceUrl = aiModelConfig.value?.hfSpaceUrl
                )
                saveResult.fold(
                    onSuccess = { savedRecord ->
                        _activeResult.value = ActiveResultContext(
                            evaluationId = savedRecord.id,
                            definition = definition,
                            calculated = calculated,
                            diagnosis = diagnosis.trim(),
                            interventionType = interventionType?.trim()?.takeIf { it.isNotEmpty() }
                        )
                        _submissionLoading.value = false
                        onNavigateToResult()
                        triggerGeminiRecommendation(
                            evaluationId = savedRecord.id,
                            calculated = calculated,
                            diagnosis = diagnosis.trim(),
                            interventionType = interventionType,
                            language = language
                        )
                    },
                    onFailure = { error ->
                        _submissionLoading.value = false
                        _submissionError.value = error.localizedMessage ?: "Failed to save evaluation"
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _submissionLoading.value = false
                _submissionError.value = e.localizedMessage ?: "Calculation failed"
            }
        }
    }

    fun retryGeminiRecommendation(language: AppLanguage) {
        val ctx = _activeResult.value ?: return
        triggerGeminiRecommendation(
            evaluationId = ctx.evaluationId,
            calculated = ctx.calculated,
            diagnosis = ctx.diagnosis,
            interventionType = ctx.interventionType,
            language = language
        )
    }

    private fun triggerGeminiRecommendation(
        evaluationId: String,
        calculated: CalculatedScoreResult,
        diagnosis: String,
        interventionType: String?,
        language: AppLanguage
    ) {
        geminiJob?.cancel()
        geminiJob = viewModelScope.launch {
            geminiRepo.streamClinicalRecommendation(
                result = calculated,
                diagnosis = diagnosis,
                interventionType = interventionType,
                language = language
            ).collect { state ->
                _geminiState.value = state
                if (state is GeminiUiState.Success) {
                    repository.updateGeminiRecommendation(evaluationId, state.text)
                }
            }
        }
    }

    fun saveCustomScore(score: CustomCatalogScore) {
        viewModelScope.launch {
            val result = repository.upsertCustomScore(score)
            _adminStatusMessage.value = result.fold(
                onSuccess = { "Score '${score.name}' enregistré dans Firestore." },
                onFailure = { it.localizedMessage ?: "Erreur d'enregistrement" }
            )
        }
    }

    fun deleteCustomScore(scoreId: String) {
        viewModelScope.launch {
            val result = repository.deleteCustomScore(scoreId)
            _adminStatusMessage.value = result.fold(
                onSuccess = { "Score '$scoreId' supprimé." },
                onFailure = { it.localizedMessage ?: "Erreur de suppression" }
            )
        }
    }

    fun saveGlobalModelConfig(
        totalEvaluations: Long,
        maturity: String,
        awellVersion: String,
        mcpVersion: String,
        hfSpaceUrl: String,
        notice: String
    ) {
        viewModelScope.launch {
            val result = repository.updateAiModelConfig(
                totalEvaluations = totalEvaluations,
                maturity = maturity,
                awellVersion = awellVersion,
                mcpVersion = mcpVersion,
                hfSpaceUrl = hfSpaceUrl,
                notice = notice
            )
            _adminStatusMessage.value = result.fold(
                onSuccess = { "Configuration ai_model_config/current mise à jour." },
                onFailure = { it.localizedMessage ?: "Erreur de mise à jour" }
            )
        }
    }

    private companion object {
        const val TAG = "MedicalScoreVM"
    }
}
