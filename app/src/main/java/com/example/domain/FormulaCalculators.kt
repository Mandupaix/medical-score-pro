package com.example.domain

import com.example.i18n.AppLanguage
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Deterministic physiological & clinical equation evaluators for non-additive MCP calculators.
 */
internal object FormulaCalculators {

    fun evaluate(
        formulaKey: String,
        def: ClinicalScoreDefinition,
        num: Map<String, Double>,
        bool: Map<String, Boolean>,
        lang: AppLanguage
    ): CalculatedScoreResult {
        return when (formulaKey) {
            "aspects_inverse" -> evalAspects(def, bool, lang)
            "grace_acs" -> evalGraceAcs(def, num, bool, lang)
            "qtc_bazett" -> evalQtcBazett(def, num, lang)
            "map_bp" -> evalMapBp(def, num, lang)
            "framingham_proxy" -> evalFramingham(def, num, bool, lang)
            "rass_abs" -> evalRass(def, num, lang)
            "murray_avg" -> evalMurray(def, num, lang)
            "pf_ratio" -> evalPfRatio(def, num, lang)
            "shock_index" -> evalShockIndex(def, num, lang)
            "wells_pe" -> evalWellsPe(def, bool, lang)
            "psi_port" -> evalPsiPort(def, num, bool, lang)
            "meld_na" -> evalMeldNa(def, num, lang)
            "fib_4" -> evalFib4(def, num, lang)
            "maddrey_df" -> evalMaddrey(def, num, lang)
            "apri_liver" -> evalApri(def, num, lang)
            "ckd_epi_2021" -> evalCkdEpi2021(def, num, bool, lang)
            "cockcroft_gault" -> evalCockcroftGault(def, num, bool, lang)
            "mdrd_egfr" -> evalMdrd(def, num, bool, lang)
            "fena" -> evalFena(def, num, lang)
            "free_water_deficit" -> evalFreeWaterDeficit(def, num, lang)
            "homa_ir" -> evalHomaIr(def, num, lang)
            "corrected_calcium" -> evalCorrectedCa(def, num, lang)
            "corrected_sodium" -> evalCorrectedNa(def, num, lang)
            "hba1c_eag" -> evalHba1cEag(def, num, lang)
            "anc_calc" -> evalAnc(def, num, lang)
            "parkland_burn" -> evalParkland(def, num, lang)
            "bsa_mosteller" -> evalBsaMosteller(def, num, lang)
            "calvert_carboplatin" -> evalCalvert(def, num, lang)
            "das28_esr" -> evalDas28(def, num, lang)
            "pasi_calc" -> evalPasi(def, num, lang)
            "ots_calc" -> evalOts(def, num, bool, lang)
            "anion_gap" -> evalAnionGap(def, num, lang)
            "serum_osmolality" -> evalOsmolality(def, num, lang)
            "winters_formula" -> evalWinters(def, num, lang)
            "bmi_calc" -> evalBmi(def, num, lang)
            "ppi_scaled10" -> evalPpiScaled10(def, num, bool, lang)
            "devine_ibw" -> evalDevineIbw(def, num, bool, lang)
            else -> error("Unknown formulaKey: $formulaKey")
        }
    }

    fun formatSeverity(
        value: Double,
        thresholds: SeverityThresholds?,
        lang: AppLanguage
    ): Pair<String, SeverityLevel> {
        val level = if (thresholds == null) {
            SeverityLevel.LOW
        } else if (thresholds.higherIsWorse) {
            when {
                value >= thresholds.severeBoundary -> SeverityLevel.SEVERE
                value >= thresholds.moderateBoundary -> SeverityLevel.MODERATE
                else -> SeverityLevel.LOW
            }
        } else {
            when {
                value <= thresholds.severeBoundary -> SeverityLevel.SEVERE
                value <= thresholds.moderateBoundary -> SeverityLevel.MODERATE
                else -> SeverityLevel.LOW
            }
        }
        val label = when (level) {
            SeverityLevel.SEVERE -> when (lang) {
                AppLanguage.FR -> "Élevé / Critique"
                AppLanguage.EN -> "High / Critical"
                AppLanguage.ES -> "Alto / Crítico"
            }
            SeverityLevel.MODERATE -> when (lang) {
                AppLanguage.FR -> "Modéré / Intermédiaire"
                AppLanguage.EN -> "Moderate / Intermediate"
                AppLanguage.ES -> "Moderado / Intermedio"
            }
            SeverityLevel.LOW -> when (lang) {
                AppLanguage.FR -> "Faible / Normal"
                AppLanguage.EN -> "Low / Normal"
                AppLanguage.ES -> "Bajo / Normal"
            }
        }
        return label to level
    }

