package com.example.domain

import com.example.i18n.AppLanguage
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.roundToInt

/**
 * Deterministic Clinical Score Engine aligned with @awell-health/awell-score (MIT)
 * and Medical Calculator MCP specifications. AI never computes standard clinical scores.
 */
object ClinicalScoreEngine {

    fun calculate(
        definition: ClinicalScoreDefinition,
        numericInputs: Map<String, Double>,
        booleanInputs: Map<String, Boolean>,
        language: AppLanguage
    ): CalculatedScoreResult {
        definition.formulaKey?.let { key ->
            return FormulaCalculators.evaluate(key, definition, numericInputs, booleanInputs, language)
        }
        return when (definition.scoreId) {
            "glasgow" -> calculateGlasgow(definition, numericInputs, language)
            "euroscore_ii" -> calculateEuroScoreII(definition, numericInputs, booleanInputs, language)
            "cha2ds2_vasc" -> calculateCha2ds2Vasc(definition, numericInputs, booleanInputs, language)
            "qsofa" -> calculateQSofa(definition, booleanInputs, language)
            "curb_65" -> calculateCurb65(definition, booleanInputs, language)
            "phq_9" -> calculatePhq9(definition, numericInputs, language)
            "apgar" -> calculateApgar(definition, numericInputs, language)
            "heart_score" -> calculateHeartScore(definition, numericInputs, language)
            "meld" -> calculateMeld(definition, numericInputs, language)
            else -> calculateGenericAdditive(definition, numericInputs, booleanInputs, language)
        }
    }

