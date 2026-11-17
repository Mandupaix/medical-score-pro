package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.MedGemmaScoreRecord
import com.example.domain.UserEvaluationRecord
import com.example.i18n.AppLanguage
import com.example.i18n.AppStrings
import com.example.security.CryptoManager
import com.example.ui.theme.MedGemmaContainer
import com.example.ui.theme.MedGemmaIndigo
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    language: AppLanguage,
    historyState: UiState<List<UserEvaluationRecord>>,
    medGemmaMap: Map<String, MedGemmaScoreRecord>,
    diagnosisFilter: String,
    dateFilter: DateRangeFilter,
    onDiagnosisFilterChange: (String) -> Unit,
    onDateFilterChange: (DateRangeFilter) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val strings = AppStrings.get(language)
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.historyTitle) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("history_back_button")
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
                    .widthIn(max = 800.dp)
                    .fillMaxSize()
            ) {
                // Search & Date Range Filters
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = diagnosisFilter,
                        onValueChange = onDiagnosisFilterChange,
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        placeholder = { Text(strings.filterByDiagnosis) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("history_diagnosis_filter")
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val ranges = listOf(
                            DateRangeFilter.ALL to strings.dateRangeAll,
                            DateRangeFilter.TODAY to strings.dateRangeToday,
                            DateRangeFilter.SEVEN_DAYS to strings.dateRange7Days,
                            DateRangeFilter.THIRTY_DAYS to strings.dateRange30Days
                        )
                        items(ranges) { (filter, label) ->
                            FilterChip(
                                selected = dateFilter == filter,
                                onClick = { onDateFilterChange(filter) },
                                label = { Text(label) },
                                modifier = Modifier.testTag("history_date_${filter.name}")
                            )
                        }
                    }
                }

                when (historyState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Error -> {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.padding(16.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = historyState.message,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    is UiState.Success -> {
                        if (historyState.data.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_empty_history),
                                    contentDescription = strings.historyEmptyTitle,
                                    modifier = Modifier.size(160.dp)
                                )
                                Text(
                                    text = strings.historyEmptyTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                                Text(
                                    text = strings.historyEmptySubtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(historyState.data, key = { it.id }) { record ->
                                    HistoryEvaluationCard(
                                        record = record,
                                        medGemmaScore = medGemmaMap[record.id],
                                        dateFormatter = dateFormatter,
                                        strings = strings
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryEvaluationCard(
    record: UserEvaluationRecord,
    medGemmaScore: MedGemmaScoreRecord?,
    dateFormatter: SimpleDateFormat,
    strings: com.example.i18n.LocalizedStrings
) {
    var decryptedNotes by remember(record.id) { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.testTag("history_card_${record.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.scoreName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = record.specialty,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = record.clinicalScoreDisplay,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = record.diagnosis,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = record.severityLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Live MedGemma Async Update Badge in History
            Surface(
                color = MedGemmaContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Biotech,
                        contentDescription = null,
                        tint = MedGemmaIndigo,
                        modifier = Modifier.size(18.dp)
                    )
                    if (medGemmaScore == null) {
                        Text(
                            text = strings.medGemmaPending,
                            style = MaterialTheme.typography.labelMedium,
                            color = MedGemmaIndigo
                        )
                    } else {
                        Text(
                            text = "MedGemma IA: ${medGemmaScore.improvementScore.toInt()}/100 — ${medGemmaScore.clinicalInsights}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MedGemmaIndigo
                        )
                    }
                }
            }

            if (!record.geminiRecommendation.isNullOrBlank()) {
                Text(
                    text = record.geminiRecommendation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Keystore Encrypted Notes Decrypt Button
            if (!record.encryptedClinicalNotesCiphertext.isNullOrBlank() &&
                !record.encryptedClinicalNotesIv.isNullOrBlank()
            ) {
                if (decryptedNotes == null) {
                    TextButton(
                        onClick = {
                            decryptedNotes = try {
                                CryptoManager.decrypt(
                                    record.encryptedClinicalNotesCiphertext,
                                    record.encryptedClinicalNotesIv
                                )
                            } catch (e: Exception) {
                                "Keystore decryption unavailable (${e.localizedMessage})"
                            }
                        },
                        modifier = Modifier.testTag("decrypt_notes_${record.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LockOpen,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(strings.decryptNotesButton)
                    }
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = decryptedNotes!!,
                                style = MaterialTheme.typography.bodySmall
                            )
                            TextButton(onClick = { decryptedNotes = null }) {
                                Text(strings.hideNotesButton)
                            }
                        }
                    }
                }
            }

            val timestampStr = record.createdAt?.toDate()?.let { dateFormatter.format(it) } ?: "..."
            Text(
                text = timestampStr,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