    private fun buildResult(
        def: ClinicalScoreDefinition,
        value: Double,
        display: String,
        lang: AppLanguage,
        breakdown: List<String>,
        serialized: Map<String, Any>,
        cardiacRisk: Double? = null,
        cardiacGuidance: String? = null,
        overrideLevel: Pair<String, SeverityLevel>? = null
    ): CalculatedScoreResult {
        val (sevLabel, sevLevel) = overrideLevel ?: formatSeverity(value, def.severityThresholds, lang)
        return CalculatedScoreResult(
            scoreId = def.scoreId,
            scoreName = def.name,
            specialty = def.specialtyNameEn,
            numericValue = value,
            displayValue = display,
            severityLabel = sevLabel,
            severityLevel = sevLevel,
            cardiacRiskPercent = cardiacRisk,
            cardiacGuidance = cardiacGuidance,
            breakdownLines = breakdown,
            serializedInputs = serialized
        )
    }

    private fun evalAspects(def: ClinicalScoreDefinition, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        var deductions = 0
        val serialized = mutableMapOf<String, Any>()
        val breakdown = mutableListOf("Base ASPECTS = 10")
        def.inputs.forEach { spec ->
            val checked = bool[spec.key] == true
            serialized[spec.key] = checked
            if (checked) {
                deductions += 1
                breakdown.add("${spec.labelEn}: -1")
            }
        }
        val score = (10 - deductions).coerceIn(0, 10)
        breakdown.add("Total ASPECTS = 10 - $deductions = $score / 10")
        return buildResult(def, score.toDouble(), "$score / 10", lang, breakdown, serialized)
    }

    private fun evalGraceAcs(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val age = (num["age"] ?: 62.0).roundToInt()
        val hr = (num["hr"] ?: 82.0).roundToInt()
        val sbp = (num["sbp"] ?: 125.0).roundToInt()
        val creat = num["creat"] ?: 1.0
        val killip = (num["killip"] ?: 0.0).roundToInt()
        val arrest = bool["arrest"] == true
        val stDev = bool["st_dev"] == true
        val trop = bool["troponin"] == true

        val agePts = when {
            age < 30 -> 0; age < 40 -> 8; age < 50 -> 25; age < 60 -> 41; age < 70 -> 58; age < 80 -> 75; age < 90 -> 91; else -> 100
        }
        val hrPts = when {
            hr < 50 -> 0; hr < 70 -> 3; hr < 90 -> 9; hr < 110 -> 15; hr < 150 -> 24; hr < 200 -> 38; else -> 46
        }
        val sbpPts = when {
            sbp < 80 -> 58; sbp < 100 -> 53; sbp < 120 -> 43; sbp < 140 -> 34; sbp < 160 -> 24; sbp < 200 -> 10; else -> 0
        }
        val crPts = when {
            creat < 0.4 -> 1; creat < 0.8 -> 4; creat < 1.2 -> 7; creat < 1.6 -> 10; creat < 2.0 -> 13; creat < 4.0 -> 21; else -> 28
        }
        val total = agePts + hrPts + sbpPts + crPts + killip +
            (if (arrest) 39 else 0) + (if (stDev) 28 else 0) + (if (trop) 14 else 0)
        val mortPct = ((1.0 / (1.0 + exp(-(total - 170.0) / 32.0))) * 45.0).coerceIn(0.4, 45.0)
        val roundedMort = (mortPct * 10.0).roundToInt() / 10.0
        return buildResult(
            def, total.toDouble(), "$total pts", lang,
            listOf(
                "Âge ($age ans) = +$agePts", "FC ($hr bpm) = +$hrPts", "PAS ($sbp mmHg) = +$sbpPts",
                "Créatinine (${"%.1f".format(creat)}) = +$crPts", "Killip = +$killip",
                "Arrêt/ST/Troponine = +${(if (arrest) 39 else 0) + (if (stDev) 28 else 0) + (if (trop) 14 else 0)}",
                "Total GRACE = $total pts (~$roundedMort% mortalité 6 mois)"
            ),
            mapOf("age" to age, "hr" to hr, "sbp" to sbp, "creat" to creat, "killip" to killip, "arrest" to arrest, "st_dev" to stDev, "troponin" to trop),
            cardiacRisk = roundedMort
        )
    }

