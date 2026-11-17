package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.GeminiRecommendationRepository
import com.example.data.MedicalScoreRepository
import com.example.data.PreferencesRepository
import com.example.data.UserPreferences
import com.example.i18n.AppLanguage
import com.example.security.configureAppCheck
import com.example.ui.AdminScreen
import com.example.ui.AuthScreen
import com.example.ui.ConsentOnboardingScreen
import com.example.ui.HistoryScreen
import com.example.ui.HomeSpecialtiesScreen
import com.example.ui.MedicalScoreViewModel
import com.example.ui.ScoreFormScreen
import com.example.ui.ScoreResultScreen
import com.example.ui.signOutClinician
import com.example.ui.theme.MedicalScoreProTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

private const val ROUTE_HOME = "home"
private const val ROUTE_FORM = "score_form/{scoreId}"
private const val ROUTE_RESULT = "score_result"
private const val ROUTE_HISTORY = "history"
private const val ROUTE_ADMIN = "admin"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory Firebase App Check wiring with launch intent BEFORE UI/ViewModels
        configureAppCheck(this, intent)

        // STEP 2: Mandatory Edge-to-Edge layout
        enableEdgeToEdge()

        setContent {
            MedicalScoreProTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

private fun FirebaseAuth.authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { auth ->
        trySend(auth.currentUser)
    }
    addAuthStateListener(listener)
    awaitClose { removeAuthStateListener(listener) }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefsRepo = remember(context) { PreferencesRepository(context.applicationContext) }
    val userPrefs by prefsRepo.preferencesFlow.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )
    val auth = remember { Firebase.auth }
    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(
        initialValue = auth.currentUser
    )

    if (!userPrefs.consentAccepted) {
        ConsentOnboardingScreen(
            language = userPrefs.language,
            onLanguageChange = { lang -> scope.launch { prefsRepo.setLanguage(lang) } },
            onAcceptConsent = { scope.launch { prefsRepo.setConsentAccepted(true) } }
        )
    } else if (currentUser == null) {
        AuthScreen(
            language = userPrefs.language,
            onLanguageChange = { lang -> scope.launch { prefsRepo.setLanguage(lang) } },
            onAuthSuccess = { /* AuthStateListener updates currentUser */ }
        )
    } else {
        val uid = currentUser!!.uid
        key(uid) {
            AuthenticatedMedicalApp(
                currentUserId = uid,
                language = userPrefs.language,
                onLanguageChange = { lang -> scope.launch { prefsRepo.setLanguage(lang) } }
            )
        }
    }
}

@Composable
private fun AuthenticatedMedicalApp(
    currentUserId: String,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val viewModel: MedicalScoreViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY])
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                MedicalScoreViewModel(
                    repository = MedicalScoreRepository(db),
                    geminiRepo = GeminiRecommendationRepository(),
                    currentUserId = currentUserId
                )
            }
        }
    )

    val navController = rememberNavController()
    val isAdmin by viewModel.isAdmin.collectAsStateWithLifecycle()
    val modelConfig by viewModel.aiModelConfig.collectAsStateWithLifecycle()
    val customCatalog by viewModel.customCatalog.collectAsStateWithLifecycle()
    val activeResult by viewModel.activeResult.collectAsStateWithLifecycle()
    val activeMedGemma by viewModel.activeMedGemmaScore.collectAsStateWithLifecycle()
    val geminiState by viewModel.geminiState.collectAsStateWithLifecycle()
    val submissionLoading by viewModel.submissionLoading.collectAsStateWithLifecycle()
    val submissionError by viewModel.submissionError.collectAsStateWithLifecycle()
    val filteredHistory by viewModel.filteredHistory.collectAsStateWithLifecycle()
    val medGemmaMap by viewModel.medGemmaScoresMap.collectAsStateWithLifecycle()
    val diagnosisFilter by viewModel.diagnosisFilter.collectAsStateWithLifecycle()
    val dateFilter by viewModel.dateFilter.collectAsStateWithLifecycle()
    val adminStatus by viewModel.adminStatusMessage.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        composable(ROUTE_HOME) {
            HomeSpecialtiesScreen(
                language = language,
                onLanguageChange = onLanguageChange,
                isAdmin = isAdmin,
                modelConfig = modelConfig,
                customCatalog = customCatalog,
                onSelectScore = { scoreId ->
                    viewModel.clearSubmissionError()
                    navController.navigate("score_form/$scoreId")
                },
                onOpenHistory = { navController.navigate(ROUTE_HISTORY) },
                onOpenAdmin = {
                    viewModel.clearAdminStatus()
                    navController.navigate(ROUTE_ADMIN)
                },
                onSignOut = {
                    signOutClinician(credentialManager, scope)
                }
            )
        }

        composable(
            route = ROUTE_FORM,
            arguments = listOf(navArgument("scoreId") { type = NavType.StringType })
        ) { backStackEntry ->
            val scoreId = backStackEntry.arguments?.getString("scoreId") ?: "glasgow"
            val definition = viewModel.findScoreDefinition(scoreId)
                ?: com.example.domain.MedicalCalculatorCatalog.glasgowScore

            ScoreFormScreen(
                definition = definition,
                language = language,
                modelConfig = modelConfig,
                isSubmitting = submissionLoading,
                submissionError = submissionError,
                onBack = { navController.popBackStack() },
                onSubmit = { numInputs, boolInputs, diagnosis, intervention, notes ->
                    viewModel.submitScoreEvaluation(
                        definition = definition,
                        numericInputs = numInputs,
                        booleanInputs = boolInputs,
                        diagnosis = diagnosis,
                        interventionType = intervention,
                        privateClinicalNotes = notes,
                        language = language,
                        onNavigateToResult = {
                            navController.navigate(ROUTE_RESULT)
                        }
                    )
                }
            )
        }

        composable(ROUTE_RESULT) {
            val ctx = activeResult
            if (ctx != null) {
                ScoreResultScreen(
                    context = ctx,
                    language = language,
                    medGemmaScore = activeMedGemma,
                    geminiState = geminiState,
                    modelConfig = modelConfig,
                    onRetryGemini = { viewModel.retryGeminiRecommendation(language) },
                    onOpenHistory = { navController.navigate(ROUTE_HISTORY) },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(ROUTE_HISTORY) {
            HistoryScreen(
                language = language,
                historyState = filteredHistory,
                medGemmaMap = medGemmaMap,
                diagnosisFilter = diagnosisFilter,
                dateFilter = dateFilter,
                onDiagnosisFilterChange = viewModel::updateDiagnosisFilter,
                onDateFilterChange = viewModel::updateDateFilter,
                onBack = { navController.popBackStack() }
            )
        }

        composable(ROUTE_ADMIN) {
            AdminScreen(
                language = language,
                modelConfig = modelConfig,
                customCatalog = customCatalog,
                statusMessage = adminStatus,
                onSaveScore = viewModel::saveCustomScore,
                onDeleteScore = viewModel::deleteCustomScore,
                onSaveConfig = viewModel::saveGlobalModelConfig,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
