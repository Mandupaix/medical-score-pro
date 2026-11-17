package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition
import com.example.domain.ScoreInputSpec
import com.example.domain.ScoreOption
import com.example.domain.SeverityThresholds

internal data class SpecialtyMeta(
    val id: String,
    val fr: String,
    val en: String,
    val es: String
)

internal fun opt(value: Int, fr: String, en: String = fr, es: String = en) =
    ScoreOption(value = value, labelFr = fr, labelEn = en, labelEs = es)

internal fun choice(
    key: String,
    fr: String,
    en: String,
    es: String,
    options: List<ScoreOption>,
    defaultValue: Int = options.first().value
) = ScoreInputSpec.Choice(
    key = key,
    labelFr = fr,
    labelEn = en,
    labelEs = es,
    options = options,
    defaultValue = defaultValue
)

internal fun toggle(
    key: String,
    fr: String,
    en: String,
    es: String,
    points: Int = 1,
    defaultChecked: Boolean = false
) = ScoreInputSpec.Toggle(
    key = key,
    labelFr = fr,
    labelEn = en,
    labelEs = es,
    pointsWhenTrue = points,
    defaultChecked = defaultChecked
)

internal fun slider(
    key: String,
    fr: String,
    en: String,
    es: String,
    min: Double,
    max: Double,
    default: Double,
    unit: String = "",
    step: Double = 1.0
) = ScoreInputSpec.NumericSlider(
    key = key,
    labelFr = fr,
    labelEn = en,
    labelEs = es,
    min = min,
    max = max,
    step = step,
    unit = unit,
    defaultValue = default
)

internal fun mcpScore(
    scoreId: String,
    name: String,
    specialty: SpecialtyMeta,
    descFr: String,
    descEn: String,
    descEs: String,
    minScore: Double,
    maxScore: Double,
    inputs: List<ScoreInputSpec>,
    severeBoundary: Double,
    moderateBoundary: Double,
    higherIsWorse: Boolean = true,
    unitDisplay: String = "pts",
    formulaKey: String? = null,
    isCardiac: Boolean = false,
    supportsIntervention: Boolean = true,
    source: String = "@awell-health/awell-score & Medical Calculator MCP",
    license: String = "MIT License"
) = ClinicalScoreDefinition(
    scoreId = scoreId,
    name = name,
    specialtyId = specialty.id,
    specialtyNameFr = specialty.fr,
    specialtyNameEn = specialty.en,
    specialtyNameEs = specialty.es,
    descriptionFr = descFr,
    descriptionEn = descEn,
    descriptionEs = descEs,
    source = source,
    license = license,
    versionHistory = listOf("v2.1.0 (2026-11) — Medical Calculator MCP & Awell Score MIT"),
    supportsInterventionField = supportsIntervention,
    isCardiacRiskScore = isCardiac,
    inputs = inputs,
    minScore = minScore,
    maxScore = maxScore,
    unitDisplay = unitDisplay,
    severityThresholds = SeverityThresholds(
        severeBoundary = severeBoundary,
        moderateBoundary = moderateBoundary,
        higherIsWorse = higherIsWorse
    ),
    formulaKey = formulaKey
)