    private fun evalQtcBazett(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val qt = num["qt_ms"] ?: 400.0
        val hr = (num["hr"] ?: 75.0).coerceAtLeast(25.0)
        val rrSec = 60.0 / hr
        val qtc = (qt / sqrt(rrSec)).roundToInt().toDouble()
        return buildResult(
            def, qtc, "${qtc.roundToInt()} ms", lang,
            listOf("QT = ${qt.roundToInt()} ms", "FC = ${hr.roundToInt()} bpm → RR = ${"%.3f".format(rrSec)} s", "QTc = QT / √RR = ${qtc.roundToInt()} ms"),
            mapOf("qt_ms" to qt, "hr" to hr)
        )
    }

    private fun evalMapBp(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val sbp = num["sbp"] ?: 120.0
        val dbp = num["dbp"] ?: 75.0
        val pam = ((sbp + 2.0 * dbp) / 3.0).roundToInt().toDouble()
        return buildResult(
            def, pam, "${pam.roundToInt()} mmHg", lang,
            listOf("PAS = ${sbp.roundToInt()} mmHg", "PAD = ${dbp.roundToInt()} mmHg", "PAM = (PAS + 2×PAD)/3 = ${pam.roundToInt()} mmHg"),
            mapOf("sbp" to sbp, "dbp" to dbp)
        )
    }

    private fun evalFramingham(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val age = num["age"] ?: 54.0
        val sbp = num["sbp"] ?: 130.0
        val chol = num["chol"] ?: 205.0
        val hdl = (num["hdl"] ?: 48.0).coerceAtLeast(20.0)
        val smoker = bool["smoker"] == true
        val dm = bool["diabetes"] == true
        val treated = bool["treated_htn"] == true

        val lp = 3.06117 * ln(age) + 1.1237 * ln(chol) - 0.93263 * ln(hdl) +
            (if (treated) 1.99881 else 1.93303) * ln(sbp) +
            (if (smoker) 0.65451 else 0.0) + (if (dm) 0.57367 else 0.0) - 23.9802
        val riskPct = ((1.0 - 0.88936.pow(exp(lp))) * 100.0).coerceIn(1.0, 50.0)
        val rounded = (riskPct * 10.0).roundToInt() / 10.0
        return buildResult(
            def, rounded, "$rounded%", lang,
            listOf("Âge=${age.roundToInt()}, PAS=${sbp.roundToInt()}, Chol=${chol.roundToInt()}, HDL=${hdl.roundToInt()}", "Risque cardiovasculaire à 10 ans = $rounded%"),
            mapOf("age" to age, "sbp" to sbp, "chol" to chol, "hdl" to hdl, "smoker" to smoker, "diabetes" to dm, "treated_htn" to treated),
            cardiacRisk = rounded
        )
    }

    private fun evalRass(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val v = (num["rass_level"] ?: 0.0).roundToInt()
        val abs = kotlin.math.abs(v)
        val (lbl, lvl) = when {
            abs >= 3 -> (if (lang == AppLanguage.FR) "Sédation profonde / Agitation sévère ($v)" else "Deep sedation / Severe agitation ($v)") to SeverityLevel.SEVERE
            abs >= 1 -> (if (lang == AppLanguage.FR) "Sédation légère / Agitation modérée ($v)" else "Mild sedation / Agitation ($v)") to SeverityLevel.MODERATE
            else -> (if (lang == AppLanguage.FR) "Alerte et calme (Cible = 0)" else "Alert and calm (Target = 0)") to SeverityLevel.LOW
        }
        val sign = if (v > 0) "+$v" else "$v"
        return buildResult(def, v.toDouble(), "RASS $sign", lang, listOf("Score RASS observé = $sign"), mapOf("rass_level" to v), overrideLevel = lbl to lvl)
    }

    private fun evalMurray(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val sum = listOf("cxr", "pf", "peep", "comp").sumOf { (num[it] ?: 0.0).roundToInt() }
        val avg = (sum / 4.0 * 100.0).roundToInt() / 100.0
        return buildResult(
            def, avg, "${"%.2f".format(avg)} / 4.0", lang,
            listOf("Somme 4 composantes = $sum", "Score Murray = $sum / 4 = ${"%.2f".format(avg)}"),
            mapOf("cxr" to (num["cxr"] ?: 0.0), "pf" to (num["pf"] ?: 0.0), "peep" to (num["peep"] ?: 0.0), "comp" to (num["comp"] ?: 0.0))
        )
    }

