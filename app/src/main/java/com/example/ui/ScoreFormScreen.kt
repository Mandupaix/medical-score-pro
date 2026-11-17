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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.AiModelConfigRecord
import com.example.domain.ClinicalScoreDefinition
import com.example.domain.ScoreInputSpec
import com.example.i18n.AppLanguage
import com.example.i18n.AppStrings
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreFormScreen(
    definition: ClinicalScoreDefinition,
    language: AppLanguage,
    modelConfig: AiModelConfigRecord?,
    isSubmitting: Boolean,
    submissionError: String?,
    onBack: () -> Unit,
    onSubmit: (
        numericInputs: Map<String, Double>,
        booleanInputs: Map<String, Boolean>,
        diagnosis: String,
        interventionType: String?,
        privateClinicalNotes: String?
    ) -> Unit
) {
    BackHandler(onBack = onBack)
    val strings = AppStrings.get(language)

    var diagnosis by remember { mutableStateOf("") }
    var interventionType by remember { mutableStateOf("") }
    var privateNotes by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var showLicenseSheet by remember { mutableStateOf(false) }

    val numericInputs = remember(definition.scoreId) {
        mutableStateMapOf<String, Double>().apply {
            definition.inputs.forEach { spec ->
                when (spec) {
                    is ScoreInputSpec.Choice -> put(spec.key, spec.defaultValue.toDouble())
                    is ScoreInputSpec.NumericSlider -> put(spec.key, spec.defaultValue)
                    is ScoreInputSpec.Toggle -> Unit
                }
            }
        }
    }
    val booleanInputs = remember(definition.scoreId) {
        mutableStateMapOf<String, Boolean>().apply {
            definition.inputs.forEach { spec ->
                if (spec is ScoreInputSpec.Toggle) put(spec.key, spec.defaultChecked)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(definition.name) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("form_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showLicenseSheet = true },
                        modifier = Modifier.testTag("form_license_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Gavel,
                            contentDescription = strings.licensesAndUpdates
                        )
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
                    .widthIn(max = 720.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Strict Anonymization Notice (no name, surname, or facility fields)
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PrivacyTip,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = strings.formAnonymousBadge,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                // Diagnosis & Intervention (Anonymized Clinical Context)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = diagnosis,
                            onValueChange = {
                                diagnosis = it
                                validationError = null
                            },
                            label = { Text(strings.diagnosisLabel) },
                            placeholder = { Text(strings.diagnosisPlaceholder) },
                            singleLine = true,
                            isError = validationError != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("diagnosis_input")
                        )

                        if (definition.supportsInterventionField) {
                            OutlinedTextField(
                                value = interventionType,
                                onValueChange = { interventionType = it },
                                label = { Text(strings.interventionLabel) },
                                placeholder = { Text(strings.interventionPlaceholder) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("intervention_input")
                            )
                        }

                        OutlinedTextField(
                            value = privateNotes,
                            onValueChange = { privateNotes = it },
                            label = { Text(strings.encryptedNotesLabel) },
                            supportingText = { Text(strings.encryptedNotesHint) },
                            leadingIcon = {
                                Icon(Icons.Filled.Lock, contentDescription = null)
                            },
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("encrypted_notes_input")
                        )
                    }
                }

                // Clinical Parameter Inputs
                definition.inputs.forEach { spec ->
                    val label = when (language) {
                        AppLanguage.FR -> spec.labelFr
                        AppLanguage.EN -> spec.labelEn
                        AppLanguage.ES -> spec.labelEs
                    }

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (spec) {
                                is ScoreInputSpec.Choice -> {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    val selectedVal = (numericInputs[spec.key] ?: spec.defaultValue.toDouble()).roundToInt()
                                    spec.options.forEach { opt ->
                                        val optText = when (language) {
                                            AppLanguage.FR -> opt.labelFr
                                            AppLanguage.EN -> opt.labelEn
                                            AppLanguage.ES -> opt.labelEs
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = selectedVal == opt.value,
                                                onClick = { numericInputs[spec.key] = opt.value.toDouble() },
                                                modifier = Modifier.testTag("choice_${spec.key}_${opt.value}")
                                            )
                                            Text(
                                                text = optText,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 6.dp)
                                            )
                                        }
                                    }
                                }

                                is ScoreInputSpec.Toggle -> {
                                    val checked = booleanInputs[spec.key] ?: spec.defaultChecked
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.weight(1f).padding(end = 12.dp)
                                        )
                                        Switch(
                                            checked = checked,
                                            onCheckedChange = { booleanInputs[spec.key] = it },
                                            modifier = Modifier.testTag("toggle_${spec.key}")
                                        )
                                    }
                                }

                                is ScoreInputSpec.NumericSlider -> {
                                    val currentVal = (numericInputs[spec.key] ?: spec.defaultValue).toFloat()
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "${"%.1f".format(currentVal)} ${spec.unit}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Slider(
                                        value = currentVal,
                                        onValueChange = { numericInputs[spec.key] = it.toDouble() },
                                        valueRange = spec.min.toFloat()..spec.max.toFloat(),
                                        modifier = Modifier.testTag("slider_${spec.key}")
                                    )
                                }
                            }
                        }
                    }
                }

                val activeError = validationError ?: submissionError
                if (!activeError.isNullOrBlank()) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = activeError,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Button(
                    onClick = {
                        if (diagnosis.isBlank()) {
                            validationError = strings.diagnosisRequiredError
                        } else {
                            onSubmit(
                                numericInputs.toMap(),
                                booleanInputs.toMap(),
                                diagnosis,
                                interventionType.takeIf { it.isNotBlank() },
                                privateNotes.takeIf { it.isNotBlank() }
                            )
                        }
                    },
                    enabled = !isSubmitting,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("calculate_submit_button")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Calculate,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(strings.calculateScoreButton)
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showLicenseSheet) {
        LicenseAndUpdatesBottomSheet(
            definition = definition,
            modelConfig = modelConfig,
            strings = strings.toLicenseCompat(),
            onDismiss = { showLicenseSheet = false }
        )
    }
}
