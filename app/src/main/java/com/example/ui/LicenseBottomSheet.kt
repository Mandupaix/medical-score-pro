package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.AiModelConfigRecord
import com.example.domain.ClinicalScoreDefinition
import com.example.i18n.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseAndUpdatesBottomSheet(
    definition: ClinicalScoreDefinition,
    modelConfig: AiModelConfigRecord?,
    strings: AppStringsCompat,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Gavel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${definition.name} — ${strings.licensesTitle}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${strings.sourceLabel}: ${definition.source}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${strings.licenseLabel}: ${definition.license}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (modelConfig != null) {
                        Text(
                            text = "@awell-health/awell-score v${modelConfig.awellScoreVersion} • medical-calc-mcp v${modelConfig.medicalCalcMcpVersion}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            HorizontalDivider()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Update,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = strings.versionHistoryLabel,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            definition.versionHistory.forEach { entry ->
                Text(
                    text = "• $entry",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!modelConfig?.latestSocietyNotice.isNullOrBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = modelConfig!!.latestSocietyNotice!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_license_sheet_button")
                ) {
                    Text(strings.closeLabel)
                }
            }
        }
    }
}

data class StringsCompat(
    val licensesTitle: String,
    val sourceLabel: String,
    val licenseLabel: String,
    val versionHistoryLabel: String,
    val closeLabel: String
)

typealias AppStringsCompat = StringsCompat

fun com.example.i18n.LocalizedStrings.toLicenseCompat(): AppStringsCompat = AppStringsCompat(
    licensesTitle = licensesAndUpdates,
    sourceLabel = licenseSourceLabel,
    licenseLabel = licenseTypeLabel,
    versionHistoryLabel = versionHistoryLabel,
    closeLabel = closeButton
)