    private fun evalPfRatio(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val pao2 = num["pao2"] ?: 90.0
        val fio2Pct = (num["fio2"] ?: 40.0).coerceIn(21.0, 100.0)
        val ratio = (pao2 / (fio2Pct / 100.0)).roundToInt().toDouble()
        return buildResult(
            def, ratio, "${ratio.roundToInt()} mmHg", lang,
            listOf("PaO₂ = ${pao2.roundToInt()} mmHg", "FiO₂ = ${"%.2f".format(fio2Pct / 100.0)}", "PaO₂/FiO₂ = ${ratio.roundToInt()} mmHg"),
            mapOf("pao2" to pao2, "fio2" to fio2Pct)
        )
    }

    private fun evalShockIndex(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val hr = num["hr"] ?: 95.0
        val sbp = (num["sbp"] ?: 110.0).coerceAtLeast(30.0)
        val si = ((hr / sbp) * 100.0).roundToInt() / 100.0
        return buildResult(
            def, si, "${"%.2f".format(si)}", lang,
            listOf("FC = ${hr.roundToInt()} bpm", "PAS = ${sbp.roundToInt()} mmHg", "Index de choc = ${"%.2f".format(si)}"),
            mapOf("hr" to hr, "sbp" to sbp)
        )
    }

    private fun evalWellsPe(def: ClinicalScoreDefinition, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val weights = mapOf("dvt_signs" to 3.0, "pe_likely" to 3.0, "hr100" to 1.5, "immob" to 1.5, "prev_vte" to 1.5, "hemoptysis" to 1.0, "cancer" to 1.0)
        var total = 0.0
        val breakdown = mutableListOf<String>()
        val ser = mutableMapOf<String, Any>()
        weights.forEach { (k, w) ->
            val on = bool[k] == true
            ser[k] = on
            if (on) total += w
            breakdown.add("$k: ${if (on) "+$w" else "0"}")
        }
        return buildResult(def, total, "$total / 12.5", lang, breakdown, ser)
    }

    private fun evalPsiPort(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val age = (num["age"] ?: 58.0).roundToInt()
        val female = bool["female"] == true
        var pts = age - (if (female) 10 else 0)
        val breakdown = mutableListOf("Âge = +$age", "Sexe féminin = ${if (female) "-10" else "0"}")
        val ser = mutableMapOf<String, Any>("age" to age, "female" to female)
        def.inputs.filterIsInstance<ScoreInputSpec.Toggle>().filter { it.key != "female" }.forEach { t ->
            val on = bool[t.key] == true
            ser[t.key] = on
            if (on) { pts += t.pointsWhenTrue; breakdown.add("${t.labelEn}: +${t.pointsWhenTrue}") }
        }
        return buildResult(def, pts.toDouble(), "$pts pts", lang, breakdown, ser)
    }

    private fun evalMeldNa(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val bili = (num["bilirubin"] ?: 1.8).coerceAtLeast(1.0)
        val inr = (num["inr"] ?: 1.3).coerceAtLeast(1.0)
        val creat = (num["creatinine"] ?: 1.1).coerceIn(1.0, 4.0)
        val na = (num["sodium"] ?: 134.0).coerceIn(125.0, 137.0)
        val meld = (3.78 * ln(bili) + 11.2 * ln(inr) + 9.57 * ln(creat) + 6.43).roundToInt().coerceIn(6, 40)
        val meldNa = if (meld > 11) {
            (meld + 1.32 * (137.0 - na) - 0.033 * meld * (137.0 - na)).roundToInt().coerceIn(6, 40)
        } else meld
        return buildResult(
            def, meldNa.toDouble(), "$meldNa / 40", lang,
            listOf("MELD base = $meld", "Natrémie bornée [125..137] = ${na.roundToInt()} mEq/L", "MELD-Na = $meldNa"),
            mapOf("bilirubin" to bili, "inr" to inr, "creatinine" to creat, "sodium" to (num["sodium"] ?: 134.0))
        )
    }

