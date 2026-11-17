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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.domain.AiModelConfigRecord
import com.example.domain.CustomCatalogScore
import com.example.i18n.AppLanguage
import com.example.i18n.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    language: AppLanguage,
    modelConfig: AiModelConfigRecord?,
    customCatalog: List<CustomCatalogScore>,
    statusMessage: String?,
    onSaveScore: (CustomCatalogScore) -> Unit,
    onDeleteScore: (String) -> Unit,
    onSaveConfig: (
        totalEvaluations: Long,
        maturity: String,
        awellVersion: String,
        mcpVersion: String,
        hfSpaceUrl: String,
        notice: String
    ) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val strings = AppStrings.get(language)

    var scoreId by remember { mutableStateOf("") }
    var scoreName by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("Cardiology") }
    var description by remember { mutableStateOf("") }
    var minScoreText by remember { mutableStateOf("0") }
    var maxScoreText by remember { mutableStateOf("10") }

    var hfSpaceUrl by remember(modelConfig) { mutableStateOf(modelConfig?.hfSpaceUrl ?: "") }
    var awellVersion by remember(modelConfig) { mutableStateOf(modelConfig?.awellScoreVersion ?: "1.3.0") }
    var mcpVersion by remember(modelConfig) { mutableStateOf(modelConfig?.medicalCalcMcpVersion ?: "2.1.0") }
    var maturity by remember(modelConfig) { mutableStateOf(modelConfig?.modelMaturity ?: "BOOTSTRAP") }
    var notice by remember(modelConfig) { mutableStateOf(modelConfig?.latestSocietyNotice ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.adminTitle) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("admin_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                if (!statusMessage.isNullOrBlank()) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = statusMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // 1. Add / Update Custom Score in Firestore score_catalog
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = strings.adminAddScoreTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = scoreId,
                            onValueChange = { scoreId = it },
                            label = { Text("Score ID (ex: sofa_custom)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("admin_score_id")
                        )
                        OutlinedTextField(
                            value = scoreName,
                            onValueChange = { scoreName = it },
                            label = { Text("Nom du score") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("admin_score_name")
                        )
                        OutlinedTextField(
                            value = specialty,
                            onValueChange = { specialty = it },
                            label = { Text("Spécialité") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description clinique") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = minScoreText,
                                onValueChange = { minScoreText = it },
                                label = { Text("Min") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = maxScoreText,
                                onValueChange = { maxScoreText = it },
                                label = { Text("Max") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Button(
                            onClick = {
                                if (scoreId.isNotBlank() && scoreName.isNotBlank() && description.isNotBlank()) {
                                    onSaveScore(
                                        CustomCatalogScore(
                                            scoreId = scoreId.trim(),
                                            name = scoreName.trim(),
                                            specialty = specialty.trim().ifBlank { "General" },
                                            description = description.trim(),
                                            minScore = minScoreText.toDoubleOrNull() ?: 0.0,
                                            maxScore = maxScoreText.toDoubleOrNull() ?: 10.0,
                                            active = true
                                        )
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("admin_save_score_button")
                        ) {
                            Icon(Icons.Filled.AddCircle, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                            Text(strings.saveButton)
                        }
                    }
                }

                // Existing custom scores list
                if (customCatalog.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Scores Firestore actifs (${customCatalog.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                            customCatalog.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(item.name, style = MaterialTheme.typography.labelLarge)
                                        Text("${item.specialty} • ${item.scoreId}", style = MaterialTheme.typography.labelSmall)
                                    }
                                    IconButton(
                                        onClick = { onDeleteScore(item.scoreId) },
                                        modifier = Modifier.testTag("admin_delete_${item.scoreId}")
                                    ) {
                                        Icon(Icons.Filled.Delete, contentDescription = strings.deleteButton)
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Global Configuration (ai_model_config/current)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = strings.adminConfigTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("BOOTSTRAP", "CALIBRATING", "MATURE").forEach { tier ->
                                FilterChip(
                                    selected = maturity == tier,
                                    onClick = { maturity = tier },
                                    label = { Text(tier) }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = hfSpaceUrl,
                            onValueChange = { hfSpaceUrl = it },
                            label = { Text("Hugging Face Space URL (MedGemma FastAPI)") },
                            placeholder = { Text("https://xxx-medgemma.hf.space") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("admin_hf_url")
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = awellVersion,
                                onValueChange = { awellVersion = it },
                                label = { Text("awell-score") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = mcpVersion,
                                onValueChange = { mcpVersion = it },
                                label = { Text("medical-calc-mcp") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = notice,
                            onValueChange = { notice = it },
                            label = { Text("Note sociétés savantes (ESC / AHA / SFAR)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                onSaveConfig(
                                    modelConfig?.totalEvaluations ?: 0L,
                                    maturity,
                                    awellVersion,
                                    mcpVersion,
                                    hfSpaceUrl,
                                    notice
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("admin_save_config_button")
                        ) {
                            Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                            Text(strings.saveButton)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
