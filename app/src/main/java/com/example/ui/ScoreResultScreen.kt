package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.GeminiUiState
import com.example.domain.AiModelConfigRecord
import com.example.domain.MedGemmaScoreRecord
import com.example.domain.SeverityLevel
import com.example.i18n.AppLanguage
import com.example.i18n.AppStrings
import com.example.ui.theme.MedGemmaContainer
import com.example.ui.theme.MedGemmaIndigo
import com.example.ui.theme.RiskLowContainer
import com.example.ui.theme.RiskLowEmerald
import com.example.ui.theme.RiskModerateAmber
import com.example.ui.theme.RiskModerateContainer
import com.example.ui.theme.RiskSevereContainer
import com.example.ui.theme.RiskSevereCrimson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreResultScreen(
    context: ActiveResultContext,
    language: AppLanguage,
    medGemmaScore: MedGemmaScoreRecord?,
    geminiState: GeminiUiState,
    modelConfig: AiModelConfigRecord?,
    onRetryGemini: () -> Unit,
    onOpenHistory: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val strings = AppStrings.get(language)
    var showLicenseSheet by remember { mutableStateOf(false) }

    val (badgeBg, badgeFg) = when (context.calculated.severityLevel) {
        SeverityLevel.LOW -> RiskLowContainer to RiskLowEmerald
        SeverityLevel.MODERATE -> RiskModerateContainer to RiskModerateAmber
        SeverityLevel.SEVERE -> RiskSevereContainer to RiskSevereCrimson
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.calculated.scoreName) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("result_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = onOpenHistory,
                        modifier = Modifier.testTag("result_history_button")
                    ) {
                        Icon(Icons.Filled.History, contentDescription = strings.historyButton)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 760.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Deterministic Clinical Score Hero Card
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = strings.clinicalScoreTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = context.calculated.displayValue,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = badgeBg,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = context.calculated.severityLabel,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = badgeFg,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Text(
                            text = "${strings.diagnosisLabel.removeSuffix("*").trim()}: ${context.diagnosis}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (!context.interventionType.isNullOrBlank()) {
                            Text(
                                text = "${strings.interventionLabel}: ${context.interventionType}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        HorizontalDivider()

                        Text(
                            text = strings.breakdownTitle,
                            style = MaterialTheme.typography.labelLarge
                        )
                        context.calculated.breakdownLines.forEach { line ->
                            Text(
                                text = "• $line",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 2. Cardiac Risk Card (when applicable, e.g. EuroSCORE II / CHA2DS2-VASc)
                if (context.calculated.cardiacRiskPercent != null) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = null,
                                    tint = RiskSevereCrimson
                                )
                                Text(
                                    text = strings.cardiacRiskTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${"%.2f".format(context.calculated.cardiacRiskPercent)}%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (!context.calculated.cardiacGuidance.isNullOrBlank()) {
                                Text(
                                    text = context.calculated.cardiacGuidance,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                // 3. Asynchronous MedGemma AI Improvement Score (Live Firestore onSnapshot)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MedGemmaContainer.copy(alpha = 0.45f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Biotech,
                                contentDescription = null,
                                tint = MedGemmaIndigo
                            )
                            Text(
                                text = strings.medGemmaTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MedGemmaIndigo
                            )
                        }

                        if (medGemmaScore == null) {
                            Text(
                                text = strings.medGemmaPending,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.testTag("medgemma_pending_text")
                            )
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = MedGemmaIndigo
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${medGemmaScore.improvementScore.toInt()} / 100",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MedGemmaIndigo
                                )
                                Text(
                                    text = "${strings.medGemmaConfidence}: ${(medGemmaScore.confidence * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Text(
                                text = medGemmaScore.clinicalInsights,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Modèle: ${medGemmaScore.modelVersion}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 4. Instant Gemini Recommendation (Protected by Firebase App Check)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = strings.geminiTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = RiskModerateContainer,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = strings.geminiDisclaimer,
                                style = MaterialTheme.typography.labelMedium,
                                color = RiskModerateAmber,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        when (geminiState) {
                            is GeminiUiState.Idle, is GeminiUiState.Loading -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                    Text(
                                        text = strings.geminiLoading,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            is GeminiUiState.Streaming -> {
                                Text(
                                    text = geminiState.text,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            is GeminiUiState.Success -> {
                                Text(
                                    text = geminiState.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.testTag("gemini_recommendation_text")
                                )
                            }
                            is GeminiUiState.Error -> {
                                Text(
                                    text = geminiState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                TextButton(
                                    onClick = onRetryGemini,
                                    modifier = Modifier.testTag("retry_gemini_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 6.dp)
                                    )
                                    Text(strings.retryLabel)
                                }
                            }
                        }
                    }
                }

                // 5. Licenses & Updates Button
                OutlinedButton(
                    onClick = { showLicenseSheet = true },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("result_license_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Gavel,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(strings.licensesAndUpdates)
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showLicenseSheet) {
        LicenseAndUpdatesBottomSheet(
            definition = context.definition,
            modelConfig = modelConfig,
            strings = strings.toLicenseCompat(),
            onDismiss = { showLicenseSheet = false }
        )
    }
}
