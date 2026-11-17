package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.AiModelConfigRecord
import com.example.domain.ClinicalScoreDefinition
import com.example.domain.CustomCatalogScore
import com.example.domain.MedicalCalculatorCatalog
import com.example.domain.MedicalSpecialty
import com.example.i18n.AppLanguage
import com.example.i18n.AppStrings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeSpecialtiesScreen(
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    isAdmin: Boolean,
    modelConfig: AiModelConfigRecord?,
    customCatalog: List<CustomCatalogScore>,
    onSelectScore: (String) -> Unit,
    onOpenHistory: () -> Unit,
    onOpenAdmin: () -> Unit,
    onSignOut: () -> Unit
) {
    val strings = AppStrings.get(language)
    var searchQuery by remember { mutableStateOf("") }
    var expandedSpecialtyId by remember { mutableStateOf<String?>("neurology") }
    var licenseDialogDef by remember { mutableStateOf<ClinicalScoreDefinition?>(null) }
    var showLangMenu by remember { mutableStateOf(false) }

    val customDefinitions = remember(customCatalog) {
        customCatalog.map { MedicalCalculatorCatalog.customToDefinition(it) }
    }

    val filteredSpecialties = remember(searchQuery, language) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) MedicalCalculatorCatalog.all31Specialties
        else MedicalCalculatorCatalog.all31Specialties.filter { spec ->
            spec.nameFr.lowercase().contains(q) ||
                spec.nameEn.lowercase().contains(q) ||
                spec.nameEs.lowercase().contains(q) ||
                spec.featuredScores.any { it.name.lowercase().contains(q) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = strings.appTitle,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Spark Zero-Cost • 31 Spécialités • ${MedicalCalculatorCatalog.all152Scores.size} Calculateurs",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    Box {
                        AssistChip(
                            onClick = { showLangMenu = true },
                            label = { Text(language.flagLabel) },
                            modifier = Modifier.testTag("home_lang_chip")
                        )
                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = { showLangMenu = false }
                        ) {
                            AppLanguage.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang.displayName) },
                                    onClick = {
                                        onLanguageChange(lang)
                                        showLangMenu = false
                                    }
                                )
                            }
                        }
                    }

                    if (isAdmin) {
                        IconButton(
                            onClick = onOpenAdmin,
                            modifier = Modifier.testTag("open_admin_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AdminPanelSettings,
                                contentDescription = strings.adminButton,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    IconButton(
                        onClick = onOpenHistory,
                        modifier = Modifier.testTag("open_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = strings.historyButton,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = onSignOut,
                        modifier = Modifier.testTag("sign_out_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = strings.signOut
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
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 840.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Banner + Model Maturity & History Action Row
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(132.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_medical_hero),
                                    contentDescription = strings.specialtiesTitle,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Surface(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "${strings.modelMaturityLabel}: ${modelConfig?.modelMaturity ?: "BOOTSTRAP"}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "${strings.totalEvaluationsLabel}: ${modelConfig?.totalEvaluations ?: 0}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledTonalButton(
                                    onClick = onOpenHistory,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("hero_history_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.History,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 6.dp)
                                    )
                                    Text(strings.historyButton)
                                }
                                OutlinedButton(
                                    onClick = { onSelectScore("glasgow") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("hero_glasgow_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Psychology,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 6.dp)
                                    )
                                    Text("Glasgow")
                                }
                            }
                        }
                    }
                }

                // Quick Launch Calculators
                item {
                    Text(
                        text = strings.quickLaunchTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(MedicalCalculatorCatalog.builtInScores, key = { it.scoreId }) { def ->
                            QuickCalculatorCard(
                                definition = def,
                                language = language,
                                onClick = { onSelectScore(def.scoreId) }
                            )
                        }
                    }
                }

                // Admin Custom Scores (if any added via Firestore score_catalog)
                if (customDefinitions.isNotEmpty()) {
                    item {
                        Text(
                            text = "Scores Administrateur (Firestore)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            customDefinitions.forEach { customDef ->
                                Button(
                                    onClick = { onSelectScore(customDef.scoreId) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("custom_score_${customDef.scoreId}")
                                ) {
                                    Text(customDef.name)
                                }
                            }
                        }
                    }
                }

                // Search bar for 31 specialties
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        placeholder = { Text(strings.searchPlaceholder) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("specialty_search_input")
                    )
                }

                item {
                    Text(
                        text = strings.allSpecialtiesTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(filteredSpecialties, key = { it.id }) { specialty ->
                    SpecialtyAccordionCard(
                        specialty = specialty,
                        language = language,
                        searchQuery = searchQuery,
                        expanded = expandedSpecialtyId == specialty.id || searchQuery.isNotBlank(),
                        onToggleExpand = {
                            expandedSpecialtyId =
                                if (expandedSpecialtyId == specialty.id) null else specialty.id
                        },
                        onSelectScore = onSelectScore,
                        onOpenLicenses = { licenseDialogDef = it },
                        licensesLabel = strings.licensesAndUpdates
                    )
                }
            }
        }
    }

    if (licenseDialogDef != null) {
        LicenseAndUpdatesBottomSheet(
            definition = licenseDialogDef!!,
            modelConfig = modelConfig,
            strings = strings.toLicenseCompat(),
            onDismiss = { licenseDialogDef = null }
        )
    }
}

@Composable
private fun QuickCalculatorCard(
    definition: ClinicalScoreDefinition,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val specLabel = when (language) {
        AppLanguage.FR -> definition.specialtyNameFr
        AppLanguage.EN -> definition.specialtyNameEn
        AppLanguage.ES -> definition.specialtyNameEs
    }
    val icon = when (definition.specialtyId) {
        "cardiology" -> Icons.Filled.Favorite
        "neurology" -> Icons.Filled.Psychology
        "intensive_care", "emergency" -> Icons.Filled.LocalHospital
        else -> Icons.Filled.Calculate
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .widthIn(min = 190.dp, max = 220.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("quick_card_${definition.scoreId}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp).size(22.dp)
                )
            }
            Text(
                text = definition.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = specLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun SpecialtyAccordionCard(
    specialty: MedicalSpecialty,
    language: AppLanguage,
    searchQuery: String,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onSelectScore: (String) -> Unit,
    onOpenLicenses: (ClinicalScoreDefinition) -> Unit,
    licensesLabel: String
) {
    val name = when (language) {
        AppLanguage.FR -> specialty.nameFr
        AppLanguage.EN -> specialty.nameEn
        AppLanguage.ES -> specialty.nameEs
    }
    val q = searchQuery.trim().lowercase()
    val specMatches = q.isEmpty() ||
        specialty.nameFr.lowercase().contains(q) ||
        specialty.nameEn.lowercase().contains(q) ||
        specialty.nameEs.lowercase().contains(q)
    val scoresToDisplay = if (specMatches) {
        specialty.featuredScores
    } else {
        specialty.featuredScores.filter { it.name.lowercase().contains(q) }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand)
                    .padding(16.dp)
                    .testTag("specialty_row_${specialty.id}"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MedicalServices,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${specialty.calculatorCountMcp} calculateurs MCP",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    scoresToDisplay.forEach { score ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectScore(score.scoreId) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                                    .testTag("specialty_score_${specialty.id}_${score.scoreId}"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = score.name,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Text(
                                        text = when (language) {
                                            AppLanguage.FR -> score.descriptionFr
                                            AppLanguage.EN -> score.descriptionEn
                                            AppLanguage.ES -> score.descriptionEs
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(
                                    onClick = { onOpenLicenses(score) },
                                    modifier = Modifier.testTag("license_btn_${score.scoreId}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Gavel,
                                        contentDescription = licensesLabel,
                                        tint = MaterialTheme.colorScheme.secondary
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