    private fun calculateGlasgow(
        def: ClinicalScoreDefinition,
        inputs: Map<String, Double>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val eye = (inputs["eye"] ?: 4.0).roundToInt().coerceIn(1, 4)
        val verbal = (inputs["verbal"] ?: 5.0).roundToInt().coerceIn(1, 5)
        val motor = (inputs["motor"] ?: 6.0).roundToInt().coerceIn(1, 6)
        val total = eye + verbal + motor

        val (severityLabel, level) = when {
            total <= 8 -> when (lang) {
                AppLanguage.FR -> "Coma sévère (≤ 8)"
                AppLanguage.EN -> "Severe coma (≤ 8)"
                AppLanguage.ES -> "Coma grave (≤ 8)"
            } to SeverityLevel.SEVERE
            total <= 12 -> when (lang) {
                AppLanguage.FR -> "Altération modérée (9–12)"
                AppLanguage.EN -> "Moderate impairment (9–12)"
                AppLanguage.ES -> "Alteración moderada (9–12)"
            } to SeverityLevel.MODERATE
            else -> when (lang) {
                AppLanguage.FR -> "Léger / Normal (13–15)"
                AppLanguage.EN -> "Mild / Normal (13–15)"
                AppLanguage.ES -> "Leve / Normal (13–15)"
            } to SeverityLevel.LOW
        }

        val breakdown = listOf(
            "E (Ouverture des yeux / Eye) = $eye / 4",
            "V (Réponse verbale / Verbal) = $verbal / 5",
            "M (Réponse motrice / Motor) = $motor / 6",
            "Total GCS = E($eye) + V($verbal) + M($motor) = $total / 15"
        )

        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = total.toDouble(),
            displayValue = "$total / 15",
            severityLabel = severityLabel,
            severityLevel = level,
            breakdownLines = breakdown,
            serializedInputs = mapOf("eye" to eye, "verbal" to verbal, "motor" to motor)
        )
    }

    private fun calculateEuroScoreII(
        def: ClinicalScoreDefinition,
        numInputs: Map<String, Double>,
        boolInputs: Map<String, Boolean>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val age = (numInputs["age"] ?: 65.0).coerceIn(18.0, 105.0)
        val female = boolInputs["female"] ?: false
        val crClMode = (numInputs["renal"] ?: 0.0).roundToInt() // 0 normal, 1 mod(50-85), 2 sev(<50), 3 dialysis
        val nyha = (numInputs["nyha"] ?: 1.0).roundToInt().coerceIn(1, 4)
        val lvef = (numInputs["lvef"] ?: 0.0).roundToInt() // 0 >50%, 1 31-50%, 2 21-30%, 3 <=20%
        val urgent = boolInputs["urgent"] ?: false
        val recentMi = boolInputs["recent_mi"] ?: false
        val iddm = boolInputs["iddm"] ?: false
        val extraCardiacArteriopathy = boolInputs["arteriopathy"] ?: false

        // Published Nashef et al. (2012) EuroSCORE II logistic regression coefficients
        var z = -5.324537
        val breakdown = mutableListOf("Constante logistique β₀ = -5.3245")

        val ageOver60 = (age - 60.0).coerceAtLeast(0.0)
        val ageBeta = ageOver60 * 0.0285181
        z += ageBeta
        breakdown.add("Âge (${age.roundToInt()} ans) : +${"%.4f".format(ageBeta)}")

        if (female) {
            z += 0.2196434
            breakdown.add("Sexe féminin : +0.2196")
        }
        when (crClMode) {
            1 -> { z += 0.303553; breakdown.add("Clairance créatinine 50–85 mL/min : +0.3036") }
            2 -> { z += 0.8592256; breakdown.add("Clairance créatinine < 50 mL/min : +0.8592") }
            3 -> { z += 0.6421508; breakdown.add("Dialyse chronique : +0.6422") }
        }
        when (nyha) {
            2 -> { z += 0.1070545; breakdown.add("NYHA II : +0.1071") }
            3 -> { z += 0.2958358; breakdown.add("NYHA III : +0.2958") }
            4 -> { z += 0.5597929; breakdown.add("NYHA IV : +0.5598") }
        }
        when (lvef) {
            1 -> { z += 0.3150652; breakdown.add("FEVG 31–50% : +0.3151") }
            2 -> { z += 0.8084096; breakdown.add("FEVG 21–30% : +0.8084") }
            3 -> { z += 0.9346919; breakdown.add("FEVG ≤ 20% : +0.9347") }
        }
        if (recentMi) {
            z += 0.1528943
            breakdown.add("IDM récent (< 90j) : +0.1529")
        }
        if (iddm) {
            z += 0.3542749
            breakdown.add("Diabète insulinotraité : +0.3543")
        }
        if (extraCardiacArteriopathy) {
            z += 0.5360268
            breakdown.add("Artériopathie extracardiaque : +0.5360")
        }
        if (urgent) {
            z += 0.3174673
            breakdown.add("Chirurgie urgente : +0.3175")
        }

        val riskPercent = (exp(z) / (1.0 + exp(z))) * 100.0
        val rounded = (riskPercent * 100.0).roundToInt() / 100.0
        breakdown.add("Logit z = ${"%.4f".format(z)} → Risque = ${"%.2f".format(rounded)}%")

        val (severityLabel, level, guidance) = when {
            rounded >= 8.0 -> Triple(
                when (lang) {
                    AppLanguage.FR -> "Risque élevé (≥ 8%)"
                    AppLanguage.EN -> "High risk (≥ 8%)"
                    AppLanguage.ES -> "Riesgo alto (≥ 8%)"
                },
                SeverityLevel.SEVERE,
                when (lang) {
                    AppLanguage.FR -> "Discussion Heart Team obligatoire : envisager une approche hybride ou percutanée (TAVI/TEER) et optimisation hémodynamique pré-opératoire."
                    AppLanguage.EN -> "Heart Team review required: consider percutaneous/hybrid alternatives (TAVI/TEER) and pre-op hemodynamic optimization."
                    AppLanguage.ES -> "Evaluación por Heart Team obligatoria: valorar alternativas percutáneas/híbridas y optimización preoperatoria."
                }
            )
            rounded >= 4.0 -> Triple(
                when (lang) {
                    AppLanguage.FR -> "Risque intermédiaire (4–8%)"
                    AppLanguage.EN -> "Intermediate risk (4–8%)"
                    AppLanguage.ES -> "Riesgo intermedio (4–8%)"
                },
                SeverityLevel.MODERATE,
                when (lang) {
                    AppLanguage.FR -> "Évaluation pluridisciplinaire recommandée : bilan rénal, échocardiographie complète et surveillance post-opératoire en soins intensifs."
                    AppLanguage.EN -> "Multidisciplinary assessment advised: renal optimization, comprehensive echo, and planned ICU post-op monitoring."
                    AppLanguage.ES -> "Evaluación multidisciplinaria recomendada: optimización renal y vigilancia postoperatoria en UCI."
                }
            )
            else -> Triple(
                when (lang) {
                    AppLanguage.FR -> "Risque faible (< 4%)"
                    AppLanguage.EN -> "Low risk (< 4%)"
                    AppLanguage.ES -> "Riesgo bajo (< 4%)"
                },
                SeverityLevel.LOW,
                when (lang) {
                    AppLanguage.FR -> "Risque opératoire standard : parcours chirurgical conventionnel avec prévention du risque infectieux et rénal."
                    AppLanguage.EN -> "Standard surgical risk: conventional surgical pathway with routine renal and infection prophylaxis."
                    AppLanguage.ES -> "Riesgo quirúrgico estándar: vía quirúrgica convencional con profilaxis habitual."
                }
            )
        }

        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = rounded,
            displayValue = "${"%.2f".format(rounded)}%",
            severityLabel = severityLabel,
            severityLevel = level,
            cardiacRiskPercent = rounded,
            cardiacGuidance = guidance,
            breakdownLines = breakdown,
            serializedInputs = mapOf(
                "age" to age.roundToInt(),
                "female" to female,
                "renal" to crClMode,
                "nyha" to nyha,
                "lvef" to lvef,
                "urgent" to urgent,
                "recent_mi" to recentMi,
                "iddm" to iddm,
                "arteriopathy" to extraCardiacArteriopathy
            )
        )
    }

    private fun calculateCha2ds2Vasc(
        def: ClinicalScoreDefinition,
        numInputs: Map<String, Double>,
        boolInputs: Map<String, Boolean>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        var score = 0
        val breakdown = mutableListOf<String>()
        fun add(flag: Boolean, pts: Int, label: String) {
            if (flag) {
                score += pts
                breakdown.add("$label : +$pts")
            } else {
                breakdown.add("$label : +0")
            }
        }
        add(boolInputs["chf"] ?: false, 1, "C - Insuffisance cardiaque / FEVG ≤ 40%")
        add(boolInputs["htn"] ?: false, 1, "H - Hypertension artérielle")
        val ageBand = (numInputs["age_band"] ?: 0.0).roundToInt() // 0 <65, 1 65-74, 2 >=75
        if (ageBand == 2) {
            score += 2; breakdown.add("A₂ - Âge ≥ 75 ans : +2")
        } else if (ageBand == 1) {
            score += 1; breakdown.add("A - Âge 65–74 ans : +1")
        } else {
            breakdown.add("A - Âge < 65 ans : +0")
        }
        add(boolInputs["diabetes"] ?: false, 1, "D - Diabète")
        add(boolInputs["stroke"] ?: false, 2, "S₂ - AVC / AIT / Thrombo-embolie")
        add(boolInputs["vascular"] ?: false, 1, "V - Maladie vasculaire")
        add(boolInputs["female"] ?: false, 1, "Sc - Sexe féminin")

        val annualStrokeRisk = when (score) {
            0 -> 0.2
            1 -> 0.6
            2 -> 2.2
            3 -> 3.2
            4 -> 4.8
            5 -> 7.2
            6 -> 9.7
            7 -> 11.2
            8 -> 10.8
            else -> 12.2
        }

        val (severityLabel, level, guidance) = when {
            score >= 2 -> Triple(
                when (lang) {
                    AppLanguage.FR -> "Risque thrombo-embolique élevé ($score pts)"
                    AppLanguage.EN -> "High thromboembolic risk ($score pts)"
                    AppLanguage.ES -> "Alto riesgo tromboembólico ($score pts)"
                },
                SeverityLevel.SEVERE,
                when (lang) {
                    AppLanguage.FR -> "Anticoagulation orale recommandée (AOD en 1re intention selon directives ESC), après évaluation du risque hémorragique (HAS-BLED)."
                    AppLanguage.EN -> "Oral anticoagulation recommended (DOAC preferred per ESC guidelines) after bleeding risk evaluation (HAS-BLED)."
                    AppLanguage.ES -> "Anticoagulación oral recomendada (ACOD de primera línea según guías ESC), tras valorar HAS-BLED."
                }
            )
            score == 1 -> Triple(
                when (lang) {
                    AppLanguage.FR -> "Risque intermédiaire (1 pt)"
                    AppLanguage.EN -> "Intermediate risk (1 pt)"
                    AppLanguage.ES -> "Riesgo intermedio (1 pt)"
                },
                SeverityLevel.MODERATE,
                when (lang) {
                    AppLanguage.FR -> "Anticoagulation à discuter selon le sexe et le profil individuel du patient (Classe IIa)."
                    AppLanguage.EN -> "Consider oral anticoagulation based on sex-independent risk factors and patient preference."
                    AppLanguage.ES -> "Valorar anticoagulación oral según factores individuales del paciente."
                }
            )
            else -> Triple(
                when (lang) {
                    AppLanguage.FR -> "Risque faible (0 pt)"
                    AppLanguage.EN -> "Low risk (0 pt)"
                    AppLanguage.ES -> "Riesgo bajo (0 pt)"
                },
                SeverityLevel.LOW,
                when (lang) {
                    AppLanguage.FR -> "Pas d'anticoagulation recommandée en l'absence d'autre indication."
                    AppLanguage.EN -> "No antithrombotic therapy recommended."
                    AppLanguage.ES -> "No se recomienda terapia antitrombótica."
                }
            )
        }

        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = score.toDouble(),
            displayValue = "$score / 9",
            severityLabel = severityLabel,
            severityLevel = level,
            cardiacRiskPercent = annualStrokeRisk,
            cardiacGuidance = guidance,
            breakdownLines = breakdown + "Risque annuel estimé d'AVC : ${annualStrokeRisk}%",
            serializedInputs = mapOf(
                "chf" to (boolInputs["chf"] ?: false),
                "htn" to (boolInputs["htn"] ?: false),
                "age_band" to ageBand,
                "diabetes" to (boolInputs["diabetes"] ?: false),
                "stroke" to (boolInputs["stroke"] ?: false),
                "vascular" to (boolInputs["vascular"] ?: false),
                "female" to (boolInputs["female"] ?: false)
            )
        )
    }

    private fun calculateQSofa(
        def: ClinicalScoreDefinition,
        boolInputs: Map<String, Boolean>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val rr = boolInputs["rr_high"] ?: false
        val mental = boolInputs["altered_mentation"] ?: false
        val sbp = boolInputs["sbp_low"] ?: false
        val total = listOf(rr, mental, sbp).count { it }
        val (label, level) = if (total >= 2) {
            when (lang) {
                AppLanguage.FR -> "Haut risque de sepsis (≥ 2)"
                AppLanguage.EN -> "High sepsis risk (≥ 2)"
                AppLanguage.ES -> "Alto riesgo de sepsis (≥ 2)"
            } to SeverityLevel.SEVERE
        } else {
            when (lang) {
                AppLanguage.FR -> "Risque faible (< 2)"
                AppLanguage.EN -> "Low risk (< 2)"
                AppLanguage.ES -> "Riesgo bajo (< 2)"
            } to SeverityLevel.LOW
        }
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = total.toDouble(),
            displayValue = "$total / 3",
            severityLabel = label,
            severityLevel = level,
            breakdownLines = listOf(
                "FR ≥ 22/min : ${if (rr) "+1" else "0"}",
                "Altération conscience (GCS < 15) : ${if (mental) "+1" else "0"}",
                "PAS ≤ 100 mmHg : ${if (sbp) "+1" else "0"}"
            ),
            serializedInputs = mapOf("rr_high" to rr, "altered_mentation" to mental, "sbp_low" to sbp)
        )
    }

    private fun calculateCurb65(
        def: ClinicalScoreDefinition,
        boolInputs: Map<String, Boolean>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val keys = listOf("confusion", "urea", "rr", "bp", "age65")
        val total = keys.count { boolInputs[it] == true }
        val (label, level) = when {
            total >= 3 -> (if (lang == AppLanguage.FR) "Pneumonie sévère (≥ 3)" else "Severe pneumonia (≥ 3)") to SeverityLevel.SEVERE
            total == 2 -> (if (lang == AppLanguage.FR) "Risque modéré (2)" else "Moderate risk (2)") to SeverityLevel.MODERATE
            else -> (if (lang == AppLanguage.FR) "Risque faible (0–1)" else "Low risk (0–1)") to SeverityLevel.LOW
        }
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = total.toDouble(),
            displayValue = "$total / 5",
            severityLabel = label,
            severityLevel = level,
            breakdownLines = keys.map { "$it : ${if (boolInputs[it] == true) "+1" else "0"}" },
            serializedInputs = keys.associateWith { boolInputs[it] == true }
        )
    }

    private fun calculatePhq9(
        def: ClinicalScoreDefinition,
        numInputs: Map<String, Double>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val total = def.inputs.sumOf { (numInputs[it.key] ?: 0.0).roundToInt().coerceIn(0, 3) }
        val (label, level) = when {
            total >= 20 -> (if (lang == AppLanguage.FR) "Dépression sévère (≥ 20)" else "Severe depression (≥ 20)") to SeverityLevel.SEVERE
            total >= 10 -> (if (lang == AppLanguage.FR) "Dépression modérée (10–19)" else "Moderate depression (10–19)") to SeverityLevel.MODERATE
            else -> (if (lang == AppLanguage.FR) "Symptômes minimes/légers (< 10)" else "Minimal/mild (< 10)") to SeverityLevel.LOW
        }
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = total.toDouble(),
            displayValue = "$total / 27",
            severityLabel = label,
            severityLevel = level,
            breakdownLines = def.inputs.map { "${it.labelEn}: ${(numInputs[it.key] ?: 0.0).roundToInt()} / 3" },
            serializedInputs = def.inputs.associate { it.key to (numInputs[it.key] ?: 0.0).roundToInt() }
        )
    }

    private fun calculateApgar(
        def: ClinicalScoreDefinition,
        numInputs: Map<String, Double>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val total = def.inputs.sumOf { (numInputs[it.key] ?: 2.0).roundToInt().coerceIn(0, 2) }
        val (label, level) = when {
            total <= 3 -> (if (lang == AppLanguage.FR) "Détresse critique (0–3)" else "Critically low (0–3)") to SeverityLevel.SEVERE
            total <= 6 -> (if (lang == AppLanguage.FR) "Modérément abaissé (4–6)" else "Moderately abnormal (4–6)") to SeverityLevel.MODERATE
            else -> (if (lang == AppLanguage.FR) "Adaptation normale (7–10)" else "Reassuring (7–10)") to SeverityLevel.LOW
        }
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = total.toDouble(),
            displayValue = "$total / 10",
            severityLabel = label,
            severityLevel = level,
            breakdownLines = def.inputs.map { "${it.labelEn}: ${(numInputs[it.key] ?: 2.0).roundToInt()} / 2" },
            serializedInputs = def.inputs.associate { it.key to (numInputs[it.key] ?: 2.0).roundToInt() }
        )
    }

    private fun calculateHeartScore(
        def: ClinicalScoreDefinition,
        numInputs: Map<String, Double>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val total = def.inputs.sumOf { (numInputs[it.key] ?: 0.0).roundToInt().coerceIn(0, 2) }
        val (label, level) = when {
            total >= 7 -> (if (lang == AppLanguage.FR) "Risque MACE élevé (7–10)" else "High MACE risk (7–10)") to SeverityLevel.SEVERE
            total >= 4 -> (if (lang == AppLanguage.FR) "Risque MACE modéré (4–6)" else "Moderate MACE risk (4–6)") to SeverityLevel.MODERATE
            else -> (if (lang == AppLanguage.FR) "Risque MACE faible (0–3)" else "Low MACE risk (0–3)") to SeverityLevel.LOW
        }
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = total.toDouble(),
            displayValue = "$total / 10",
            severityLabel = label,
            severityLevel = level,
            breakdownLines = def.inputs.map { "${it.labelEn}: ${(numInputs[it.key] ?: 0.0).roundToInt()} / 2" },
            serializedInputs = def.inputs.associate { it.key to (numInputs[it.key] ?: 0.0).roundToInt() }
        )
    }

    private fun calculateMeld(
        def: ClinicalScoreDefinition,
        numInputs: Map<String, Double>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        val bili = (numInputs["bilirubin"] ?: 1.0).coerceAtLeast(1.0)
        val inr = (numInputs["inr"] ?: 1.0).coerceAtLeast(1.0)
        val creat = (numInputs["creatinine"] ?: 1.0).coerceIn(1.0, 4.0)
        val raw = 3.78 * ln(bili) + 11.2 * ln(inr) + 9.57 * ln(creat) + 6.43
        val total = raw.roundToInt().coerceIn(6, 40)
        val (label, level) = when {
            total >= 25 -> (if (lang == AppLanguage.FR) "MELD élevé (≥ 25)" else "High MELD (≥ 25)") to SeverityLevel.SEVERE
            total >= 15 -> (if (lang == AppLanguage.FR) "MELD intermédiaire (15–24)" else "Moderate MELD (15–24)") to SeverityLevel.MODERATE
            else -> (if (lang == AppLanguage.FR) "MELD faible (< 15)" else "Low MELD (< 15)") to SeverityLevel.LOW
        }
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = total.toDouble(),
            displayValue = "$total / 40",
            severityLabel = label,
            severityLevel = level,
            breakdownLines = listOf(
                "Bilirubine = ${"%.1f".format(bili)} mg/dL (ln=${"%.2f".format(ln(bili))})",
                "INR = ${"%.2f".format(inr)} (ln=${"%.2f".format(ln(inr))})",
                "Créatinine = ${"%.1f".format(creat)} mg/dL (ln=${"%.2f".format(ln(creat))})",
                "MELD = 3.78×ln(Bili) + 11.2×ln(INR) + 9.57×ln(Cr) + 6.43 = $total"
            ),
            serializedInputs = mapOf("bilirubin" to bili, "inr" to inr, "creatinine" to creat)
        )
    }

    private fun calculateGenericAdditive(
        def: ClinicalScoreDefinition,
        numInputs: Map<String, Double>,
        boolInputs: Map<String, Boolean>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        var total = 0.0
        val breakdown = mutableListOf<String>()
        val serialized = mutableMapOf<String, Any>()
        def.inputs.forEach { spec ->
            when (spec) {
                is ScoreInputSpec.Choice -> {
                    val v = (numInputs[spec.key] ?: spec.defaultValue.toDouble()).roundToInt()
                    total += v
                    serialized[spec.key] = v
                    breakdown.add("${spec.labelEn}: +$v")
                }
                is ScoreInputSpec.Toggle -> {
                    val checked = boolInputs[spec.key] ?: spec.defaultChecked
                    val pts = if (checked) spec.pointsWhenTrue else 0
                    total += pts
                    serialized[spec.key] = checked
                    breakdown.add("${spec.labelEn}: +$pts")
                }
                is ScoreInputSpec.NumericSlider -> {
                    val v = numInputs[spec.key] ?: spec.defaultValue
                    total += v
                    serialized[spec.key] = v
                    breakdown.add("${spec.labelEn}: ${"%.1f".format(v)} ${spec.unit}")
                }
            }
        }
        val clamped = total.coerceIn(def.minScore, def.maxScore)
        val (label, level) = if (def.severityThresholds != null) {
            FormulaCalculators.formatSeverity(clamped, def.severityThresholds, lang)
        } else {
            val ratio = if (def.maxScore > def.minScore) (clamped - def.minScore) / (def.maxScore - def.minScore) else 0.0
            when {
                ratio >= 0.66 -> when (lang) {
                    AppLanguage.FR -> "Élevé / Critique"
                    AppLanguage.EN -> "High / Critical"
                    AppLanguage.ES -> "Alto / Crítico"
                } to SeverityLevel.SEVERE
                ratio >= 0.33 -> when (lang) {
                    AppLanguage.FR -> "Modéré / Intermédiaire"
                    AppLanguage.EN -> "Moderate / Intermediate"
                    AppLanguage.ES -> "Moderado / Intermedio"
                } to SeverityLevel.MODERATE
                else -> when (lang) {
                    AppLanguage.FR -> "Faible / Normal"
                    AppLanguage.EN -> "Low / Normal"
                    AppLanguage.ES -> "Bajo / Normal"
                } to SeverityLevel.LOW
            }
        }
        val display = when (def.unitDisplay) {
            "pts" -> "${clamped.roundToInt()} / ${def.maxScore.roundToInt()}"
            "%" -> "${clamped.roundToInt()}%"
            else -> "${def.unitDisplay} ${clamped.roundToInt()}"
        }
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = clamped,
            displayValue = display,
            severityLabel = label,
            severityLevel = level,
            breakdownLines = breakdown,
            serializedInputs = serialized
        )
    }
}