    private fun evalFib4(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val age = num["age"] ?: 52.0
        val ast = num["ast"] ?: 48.0
        val alt = (num["alt"] ?: 42.0).coerceAtLeast(1.0)
        val plt = (num["platelets"] ?: 180.0).coerceAtLeast(5.0)
        val fib4 = ((age * ast) / (plt * sqrt(alt)) * 100.0).roundToInt() / 100.0
        return buildResult(
            def, fib4, "${"%.2f".format(fib4)}", lang,
            listOf("FIB-4 = (${age.roundToInt()} × ${ast.roundToInt()}) / (${plt.roundToInt()} × √${alt.roundToInt()}) = ${"%.2f".format(fib4)}"),
            mapOf("age" to age, "ast" to ast, "alt" to alt, "platelets" to plt)
        )
    }

    private fun evalMaddrey(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val ptP = num["pt_patient"] ?: 18.0
        val ptC = num["pt_control"] ?: 12.0
        val bili = num["bilirubin"] ?: 8.0
        val df = ((4.6 * (ptP - ptC) + bili) * 10.0).roundToInt() / 10.0
        return buildResult(
            def, df, "${"%.1f".format(df)}", lang,
            listOf("DF = 4.6 × (${"%.1f".format(ptP)} - ${"%.1f".format(ptC)}) + ${"%.1f".format(bili)} = ${"%.1f".format(df)}"),
            mapOf("pt_patient" to ptP, "pt_control" to ptC, "bilirubin" to bili)
        )
    }

    private fun evalApri(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val ast = num["ast"] ?: 55.0
        val uln = (num["ast_uln"] ?: 40.0).coerceAtLeast(10.0)
        val plt = (num["platelets"] ?: 150.0).coerceAtLeast(5.0)
        val apri = (((ast / uln) / plt) * 100.0 * 100.0).roundToInt() / 100.0
        return buildResult(
            def, apri, "${"%.2f".format(apri)}", lang,
            listOf("APRI = ((${ast.roundToInt()} / ${uln.roundToInt()}) / ${plt.roundToInt()}) × 100 = ${"%.2f".format(apri)}"),
            mapOf("ast" to ast, "ast_uln" to uln, "platelets" to plt)
        )
    }

    private fun evalCkdEpi2021(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val age = num["age"] ?: 55.0
        val cr = (num["creat"] ?: 1.2).coerceAtLeast(0.2)
        val female = bool["female"] == true
        val kappa = if (female) 0.7 else 0.9
        val alpha = if (female) -0.241 else -0.302
        val sexFactor = if (female) 1.012 else 1.0
        val egfr = (142.0 * min(cr / kappa, 1.0).pow(alpha) * max(cr / kappa, 1.0).pow(-1.200) * 0.9938.pow(age) * sexFactor).roundToInt().toDouble()
        return buildResult(
            def, egfr, "${egfr.roundToInt()} mL/min/1.73m²", lang,
            listOf("CKD-EPI 2021 (${if (female) "F" else "H"}, ${age.roundToInt()} ans, Cr=${"%.1f".format(cr)}) = ${egfr.roundToInt()} mL/min/1.73m²"),
            mapOf("age" to age, "creat" to cr, "female" to female)
        )
    }

    private fun evalCockcroftGault(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val age = num["age"] ?: 62.0
        val wt = num["weight"] ?: 72.0
        val cr = (num["creat"] ?: 1.3).coerceAtLeast(0.2)
        val female = bool["female"] == true
        val raw = ((140.0 - age) * wt) / (72.0 * cr) * (if (female) 0.85 else 1.0)
        val cl = raw.roundToInt().toDouble()
        return buildResult(
            def, cl, "${cl.roundToInt()} mL/min", lang,
            listOf("CrCl = ((140-${age.roundToInt()})×${wt.roundToInt()}) / (72×${"%.1f".format(cr)})${if (female) " ×0.85" else ""} = ${cl.roundToInt()} mL/min"),
            mapOf("age" to age, "weight" to wt, "creat" to cr, "female" to female)
        )
    }

    private fun evalMdrd(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val age = num["age"] ?: 60.0
        val cr = (num["creat"] ?: 1.4).coerceAtLeast(0.2)
        val female = bool["female"] == true
        val egfr = (175.0 * cr.pow(-1.154) * age.pow(-0.203) * (if (female) 0.742 else 1.0)).roundToInt().toDouble()
        return buildResult(
            def, egfr, "${egfr.roundToInt()} mL/min/1.73m²", lang,
            listOf("MDRD = 175 × Cr^-1.154 × Âge^-0.203 = ${egfr.roundToInt()} mL/min/1.73m²"),
            mapOf("age" to age, "creat" to cr, "female" to female)
        )
    }

