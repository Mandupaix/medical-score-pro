package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogNephroEndoHem {

    val nephroMeta = SpecialtyMeta("nephrology", "Néphrologie", "Nephrology", "Nefrología")
    val endoMeta = SpecialtyMeta("endocrinology", "Endocrinologie", "Endocrinology", "Endocrinología")
    val hemMeta = SpecialtyMeta("hematology", "Hématologie", "Hematology", "Hematología")

    // 6 Nephrology calculators
    val nephrologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "ckd_epi",
            name = "DFG estimé (CKD-EPI 2021)",
            specialty = nephroMeta,
            descFr = "Équation CKD-EPI 2021 (sans coefficient ethnique) en mL/min/1.73m².",
            descEn = "2021 Race-free CKD-EPI Creatinine eGFR equation (mL/min/1.73m²).",
            descEs = "Fórmula CKD-EPI 2021 para filtrado glomerular estimado.",
            minScore = 2.0,
            maxScore = 160.0,
            severeBoundary = 30.0,
            moderateBoundary = 60.0,
            higherIsWorse = false,
            unitDisplay = "mL/min/1.73m²",
            formulaKey = "ckd_epi_2021",
            inputs = listOf(
                slider("age", "Âge (ans)", "Age", "Edad", 18.0, 100.0, 55.0, "ans"),
                slider("creat", "Créatininémie (mg/dL)", "Serum creatinine (mg/dL)", "Creatinina (mg/dL)", 0.4, 12.0, 1.2, "mg/dL", 0.1),
                toggle("female", "Sexe féminin", "Female sex", "Sexo femenino", 1)
            )
        ),
        mcpScore(
            scoreId = "cockcroft_gault",
            name = "Clairance Créatinine (Cockcroft-Gault)",
            specialty = nephroMeta,
            descFr = "Estimation de la clairance de créatinine pour l'adaptation posologique (mL/min).",
            descEn = "Cockcroft-Gault Creatinine Clearance for drug dosing adjustments (mL/min).",
            descEs = "Aclaramiento de creatinina por Cockcroft-Gault (mL/min).",
            minScore = 5.0,
            maxScore = 180.0,
            severeBoundary = 30.0,
            moderateBoundary = 60.0,
            higherIsWorse = false,
            unitDisplay = "mL/min",
            formulaKey = "cockcroft_gault",
            inputs = listOf(
                slider("age", "Âge (ans)", "Age", "Edad", 18.0, 100.0, 62.0, "ans"),
                slider("weight", "Poids (kg)", "Weight (kg)", "Peso (kg)", 35.0, 160.0, 72.0, "kg"),
                slider("creat", "Créatininémie (mg/dL)", "Serum creatinine (mg/dL)", "Creatinina (mg/dL)", 0.4, 10.0, 1.3, "mg/dL", 0.1),
                toggle("female", "Sexe féminin (× 0.85)", "Female sex (× 0.85)", "Sexo femenino (× 0.85)", 1)
            )
        ),
        mcpScore(
            scoreId = "mdrd_egfr",
            name = "DFG estimé (MDRD simplifié)",
            specialty = nephroMeta,
            descFr = "Équation MDRD à 4 variables pour le suivi de la maladie rénale chronique.",
            descEn = "4-variable MDRD Study eGFR equation (mL/min/1.73m²).",
            descEs = "Ecuación MDRD de 4 variables para filtrado glomerular.",
            minScore = 2.0,
            maxScore = 160.0,
            severeBoundary = 30.0,
            moderateBoundary = 60.0,
            higherIsWorse = false,
            unitDisplay = "mL/min/1.73m²",
            formulaKey = "mdrd_egfr",
            inputs = listOf(
                slider("age", "Âge (ans)", "Age", "Edad", 18.0, 100.0, 60.0, "ans"),
                slider("creat", "Créatininémie (mg/dL)", "Serum creatinine", "Creatinina", 0.4, 12.0, 1.4, "mg/dL", 0.1),
                toggle("female", "Sexe féminin (× 0.742)", "Female sex", "Sexo femenino", 1)
            )
        ),
        mcpScore(
            scoreId = "fena",
            name = "Fraction Excrétée de Sodium (FENa)",
            specialty = nephroMeta,
            descFr = "Différenciation insuffisance rénale aiguë pré-rénale (< 1%) vs NTA (> 2%).",
            descEn = "Fractional Excretion of Sodium differentiating prerenal (< 1%) vs ATN (> 2%).",
            descEs = "Excreción fraccional de sodio (FENa %).",
            minScore = 0.05,
            maxScore = 30.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            unitDisplay = "%",
            formulaKey = "fena",
            inputs = listOf(
                slider("una", "Sodium urinaire (mEq/L)", "Urine Na (mEq/L)", "Na urinario", 5.0, 200.0, 35.0, "mEq/L"),
                slider("pcr", "Créatinine plasmatique (mg/dL)", "Plasma Cr (mg/dL)", "Cr plasmática", 0.5, 10.0, 2.2, "mg/dL", 0.1),
                slider("pna", "Natrémie (mEq/L)", "Plasma Na (mEq/L)", "Na plasmático", 115.0, 165.0, 138.0, "mEq/L"),
                slider("ucr", "Créatinine urinaire (mg/dL)", "Urine Cr (mg/dL)", "Cr urinaria", 10.0, 250.0, 80.0, "mg/dL")
            )
        ),
        mcpScore(
            scoreId = "free_water_deficit",
            name = "Déficit en Eau Libre (Hypernatrémie)",
            specialty = nephroMeta,
            descFr = "Calcul du volume d'eau libre à restituer dans l'hypernatrémie (Litres).",
            descEn = "Free Water Deficit calculation in hypernatremia (Liters).",
            descEs = "Cálculo del déficit de agua libre en hipernatremia (Litros).",
            minScore = 0.0,
            maxScore = 15.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            unitDisplay = "L",
            formulaKey = "free_water_deficit",
            inputs = listOf(
                slider("weight", "Poids (kg)", "Weight (kg)", "Peso (kg)", 35.0, 150.0, 70.0, "kg"),
                slider("na", "Natrémie actuelle (mEq/L)", "Measured Na (mEq/L)", "Sodio actual", 140.0, 185.0, 154.0, "mEq/L"),
                choice("tbw_type", "Coefficient eau corporelle totale", "Total body water factor", "Factor agua corporal",
                    listOf(opt(60, "Homme adulte (0.60)"), opt(50, "Femme adulte / Homme âgé (0.50)"), opt(45, "Femme âgée (0.45)")), 60)
            )
        ),
        mcpScore(
            scoreId = "kdigo_aki",
            name = "Stadification IRA KDIGO",
            specialty = nephroMeta,
            descFr = "Classification de la gravité de l'insuffisance rénale aiguë (Stades 0 à 3).",
            descEn = "KDIGO Acute Kidney Injury staging criteria (Stages 0–3).",
            descEs = "Estadificación KDIGO de lesión renal aguda (0–3).",
            minScore = 0.0,
            maxScore = 3.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "Stade",
            inputs = listOf(
                choice("stage", "Critère maximal créatinine / diurèse", "Worst Creatinine or Urine Output criterion", "Criterio máximo Cr / diuresis",
                    listOf(
                        opt(0, "Stade 0 - Pas d'IRA"),
                        opt(1, "Stade 1 - Cr ×1.5–1.9 ou +26.5 µmol/L ou diurèse < 0.5 mL/kg/h (6–12h)"),
                        opt(2, "Stade 2 - Cr ×2.0–2.9 ou diurèse < 0.5 mL/kg/h (≥ 12h)"),
                        opt(3, "Stade 3 - Cr ×3.0 / ≥ 354 µmol/L / épuration extra-rénale / anurie")
                    ))
            )
        )
    )

    // 4 Endocrinology calculators
    val endocrinologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "homa_ir",
            name = "Index HOMA-IR (Insulinorésistance)",
            specialty = endoMeta,
            descFr = "Évaluation de l'insulinorésistance : (Glycémie mmol/L × Insulinémie mUI/L) / 22.5.",
            descEn = "Homeostatic Model Assessment of Insulin Resistance.",
            descEs = "Índice HOMA-IR de resistencia a la insulina.",
            minScore = 0.2,
            maxScore = 20.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "HOMA",
            formulaKey = "homa_ir",
            supportsIntervention = false,
            inputs = listOf(
                slider("glucose_mg", "Glycémie à jeun (mg/dL)", "Fasting glucose (mg/dL)", "Glucemia ayunas (mg/dL)", 55.0, 300.0, 102.0, "mg/dL"),
                slider("insulin", "Insulinémie à jeun (mUI/L)", "Fasting insulin (µU/mL)", "Insulina ayunas", 2.0, 60.0, 12.0, "mUI/L")
            )
        ),
        mcpScore(
            scoreId = "corrected_calcium",
            name = "Calcémie Corrigée (Payne)",
            specialty = endoMeta,
            descFr = "Calcium sérique corrigé en fonction de l'albuminémie : Ca + 0.8 × (4.0 - Albumine g/dL).",
            descEn = "Albumin-corrected serum calcium calculation (mg/dL).",
            descEs = "Calcio sérico corregido por albúmina (mg/dL).",
            minScore = 5.0,
            maxScore = 16.0,
            severeBoundary = 11.5,
            moderateBoundary = 10.5,
            unitDisplay = "mg/dL",
            formulaKey = "corrected_calcium",
            inputs = listOf(
                slider("ca", "Calcémie mesurée (mg/dL)", "Measured Ca (mg/dL)", "Calcio medido", 5.0, 15.0, 8.6, "mg/dL", 0.1),
                slider("alb", "Albuminémie (g/dL)", "Serum albumin (g/dL)", "Albúmina (g/dL)", 1.2, 5.5, 2.8, "g/dL", 0.1)
            )
        ),
        mcpScore(
            scoreId = "corrected_sodium",
            name = "Natrémie Corrigée (Hyperglycémie)",
            specialty = endoMeta,
            descFr = "Correction de Katz pour pseudo-hyponatrémie hyperglycémique (mEq/L).",
            descEn = "Katz corrected sodium for hyperglycemia (mEq/L).",
            descEs = "Sodio corregido por hiperglucemia (fórmula de Katz).",
            minScore = 105.0,
            maxScore = 175.0,
            severeBoundary = 150.0,
            moderateBoundary = 146.0,
            unitDisplay = "mEq/L",
            formulaKey = "corrected_sodium",
            inputs = listOf(
                slider("na", "Natrémie mesurée (mEq/L)", "Measured Na (mEq/L)", "Na medido", 105.0, 165.0, 128.0, "mEq/L"),
                slider("glucose", "Glycémie (mg/dL)", "Serum glucose (mg/dL)", "Glucemia (mg/dL)", 100.0, 1200.0, 520.0, "mg/dL", 10.0)
            )
        ),
        mcpScore(
            scoreId = "hba1c_eag",
            name = "Glycémie Moyenne Estimée (eAG / HbA1c)",
            specialty = endoMeta,
            descFr = "Conversion de l'HbA1c (%) en glycémie moyenne estimée (28.7 × HbA1c - 46.7 mg/dL).",
            descEn = "ADAG Estimated Average Glucose from HbA1c (%).",
            descEs = "Glucemia media estimada desde HbA1c (%).",
            minScore = 60.0,
            maxScore = 400.0,
            severeBoundary = 212.0,
            moderateBoundary = 154.0,
            unitDisplay = "mg/dL",
            formulaKey = "hba1c_eag",
            supportsIntervention = false,
            inputs = listOf(
                slider("hba1c", "HbA1c (%)", "HbA1c (%)", "HbA1c (%)", 4.5, 15.0, 7.2, "%", 0.1)
            )
        )
    )

    // 5 Hematology calculators
    val hematologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "isth_dic",
            name = "Score ISTH (CIVD)",
            specialty = hemMeta,
            descFr = "Diagnostic de coagulation intravasculaire disséminée avérée (≥ 5 pts).",
            descEn = "ISTH Criteria for overt Disseminated Intravascular Coagulation (≥ 5 overt DIC).",
            descEs = "Criterios ISTH para coagulación intravascular diseminada (≥ 5).",
            minScore = 0.0,
            maxScore = 8.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("plt", "Plaquettes (G/L)", "Platelets", "Plaquetas", listOf(opt(0, "≥ 100 (+0)"), opt(1, "50–99 (+1)"), opt(2, "< 50 (+2)"))),
                choice("ddimer", "D-Dimères / PDF", "Fibrin-related marker (D-dimer)", "Dímero D", listOf(opt(0, "Normal (+0)"), opt(2, "Élévation modérée (+2)"), opt(3, "Élévation majeure (+3)"))),
                choice("pt", "Allongement TQ (sec)", "Prolonged PT", "Prolongación TP", listOf(opt(0, "< 3s (+0)"), opt(1, "3–6s (+1)"), opt(2, "> 6s (+2)"))),
                choice("fib", "Fibrinogène (g/L)", "Fibrinogen", "Fibrinógeno", listOf(opt(0, "≥ 1.0 g/L (+0)"), opt(1, "< 1.0 g/L (+1)")))
            )
        ),
        mcpScore(
            scoreId = "hit_4t",
            name = "Score des 4T (TIH)",
            specialty = hemMeta,
            descFr = "Probabilité clinique de thrombopénie induite par l'héparine (0–8).",
            descEn = "4Ts Score for Heparin-Induced Thrombocytopenia probability (0–8).",
            descEs = "Puntuación 4T de trombocitopenia inducida por heparina (0–8).",
            minScore = 0.0,
            maxScore = 8.0,
            severeBoundary = 6.0,
            moderateBoundary = 4.0,
            inputs = listOf(
                choice("thromb", "Thrombopénie", "Thrombocytopenia", "Trombocitopenia", listOf(opt(0, "Chute < 30% (+0)"), opt(1, "30–50% ou nadir 10–19 (+1)"), opt(2, "> 50% et nadir ≥ 20 (+2)"))),
                choice("timing", "Délai d'apparition", "Timing of fall", "Tiempo de aparición", listOf(opt(0, "≤ 4j sans exposition récente (+0)"), opt(1, "> 10j ou doute (+1)"), opt(2, "5–10j ou ≤ 1j si expo < 30j (+2)"))),
                choice("thrombosis", "Thrombose / Séquelle", "Thrombosis", "Trombosis", listOf(opt(0, "Aucune (+0)"), opt(1, "Progression / récidive / nécrose (+1)"), opt(2, "Nouvelle thrombose prouvée (+2)"))),
                choice("other", "Autres causes de thrombopénie", "Other causes", "Otras causas", listOf(opt(0, "Évidente (+0)"), opt(1, "Possible (+1)"), opt(2, "Aucune autre évidente (+2)")))
            )
        ),
        mcpScore(
            scoreId = "khorana_vte",
            name = "Score de Khorana (MTEV & Cancer)",
            specialty = hemMeta,
            descFr = "Risque de thrombose veineuse ambulatoire sous chimiothérapie (0–6).",
            descEn = "Khorana Risk Score for VTE in ambulatory cancer patients (0–6).",
            descEs = "Índice de Khorana de riesgo tromboembólico en cáncer (0–6).",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("site", "Site tumoral", "Cancer site", "Localización tumoral", listOf(opt(0, "Autre (+0)"), opt(1, "Poumon, lymphome, gynéco, vessie, testicule (+1)"), opt(2, "Estomac, pancréas (+2)"))),
                toggle("plt350", "Plaquettes pré-chimio ≥ 350 G/L (+1)", "Platelets ≥ 350 (+1)", "Plaquetas ≥ 350 (+1)", 1),
                toggle("hb10", "Hb < 10 g/dL ou EPO (+1)", "Hb < 10 or ESA (+1)", "Hb < 10 (+1)", 1),
                toggle("wbc11", "Leucocytes > 11 G/L (+1)", "WBC > 11 (+1)", "Leucocitos > 11 (+1)", 1),
                toggle("bmi35", "IMC ≥ 35 kg/m² (+1)", "BMI ≥ 35 (+1)", "IMC ≥ 35 (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "padua_vte",
            name = "Score de Padoue (MTEV Médecine)",
            specialty = hemMeta,
            descFr = "Évaluation du risque thromboembolique veineux chez le patient hospitalisé en médecine (≥ 4 = haut risque).",
            descEn = "Padua Prediction Score for VTE risk in hospitalized medical patients.",
            descEs = "Escala de Padua de riesgo de ETV en pacientes médicos.",
            minScore = 0.0,
            maxScore = 20.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("cancer", "Cancer actif (+3)", "Active cancer (+3)", "Cáncer activo (+3)", 3),
                toggle("prev_vte", "Antécédent TVP/EP (+3)", "Previous VTE (+3)", "ETV previa (+3)", 3),
                toggle("reduced_mob", "Mobilité réduite ≥ 3j (+3)", "Reduced mobility (+3)", "Movilidad reducida (+3)", 3),
                toggle("thrombophilia", "Thrombophilie connue (+3)", "Known thrombophilia (+3)", "Trombofilia (+3)", 3),
                toggle("trauma_surg", "Traumatisme/chirurgie < 1 mois (+2)", "Recent trauma/surgery (+2)", "Trauma/cirugía (+2)", 2),
                toggle("age70", "Âge ≥ 70 ans (+1)", "Age ≥ 70 (+1)", "Edad ≥ 70 (+1)", 1),
                toggle("hf_rf", "Insuffisance cardiaque / respiratoire (+1)", "Heart/respiratory failure (+1)", "Insuficiencia cardíaca/resp (+1)", 1),
                toggle("acute_inf", "Infection aiguë / rhumatisme (+1)", "Acute infection/rheumatologic (+1)", "Infección aguda (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "anc_calc",
            name = "Compte Absolu de Neutrophiles (PNN)",
            specialty = hemMeta,
            descFr = "Calcul des polynucléaires neutrophiles (GB × (% Seg + % Band) / 100) pour neutropénie fébrile.",
            descEn = "Absolute Neutrophil Count (ANC) for neutropenia grading (cells/µL).",
            descEs = "Recuento absoluto de neutrófilos (RAN).",
            minScore = 0.0,
            maxScore = 20000.0,
            severeBoundary = 500.0,
            moderateBoundary = 1000.0,
            higherIsWorse = false,
            unitDisplay = "/mm³",
            formulaKey = "anc_calc",
            inputs = listOf(
                slider("wbc", "Leucocytes totaux (×10³/µL)", "WBC (×10³/µL)", "Leucocitos (×10³/µL)", 0.2, 30.0, 2.5, "G/L", 0.1),
                slider("neutro_pct", "% Neutrophiles (Segmentés + Immatures)", "Neutrophils + Bands (%)", "% Neutrófilos", 0.0, 95.0, 35.0, "%")
            )
        )
    )
}
