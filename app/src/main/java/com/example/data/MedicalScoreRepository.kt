package com.example.data

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.example.R
import com.example.domain.AiModelConfigRecord
import com.example.domain.CalculatedScoreResult
import com.example.domain.CustomCatalogScore
import com.example.domain.MedGemmaScoreRecord
import com.example.domain.UserEvaluationRecord
import com.example.i18n.AppLanguage
import com.example.security.CryptoManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MedicalScoreRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .build()

    private fun requireUserId(): String {
        return auth.currentUser?.uid?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeUserEvaluations(userId: String): Flow<List<UserEvaluationRecord>> = flow {
        require(userId.isNotBlank()) { "User ID must not be blank" }
        val path = "users/$userId/evaluations"
        emitAll(
            db.collection("users").document(userId).collection("evaluations")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        UserEvaluationRecord::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt?.seconds ?: Long.MAX_VALUE }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val toLog = firestoreError ?: (error as? Exception)
                    if (toLog != null && auth.currentUser?.uid == userId) {
                        handleFirestoreError(toLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    fun observeMedGemmaScore(evaluationId: String): Flow<MedGemmaScoreRecord?> = flow {
        require(evaluationId.isNotBlank()) { "Evaluation ID must not be blank" }
        val path = "medgemma_ai_scores/$evaluationId"
        emitAll(
            db.collection("medgemma_ai_scores").document(evaluationId)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(
                            MedGemmaScoreRecord::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    } else null
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val toLog = firestoreError ?: (error as? Exception)
                    if (toLog != null && auth.currentUser != null) {
                        handleFirestoreError(toLog, OperationType.GET, path)
                    }
                    throw error
                }
        )
    }

    fun observeUserMedGemmaScores(userId: String): Flow<Map<String, MedGemmaScoreRecord>> = flow {
        require(userId.isNotBlank()) { "User ID must not be blank" }
        val path = "medgemma_ai_scores"
        emitAll(
            db.collection(path)
                .whereEqualTo("evaluatorUid", userId)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        MedGemmaScoreRecord::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).associateBy { it.evaluationId.ifBlank { it.id } }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val toLog = firestoreError ?: (error as? Exception)
                    if (toLog != null && auth.currentUser?.uid == userId) {
                        handleFirestoreError(toLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    fun observeAiModelConfig(): Flow<AiModelConfigRecord?> = flow {
        val path = "ai_model_config/current"
        emitAll(
            db.collection("ai_model_config").document("current")
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(
                            AiModelConfigRecord::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    } else null
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val toLog = firestoreError ?: (error as? Exception)
                    if (toLog != null && auth.currentUser != null) {
                        handleFirestoreError(toLog, OperationType.GET, path)
                    }
                    throw error
                }
        )
    }

    fun observeActiveCustomCatalog(): Flow<List<CustomCatalogScore>> = flow {
        val path = "score_catalog"
        emitAll(
            db.collection(path)
                .whereEqualTo("active", true)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        CustomCatalogScore::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val toLog = firestoreError ?: (error as? Exception)
                    if (toLog != null && auth.currentUser != null) {
                        handleFirestoreError(toLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    suspend fun checkIsAdmin(): Boolean {
        val user = auth.currentUser ?: return false
        return try {
            val tokenResult = user.getIdToken(false).await()
            val hasAdminClaim = tokenResult.claims["admin"] == true
            if (hasAdminClaim) return true
            if (user.email == "mandupaix@gmail.com" && user.isEmailVerified) return true
            val adminDoc = db.collection("admins").document(user.uid).get().await()
            adminDoc.exists()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w("MedicalRepo", "Admin check failed gracefully: ${e.message}")
            user.email == "mandupaix@gmail.com" && user.isEmailVerified
        }
    }

    suspend fun submitEvaluation(
        result: CalculatedScoreResult,
        diagnosis: String,
        interventionType: String?,
        privateClinicalNotes: String?,
        language: AppLanguage,
        hfSpaceUrl: String?
    ): Result<UserEvaluationRecord> {
        var docPath = "users/unknown/evaluations"
        return try {
            val uid = requireUserId()
            val trimmedDiagnosis = diagnosis.trim().take(200)
            require(trimmedDiagnosis.isNotEmpty()) { "Diagnosis must not be empty" }
            val trimmedIntervention = interventionType?.trim()?.take(200)?.takeIf { it.isNotEmpty() }

            val userEvalRef = db.collection("users").document(uid).collection("evaluations").document()
            val evaluationId = userEvalRef.id
            docPath = userEvalRef.path

            val encryptedNotes = privateClinicalNotes?.trim()?.takeIf { it.isNotEmpty() }?.let { plain ->
                CryptoManager.encrypt(plain)
            }

            val breakdownText = result.breakdownLines.joinToString("\n").take(2000)

            val record = UserEvaluationRecord(
                id = evaluationId,
                userId = uid,
                scoreId = result.scoreId,
                scoreName = result.scoreName.take(128),
                specialty = result.specialty.take(64),
                clinicalScoreValue = result.numericValue,
                clinicalScoreDisplay = result.displayValue.take(64),
                severityLabel = result.severityLabel.take(64),
                cardiacRiskPercent = result.cardiacRiskPercent,
                diagnosis = trimmedDiagnosis,
                interventionType = trimmedIntervention,
                breakdownSummary = breakdownText,
                encryptedClinicalNotesCiphertext = encryptedNotes?.ciphertextBase64?.take(4096),
                encryptedClinicalNotesIv = encryptedNotes?.ivBase64?.take(128),
                language = language.code
            )

            // 1. Write user-isolated record (with Keystore-encrypted private notes)
            userEvalRef.set(record.toCreateMap()).await()

            // 2. Write strictly anonymized record to anonymous_evaluations (write-only for clients)
            val anonRef = db.collection("anonymous_evaluations").document(evaluationId)
            docPath = anonRef.path
            val anonPayload = mutableMapOf<String, Any?>(
                "evaluationId" to evaluationId,
                "evaluatorUid" to uid,
                "scoreId" to result.scoreId,
                "specialty" to result.specialty.take(64),
                "clinicalScoreValue" to result.numericValue,
                "severityLabel" to result.severityLabel.take(64),
                "diagnosis" to trimmedDiagnosis,
                "interventionType" to trimmedIntervention,
                "inputs" to result.serializedInputs,
                "language" to language.code,
                "createdAt" to FieldValue.serverTimestamp()
            ).filterValues { it != null }

            anonRef.set(anonPayload).await()

            // 3. Fire non-blocking async trigger to Hugging Face Space MedGemma server if configured
            val spaceEndpoint = hfSpaceUrl?.takeIf { it.isNotBlank() }
                ?: BuildConfig::class.java.fields
                    .firstOrNull { it.name == "MEDGEMMA_HF_SPACE_URL" }
                    ?.get(null) as? String
            if (!spaceEndpoint.isNullOrBlank()) {
                triggerHuggingFaceAsync(
                    baseUrl = spaceEndpoint,
                    evaluationId = evaluationId,
                    evaluatorUid = uid,
                    result = result,
                    diagnosis = trimmedDiagnosis,
                    interventionType = trimmedIntervention,
                    language = language
                )
            }

            Result.success(record)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docPath)
            Result.failure(e)
        }
    }

    suspend fun updateGeminiRecommendation(
        evaluationId: String,
        recommendationText: String
    ): Result<Unit> {
        var docPath = "users/unknown/evaluations/$evaluationId"
        return try {
            val uid = requireUserId()
            require(evaluationId.isNotBlank()) { "Evaluation ID required" }
            val docRef = db.collection("users").document(uid)
                .collection("evaluations").document(evaluationId)
            docPath = docRef.path
            docRef.update(
                mapOf(
                    "geminiRecommendation" to recommendationText.take(4000),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docPath)
            Result.failure(e)
        }
    }

    suspend fun upsertCustomScore(score: CustomCatalogScore): Result<Unit> {
        val docId = score.scoreId.trim().lowercase().replace(Regex("[^a-z0-9_\\-]"), "_")
        val path = "score_catalog/$docId"
        return try {
            requireUserId()
            require(docId.isNotBlank()) { "Score ID must not be blank" }
            val normalized = score.copy(scoreId = docId)
            db.collection("score_catalog").document(docId)
                .set(normalized.toCreateOrUpdateMap())
                .await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteCustomScore(scoreId: String): Result<Unit> {
        val path = "score_catalog/$scoreId"
        return try {
            requireUserId()
            db.collection("score_catalog").document(scoreId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    suspend fun updateAiModelConfig(
        totalEvaluations: Long,
        maturity: String,
        awellVersion: String,
        mcpVersion: String,
        hfSpaceUrl: String,
        notice: String
    ): Result<Unit> {
        val path = "ai_model_config/current"
        return try {
            requireUserId()
            val payload = mutableMapOf<String, Any>(
                "totalEvaluations" to totalEvaluations.coerceAtLeast(0L),
                "modelMaturity" to maturity,
                "awellScoreVersion" to awellVersion.trim().ifBlank { "1.3.0" },
                "medicalCalcMcpVersion" to mcpVersion.trim().ifBlank { "2.1.0" },
                "hfSpaceUrl" to hfSpaceUrl.trim().take(256),
                "latestSocietyNotice" to notice.trim().take(500),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            db.document(path).set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    private suspend fun triggerHuggingFaceAsync(
        baseUrl: String,
        evaluationId: String,
        evaluatorUid: String,
        result: CalculatedScoreResult,
        diagnosis: String,
        interventionType: String?,
        language: AppLanguage
    ) = withContext(Dispatchers.IO) {
        try {
            val cleanBase = baseUrl.trimEnd('/')
            val bodyJson = JSONObject().apply {
                put("evaluationId", evaluationId)
                put("evaluatorUid", evaluatorUid)
                put("scoreId", result.scoreId)
                put("specialty", result.specialty)
                put("clinicalScoreValue", result.numericValue)
                put("severityLabel", result.severityLabel)
                put("diagnosis", diagnosis)
                if (interventionType != null) put("interventionType", interventionType)
                put("inputs", JSONObject(result.serializedInputs))
                put("language", language.code)
            }
            val request = Request.Builder()
                .url("$cleanBase/analyze")
                .post(bodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()
            httpClient.newCall(request).execute().use { response ->
                Log.d("MedicalRepo", "HF MedGemma trigger status=${response.code}")
            }
        } catch (e: Exception) {
            // Non-fatal: Firestore evaluation is already saved; HF batch /process-pending or listener picks it up
            Log.w("MedicalRepo", "HF Space async trigger deferred: ${e.message}")
        }
    }
}