    private fun evalFena(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val una = num["una"] ?: 35.0
        val pcr = num["pcr"] ?: 2.2
        val pna = (num["pna"] ?: 138.0).coerceAtLeast(80.0)
        val ucr = (num["ucr"] ?: 80.0).coerceAtLeast(5.0)
        val fena = (((una * pcr) / (pna * ucr)) * 100.0 * 100.0).roundToInt() / 100.0
        return buildResult(
            def, fena, "${"%.2f".format(fena)}%", lang,
            listOf("FENa = (UNa×PCr) / (PNa×UCr) × 100 = ${"%.2f".format(fena)}%"),
            mapOf("una" to una, "pcr" to pcr, "pna" to pna, "ucr" to ucr)
        )
    }

    private fun evalFreeWaterDeficit(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val wt = num["weight"] ?: 70.0
        val na = num["na"] ?: 154.0
        val factor = ((num["tbw_type"] ?: 60.0) / 100.0)
        val deficit = (((factor * wt) * ((na / 140.0) - 1.0)).coerceAtLeast(0.0) * 10.0).roundToInt() / 10.0
        return buildResult(
            def, deficit, "${"%.1f".format(deficit)} L", lang,
            listOf("TBW = ${"%.2f".format(factor)} × ${wt.roundToInt()} kg", "Déficit = TBW × (Na/140 - 1) = ${"%.1f".format(deficit)} L"),
            mapOf("weight" to wt, "na" to na, "tbw_factor" to factor)
        )
    }

    private fun evalHomaIr(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val glucMg = num["glucose_mg"] ?: 102.0
        val ins = num["insulin"] ?: 12.0
        val homa = (((glucMg * ins) / 405.0) * 100.0).roundToInt() / 100.0
        return buildResult(
            def, homa, "${"%.2f".format(homa)}", lang,
            listOf("HOMA-IR = (${glucMg.roundToInt()} mg/dL × ${ins.roundToInt()} mUI/L) / 405 = ${"%.2f".format(homa)}"),
            mapOf("glucose_mg" to glucMg, "insulin" to ins)
        )
    }

    private fun evalCorrectedCa(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val ca = num["ca"] ?: 8.6
        val alb = num["alb"] ?: 2.8
        val corr = ((ca + 0.8 * (4.0 - alb)) * 100.0).roundToInt() / 100.0
        return buildResult(
            def, corr, "${"%.2f".format(corr)} mg/dL", lang,
            listOf("Ca corrigé = ${"%.1f".format(ca)} + 0.8 × (4.0 - ${"%.1f".format(alb)}) = ${"%.2f".format(corr)} mg/dL"),
            mapOf("ca" to ca, "alb" to alb)
        )
    }

    private fun evalCorrectedNa(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val na = num["na"] ?: 128.0
        val gluc = num["glucose"] ?: 520.0
        val corr = ((na + 0.016 * (gluc - 100.0)) * 10.0).roundToInt() / 10.0
        return buildResult(
            def, corr, "${"%.1f".format(corr)} mEq/L", lang,
            listOf("Na corrigé (Katz) = ${na.roundToInt()} + 0.016 × (${gluc.roundToInt()} - 100) = ${"%.1f".format(corr)} mEq/L"),
            mapOf("na" to na, "glucose" to gluc)
        )
    }

    private fun evalHba1cEag(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val hba1c = num["hba1c"] ?: 7.2
        val eag = (28.7 * hba1c - 46.7).roundToInt().toDouble()
        return buildResult(
            def, eag, "${eag.roundToInt()} mg/dL", lang,
            listOf("eAG = 28.7 × ${"%.1f".format(hba1c)}% - 46.7 = ${eag.roundToInt()} mg/dL (${"%.2f".format(eag / 18.0)} mmol/L)"),
            mapOf("hba1c" to hba1c)
        )
    }

    private fun evalAnc(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val wbc = num["wbc"] ?: 2.5
        val pct = num["neutro_pct"] ?: 35.0
        val anc = (wbc * 1000.0 * (pct / 100.0)).roundToInt().toDouble()
        return buildResult(
            def, anc, "${anc.roundToInt()} /mm³", lang,
            listOf("PNN = ${"%.1f".format(wbc)}×10³ × ${pct.roundToInt()}% = ${anc.roundToInt()} /mm³"),
            mapOf("wbc" to wbc, "neutro_pct" to pct)
        )
    }

    private fun evalParkland(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val wt = num["weight"] ?: 75.0
        val tbsa = num["tbsa"] ?: 25.0
        val total = (4.0 * wt * tbsa).roundToInt().toDouble()
        val first8h = (total / 2.0).roundToInt()
        return buildResult(
            def, total, "${total.roundToInt()} mL/24h", lang,
            listOf("Volume 24h = 4 × ${wt.roundToInt()} kg × ${tbsa.roundToInt()}% = ${total.roundToInt()} mL", "50% sur les 8 premières heures = $first8h mL"),
            mapOf("weight" to wt, "tbsa" to tbsa)
        )
    }

    private fun evalBsaMosteller(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val h = num["height"] ?: 172.0
        val w = num["weight"] ?: 70.0
        val bsa = (sqrt((h * w) / 3600.0) * 100.0).roundToInt() / 100.0
        return buildResult(
            def, bsa, "${"%.2f".format(bsa)} m²", lang,
            listOf("BSA = √((${h.roundToInt()} × ${w.roundToInt()}) / 3600) = ${"%.2f".format(bsa)} m²"),
            mapOf("height" to h, "weight" to w)
        )
    }

    private fun evalCalvert(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val auc = num["auc"] ?: 5.0
        val gfr = (num["gfr"] ?: 75.0).coerceAtMost(125.0)
        val dose = (auc * (gfr + 25.0)).roundToInt().toDouble()
        return buildResult(
            def, dose, "${dose.roundToInt()} mg", lang,
            listOf("Dose Carboplatine = ${"%.1f".format(auc)} × (${gfr.roundToInt()} + 25) = ${dose.roundToInt()} mg"),
            mapOf("auc" to auc, "gfr" to gfr)
        )
    }

    private fun evalDas28(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val tjc = num["tjc"] ?: 4.0
        val sjc = num["sjc"] ?: 3.0
        val esr = (num["esr"] ?: 28.0).coerceAtLeast(1.0)
        val gh = num["gh"] ?: 40.0
        val das = ((0.56 * sqrt(tjc) + 0.28 * sqrt(sjc) + 0.70 * ln(esr) + 0.014 * gh) * 100.0).roundToInt() / 100.0
        return buildResult(
            def, das, "${"%.2f".format(das)}", lang,
            listOf("DAS28-VS = 0.56√${tjc.roundToInt()} + 0.28√${sjc.roundToInt()} + 0.70ln(${esr.roundToInt()}) + 0.014×${gh.roundToInt()} = ${"%.2f".format(das)}"),
            mapOf("tjc" to tjc, "sjc" to sjc, "esr" to esr, "gh" to gh)
        )
    }

    private fun evalPasi(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val head = num["head"] ?: 2.0
        val arms = num["arms"] ?: 3.0
        val trunk = num["trunk"] ?: 4.0
        val legs = num["legs"] ?: 4.0
        val pasi = (((0.1 * head * 6.0 / 12.0) * 12.0 * 0.1 + 0.2 * arms + 0.3 * trunk + 0.4 * legs) * 6.0 / 12.0 * 10.0).roundToInt() / 10.0
        val weighted = ((0.6 * head + 1.2 * arms + 1.8 * trunk + 2.4 * legs) * 10.0).roundToInt() / 10.0
        val clamped = weighted.coerceIn(0.0, 72.0)
        return buildResult(
            def, clamped, "${"%.1f".format(clamped)} / 72", lang,
            listOf("Pondération PASI (10% tête, 20% MS, 30% tronc, 40% MI) = ${"%.1f".format(clamped)}"),
            mapOf("head" to head, "arms" to arms, "trunk" to trunk, "legs" to legs, "raw" to pasi)
        )
    }

    private fun evalOts(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        var score = (num["initial_va"] ?: 90.0).roundToInt()
        val breakdown = mutableListOf("Acuité initiale = $score")
        val ser = mutableMapOf<String, Any>("initial_va" to score)
        def.inputs.filterIsInstance<ScoreInputSpec.Toggle>().forEach { t ->
            val on = bool[t.key] == true
            ser[t.key] = on
            if (on) {
                score -= t.pointsWhenTrue
                breakdown.add("${t.labelEn}: -${t.pointsWhenTrue}")
            }
        }
        val clamped = score.coerceIn(0, 100)
        breakdown.add("Total OTS = $clamped / 100")
        return buildResult(def, clamped.toDouble(), "$clamped / 100", lang, breakdown, ser)
    }

    private fun evalAnionGap(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val na = num["na"] ?: 138.0
        val cl = num["cl"] ?: 102.0
        val hco3 = num["hco3"] ?: 16.0
        val alb = num["alb"] ?: 3.5
        val rawGap = na - cl - hco3
        val corrGap = ((rawGap + 2.5 * (4.0 - alb)) * 10.0).roundToInt() / 10.0
        return buildResult(
            def, corrGap, "${"%.1f".format(corrGap)} mEq/L", lang,
            listOf("TA brut = ${na.roundToInt()} - ${cl.roundToInt()} - ${hco3.roundToInt()} = ${"%.1f".format(rawGap)}", "TA corrigé (Alb=${"%.1f".format(alb)}) = ${"%.1f".format(corrGap)} mEq/L"),
            mapOf("na" to na, "cl" to cl, "hco3" to hco3, "alb" to alb)
        )
    }

    private fun evalOsmolality(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val na = num["na"] ?: 140.0
        val gluc = num["glucose"] ?: 110.0
        val bun = num["bun"] ?: 18.0
        val osm = (2.0 * na + gluc / 18.0 + bun / 2.8).roundToInt().toDouble()
        return buildResult(
            def, osm, "${osm.roundToInt()} mOsm/kg", lang,
            listOf("Osm = 2×${na.roundToInt()} + ${gluc.roundToInt()}/18 + ${bun.roundToInt()}/2.8 = ${osm.roundToInt()} mOsm/kg"),
            mapOf("na" to na, "glucose" to gluc, "bun" to bun)
        )
    }

    private fun evalWinters(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val hco3 = num["hco3"] ?: 14.0
        val paco2 = ((1.5 * hco3 + 8.0) * 10.0).roundToInt() / 10.0
        return buildResult(
            def, paco2, "${"%.1f".format(paco2)} ± 2 mmHg", lang,
            listOf("PaCO₂ attendue = 1.5 × ${hco3.roundToInt()} + 8 = ${"%.1f".format(paco2)} ± 2 mmHg"),
            mapOf("hco3" to hco3)
        )
    }

    private fun evalBmi(def: ClinicalScoreDefinition, num: Map<String, Double>, lang: AppLanguage): CalculatedScoreResult {
        val wt = num["weight"] ?: 72.0
        val htM = ((num["height"] ?: 175.0) / 100.0).coerceAtLeast(0.5)
        val bmi = ((wt / (htM * htM)) * 10.0).roundToInt() / 10.0
        return buildResult(
            def, bmi, "${"%.1f".format(bmi)} kg/m²", lang,
            listOf("IMC = ${wt.roundToInt()} / (${"%.2f".format(htM)})² = ${"%.1f".format(bmi)} kg/m²"),
            mapOf("weight" to wt, "height" to htM * 100.0)
        )
    }

    private fun evalPpiScaled10(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        var scaled = (num["pps"] ?: 0.0).roundToInt() + (num["intake"] ?: 0.0).roundToInt()
        val ser = mutableMapOf<String, Any>("pps" to (num["pps"] ?: 0.0), "intake" to (num["intake"] ?: 0.0))
        def.inputs.filterIsInstance<ScoreInputSpec.Toggle>().forEach { t ->
            val on = bool[t.key] == true
            ser[t.key] = on
            if (on) scaled += t.pointsWhenTrue
        }
        val ppi = scaled / 10.0
        return buildResult(def, ppi, "${"%.1f".format(ppi)} / 15", lang, listOf("Score PPI = ${"%.1f".format(ppi)} / 15"), ser)
    }

    private fun evalDevineIbw(def: ClinicalScoreDefinition, num: Map<String, Double>, bool: Map<String, Boolean>, lang: AppLanguage): CalculatedScoreResult {
        val htCm = num["height"] ?: 172.0
        val female = bool["female"] == true
        val inchesOver60 = (htCm / 2.54) - 60.0
        val ibw = (((if (female) 45.5 else 50.0) + 2.3 * inchesOver60).coerceAtLeast(30.0) * 10.0).roundToInt() / 10.0
        return buildResult(
            def, ibw, "${"%.1f".format(ibw)} kg", lang,
            listOf("Taille = ${htCm.roundToInt()} cm", "IBW Devine (${if (female) "Femme" else "Homme"}) = ${"%.1f".format(ibw)} kg"),
            mapOf("height" to htCm, "female" to female)
        )
    }
}
