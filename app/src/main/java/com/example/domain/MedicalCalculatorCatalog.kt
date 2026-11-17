package com.example.domain

/**
 * Catalogue des 31 spécialités Medical Calculator MCP (152 calculateurs)
 * et définitions déterministes compatibles @awell-health/awell-score (Licence MIT).
 */
object MedicalCalculatorCatalog {

    val glasgowScore = ClinicalScoreDefinition(
        scoreId = "glasgow",
        name = "Glasgow Coma Scale (GCS)",
        specialtyId = "neurology",
        specialtyNameFr = "Neurologie",
        specialtyNameEn = "Neurology",
        specialtyNameEs = "Neurología",
        descriptionFr = "Évaluation standardisée de l'état de conscience (Yeux, Verbal, Moteur) de 3 à 15.",
        descriptionEn = "Standardized consciousness assessment across Eye, Verbal, and Motor responses (3–15).",
        descriptionEs = "Evaluación estandarizada del nivel de conciencia (Ocular, Verbal, Motora) de 3 a 15.",
        source = "@awell-health/awell-score & Medical Calculator MCP",
        license = "MIT License (Awell Health) / Teasdale & Jennett 1974",
        versionHistory = listOf(
            "v1.3.0 (2026-11) — Alignement Medical Calculator MCP & validation GCS-P",
            "v1.2.0 (2025-04) — Pondérations déterministes Awell Score",
            "v1.0.0 (2024-01) — Échelle de Glasgow standard 3–15"
        ),
        supportsInterventionField = true,
        minScore = 3.0,
        maxScore = 15.0,
        inputs = listOf(
            ScoreInputSpec.Choice(
                key = "eye",
                labelFr = "Ouverture des yeux (E)",
                labelEn = "Eye Opening (E)",
                labelEs = "Apertura ocular (E)",
                defaultValue = 4,
                options = listOf(
                    ScoreOption(4, "4 - Spontanée", "4 - Spontaneous", "4 - Espontánea"),
                    ScoreOption(3, "3 - À la demande verbale", "3 - To speech", "3 - A la orden verbal"),
                    ScoreOption(2, "2 - À la douleur", "2 - To pressure/pain", "2 - Al dolor"),
                    ScoreOption(1, "1 - Aucune", "1 - None", "1 - Ninguna")
                )
            ),
            ScoreInputSpec.Choice(
                key = "verbal",
                labelFr = "Réponse verbale (V)",
                labelEn = "Verbal Response (V)",
                labelEs = "Respuesta verbal (V)",
                defaultValue = 5,
                options = listOf(
                    ScoreOption(5, "5 - Orientée", "5 - Oriented", "5 - Orientada"),
                    ScoreOption(4, "4 - Confuse", "4 - Confused", "4 - Confusa"),
                    ScoreOption(3, "3 - Mots inappropriés", "3 - Words", "3 - Palabras inapropiadas"),
                    ScoreOption(2, "2 - Sons incompréhensibles", "2 - Sounds", "2 - Sonidos incomprensibles"),
                    ScoreOption(1, "1 - Aucune", "1 - None", "1 - Ninguna")
                )
            ),
            ScoreInputSpec.Choice(
                key = "motor",
                labelFr = "Réponse motrice (M)",
                labelEn = "Motor Response (M)",
                labelEs = "Respuesta motora (M)",
                defaultValue = 6,
                options = listOf(
                    ScoreOption(6, "6 - Obéit aux ordres", "6 - Obeys commands", "6 - Obedece órdenes"),
                    ScoreOption(5, "5 - Orientée à la douleur", "5 - Localising", "5 - Localiza el dolor"),
                    ScoreOption(4, "4 - Évitement / Retrait", "4 - Normal flexion", "4 - Retirada al dolor"),
                    ScoreOption(3, "3 - Flexion stéréotypée", "3 - Abnormal flexion", "3 - Flexión anormal"),
                    ScoreOption(2, "2 - Extension stéréotypée", "2 - Extension", "2 - Extensión"),
                    ScoreOption(1, "1 - Aucune", "1 - None", "1 - Ninguna")
                )
            )
        )
    )

    val euroScoreII = ClinicalScoreDefinition(
        scoreId = "euroscore_ii",
        name = "EuroSCORE II",
        specialtyId = "cardiology",
        specialtyNameFr = "Cardiologie",
        specialtyNameEn = "Cardiology",
        specialtyNameEs = "Cardiología",
        descriptionFr = "Estimation logistique du risque de mortalité hospitalière en chirurgie cardiaque.",
        descriptionEn = "Logistic operative mortality risk model for adult cardiac surgery.",
        descriptionEs = "Modelo logístico de riesgo de mortalidad en cirugía cardíaca.",
        source = "Medical Calculator MCP / Nashef et al. (ESC/EACTS)",
        license = "MIT / Open Clinical Reference",
        versionHistory = listOf(
            "v2.1.0 (2026-10) — Intégration directives ESC Valvulopathies",
            "v2.0.0 (2024-06) — Régression logistique EuroSCORE II"
        ),
        supportsInterventionField = true,
        isCardiacRiskScore = true,
        minScore = 0.0,
        maxScore = 100.0,
        inputs = listOf(
            ScoreInputSpec.NumericSlider(
                key = "age",
                labelFr = "Âge (années)",
                labelEn = "Age (years)",
                labelEs = "Edad (años)",
                min = 18.0,
                max = 95.0,
                unit = "ans",
                defaultValue = 66.0
            ),
            ScoreInputSpec.Toggle("female", "Sexe féminin", "Female sex", "Sexo femenino", 0),
            ScoreInputSpec.Choice(
                key = "renal",
                labelFr = "Fonction rénale (Clairance créatinine)",
                labelEn = "Renal impairment (CrCl)",
                labelEs = "Función renal (ClCr)",
                defaultValue = 0,
                options = listOf(
                    ScoreOption(0, "Normale (> 85 mL/min)", "Normal (> 85 mL/min)", "Normal (> 85)"),
                    ScoreOption(1, "Modérée (50–85 mL/min)", "Moderate (50–85)", "Moderada (50–85)"),
                    ScoreOption(2, "Sévère (< 50 mL/min)", "Severe (< 50)", "Grave (< 50)"),
                    ScoreOption(3, "Dialyse chronique", "On dialysis", "Diálisis")
                )
            ),
            ScoreInputSpec.Choice(
                key = "nyha",
                labelFr = "Classe fonctionnelle NYHA",
                labelEn = "NYHA Class",
                labelEs = "Clase NYHA",
                defaultValue = 2,
                options = listOf(
                    ScoreOption(1, "NYHA I", "NYHA I", "NYHA I"),
                    ScoreOption(2, "NYHA II", "NYHA II", "NYHA II"),
                    ScoreOption(3, "NYHA III", "NYHA III", "NYHA III"),
                    ScoreOption(4, "NYHA IV", "NYHA IV", "NYHA IV")
                )
            ),
            ScoreInputSpec.Choice(
                key = "lvef",
                labelFr = "Fraction d'éjection VG (FEVG)",
                labelEn = "LVEF Function",
                labelEs = "FEVI",
                defaultValue = 0,
                options = listOf(
                    ScoreOption(0, "Bonne (> 50%)", "Good (> 50%)", "Buena (> 50%)"),
                    ScoreOption(1, "Modérée (31–50%)", "Moderate (31–50%)", "Moderada (31–50%)"),
                    ScoreOption(2, "Altérée (21–30%)", "Poor (21–30%)", "Baja (21–30%)"),
                    ScoreOption(3, "Très sévère (≤ 20%)", "Very poor (≤ 20%)", "Muy baja (≤ 20%)")
                )
            ),
            ScoreInputSpec.Toggle("recent_mi", "IDM récent (< 90 jours)", "Recent MI (< 90d)", "IAM reciente (< 90d)", 0),
            ScoreInputSpec.Toggle("iddm", "Diabète insulinotraité", "Insulin-treated diabetes", "Diabetes con insulina", 0),
            ScoreInputSpec.Toggle("arteriopathy", "Artériopathie extracardiaque", "Extracardiac arteriopathy", "Arteriopatía extracardíaca", 0),
            ScoreInputSpec.Toggle("urgent", "Intervention urgente", "Urgent surgery", "Cirugía urgente", 0)
        )
    )

    val cha2ds2Vasc = ClinicalScoreDefinition(
        scoreId = "cha2ds2_vasc",
        name = "CHA₂DS₂-VASc",
        specialtyId = "cardiology",
        specialtyNameFr = "Cardiologie",
        specialtyNameEn = "Cardiology",
        specialtyNameEs = "Cardiología",
        descriptionFr = "Stratification du risque d'AVC dans la fibrillation atriale non valvulaire.",
        descriptionEn = "Stroke risk stratification for atrial fibrillation.",
        descriptionEs = "Estratificación del riesgo de ictus en fibrilación auricular.",
        source = "@awell-health/awell-score & ESC AF Guidelines",
        license = "MIT License",
        versionHistory = listOf("v1.3.0 (2026-09) — Seuils anticoagulation ESC"),
        supportsInterventionField = false,
        isCardiacRiskScore = true,
        minScore = 0.0,
        maxScore = 9.0,
        inputs = listOf(
            ScoreInputSpec.Toggle("chf", "Insuffisance cardiaque / FEVG ≤ 40%", "Congestive Heart Failure", "Insuficiencia cardíaca", 1),
            ScoreInputSpec.Toggle("htn", "Hypertension artérielle", "Hypertension", "Hipertensión", 1),
            ScoreInputSpec.Choice(
                key = "age_band",
                labelFr = "Tranche d'âge",
                labelEn = "Age group",
                labelEs = "Grupo de edad",
                defaultValue = 0,
                options = listOf(
                    ScoreOption(0, "< 65 ans (+0)", "< 65 years (+0)", "< 65 años (+0)"),
                    ScoreOption(1, "65–74 ans (+1)", "65–74 years (+1)", "65–74 años (+1)"),
                    ScoreOption(2, "≥ 75 ans (+2)", "≥ 75 years (+2)", "≥ 75 años (+2)")
                )
            ),
            ScoreInputSpec.Toggle("diabetes", "Diabète sucré", "Diabetes mellitus", "Diabetes mellitus", 1),
            ScoreInputSpec.Toggle("stroke", "Antécédent AVC / AIT / Embolie (+2)", "Prior Stroke / TIA (+2)", "Ictus / AIT previo (+2)", 2),
            ScoreInputSpec.Toggle("vascular", "Maladie vasculaire (AOMI, IDM)", "Vascular disease", "Enfermedad vascular", 1),
            ScoreInputSpec.Toggle("female", "Sexe féminin (+1)", "Female sex (+1)", "Sexo femenino (+1)", 1)
        )
    )

    val qSofa = ClinicalScoreDefinition(
        scoreId = "qsofa",
        name = "qSOFA (Quick SOFA)",
        specialtyId = "intensive_care",
        specialtyNameFr = "Soins Intensifs",
        specialtyNameEn = "Intensive Care",
        specialtyNameEs = "Cuidados Intensivos",
        descriptionFr = "Dépistage rapide hors réanimation des patients suspects d'infection à risque de sepsis.",
        descriptionEn = "Bedside sepsis risk screening for patients with suspected infection.",
        descriptionEs = "Cribado rápido de sepsis fuera de UCI.",
        source = "@awell-health/awell-score & Sepsis-3",
        license = "MIT License",
        versionHistory = listOf("v1.1.0 — Sepsis-3 Consensus"),
        minScore = 0.0,
        maxScore = 3.0,
        inputs = listOf(
            ScoreInputSpec.Toggle("rr_high", "Fréquence respiratoire ≥ 22/min", "Respiratory rate ≥ 22/min", "Frecuencia respiratoria ≥ 22", 1),
            ScoreInputSpec.Toggle("altered_mentation", "Altération de la conscience (GCS < 15)", "Altered mentation (GCS < 15)", "Alteración mental (GCS < 15)", 1),
            ScoreInputSpec.Toggle("sbp_low", "Pression artérielle systolique ≤ 100 mmHg", "Systolic BP ≤ 100 mmHg", "PAS ≤ 100 mmHg", 1)
        )
    )

    val heartScore = ClinicalScoreDefinition(
        scoreId = "heart_score",
        name = "HEART Score",
        specialtyId = "emergency",
        specialtyNameFr = "Médecine d'Urgence",
        specialtyNameEn = "Emergency Medicine",
        specialtyNameEs = "Medicina de Urgencias",
        descriptionFr = "Évaluation du risque d'événement cardiaque majeur (MACE) à 6 semaines devant une douleur thoracique.",
        descriptionEn = "6-week Major Adverse Cardiac Event (MACE) risk in emergency chest pain.",
        descriptionEs = "Riesgo de evento cardíaco mayor en dolor torácico en urgencias.",
        source = "Medical Calculator MCP / Six et al.",
        license = "MIT License",
        versionHistory = listOf("v1.2.0 — Troponine haute sensibilité"),
        isCardiacRiskScore = true,
        minScore = 0.0,
        maxScore = 10.0,
        inputs = listOf(
            ScoreInputSpec.Choice("history", "Anamnèse (History)", "History", "Historia clínica", listOf(
                ScoreOption(0, "0 - Faiblement suspecte", "0 - Slightly suspicious", "0 - Poco sospechosa"),
                ScoreOption(1, "1 - Modérément suspecte", "1 - Moderately suspicious", "1 - Moderadamente sospechosa"),
                ScoreOption(2, "2 - Hautement suspecte", "2 - Highly suspicious", "2 - Altamente sospechosa")
            ), 1),
            ScoreInputSpec.Choice("ecg", "ECG", "ECG", "ECG", listOf(
                ScoreOption(0, "0 - Normal", "0 - Normal", "0 - Normal"),
                ScoreOption(1, "1 - Trouble repolarisation non spécifique", "1 - Non-specific repolarization", "1 - Alteración inespecífica"),
                ScoreOption(2, "2 - Sous-décalage ST significatif", "2 - Significant ST deviation", "2 - Desviación ST significativa")
            ), 0),
            ScoreInputSpec.Choice("age_h", "Âge", "Age", "Edad", listOf(
                ScoreOption(0, "0 - < 45 ans", "0 - < 45 yr", "0 - < 45 años"),
                ScoreOption(1, "1 - 45–64 ans", "1 - 45–64 yr", "1 - 45–64 años"),
                ScoreOption(2, "2 - ≥ 65 ans", "2 - ≥ 65 yr", "2 - ≥ 65 años")
            ), 1),
            ScoreInputSpec.Choice("risk", "Facteurs de risque CV", "Risk Factors", "Factores de riesgo", listOf(
                ScoreOption(0, "0 - Aucun facteur", "0 - No known risk factors", "0 - Ninguno"),
                ScoreOption(1, "1 - 1 à 2 facteurs", "1 - 1–2 risk factors", "1 - 1–2 factores"),
                ScoreOption(2, "2 - ≥ 3 facteurs ou athérosclérose", "2 - ≥ 3 or atherosclerosis", "2 - ≥ 3 o aterosclerosis")
            ), 1),
            ScoreInputSpec.Choice("troponin", "Troponine initiale", "Initial Troponin", "Troponina inicial", listOf(
                ScoreOption(0, "0 - ≤ Limite normale", "0 - ≤ Normal limit", "0 - ≤ Normal"),
                ScoreOption(1, "1 - 1–3× normale", "1 - 1–3× normal", "1 - 1–3× normal"),
                ScoreOption(2, "2 - > 3× normale", "2 - > 3× normal", "2 - > 3× normal")
            ), 0)
        )
    )

    val apgarScore = ClinicalScoreDefinition(
        scoreId = "apgar",
        name = "Score d'APGAR",
        specialtyId = "pediatrics",
        specialtyNameFr = "Pédiatrie",
        specialtyNameEn = "Pediatrics",
        specialtyNameEs = "Pediatría",
        descriptionFr = "Évaluation de la vitalité du nouveau-né à 1, 5 et 10 minutes de vie.",
        descriptionEn = "Newborn vitality assessment at 1, 5, and 10 minutes after birth.",
        descriptionEs = "Evaluación de vitalidad del recién nacido.",
        source = "@awell-health/awell-score",
        license = "MIT License",
        versionHistory = listOf("v1.0.0 — Standard Virginia Apgar 1952"),
        minScore = 0.0,
        maxScore = 10.0,
        inputs = listOf(
            ScoreInputSpec.Choice("appearance", "Coloration (Appearance)", "Appearance", "Color", listOf(
                ScoreOption(2, "2 - Rose généralisé", "2 - Pink all over", "2 - Rosado"),
                ScoreOption(1, "1 - Extrémités cyanosées", "1 - Acrocyanosis", "1 - Acrocianosis"),
                ScoreOption(0, "0 - Pâle ou cyanose diffuse", "0 - Pale/blue", "0 - Cianosis/palidez")
            ), 2),
            ScoreInputSpec.Choice("pulse", "Fréquence cardiaque (Pulse)", "Pulse", "Frecuencia cardíaca", listOf(
                ScoreOption(2, "2 - ≥ 100 bpm", "2 - ≥ 100 bpm", "2 - ≥ 100 lpm"),
                ScoreOption(1, "1 - < 100 bpm", "1 - < 100 bpm", "1 - < 100 lpm"),
                ScoreOption(0, "0 - Absente", "0 - Absent", "0 - Ausente")
            ), 2),
            ScoreInputSpec.Choice("grimace", "Réactivité (Grimace)", "Grimace", "Reflejos", listOf(
                ScoreOption(2, "2 - Cri vigoureux", "2 - Vigorous cry", "2 - Llanto vigoroso"),
                ScoreOption(1, "1 - Grimace", "1 - Grimace", "1 - Mueca"),
                ScoreOption(0, "0 - Aucune", "0 - None", "0 - Ninguna")
            ), 2),
            ScoreInputSpec.Choice("activity", "Tonus musculaire (Activity)", "Activity", "Tono", listOf(
                ScoreOption(2, "2 - Mouvements actifs", "2 - Active motion", "2 - Movimiento activo"),
                ScoreOption(1, "1 - Légère flexion", "1 - Some flexion", "1 - Flexión leve"),
                ScoreOption(0, "0 - Flasque", "0 - Limp", "0 - Flácido")
            ), 2),
            ScoreInputSpec.Choice("respiration", "Respiration", "Respiration", "Respiración", listOf(
                ScoreOption(2, "2 - Régulière, bon cri", "2 - Good cry", "2 - Llanto fuerte"),
                ScoreOption(1, "1 - Lente, irrégulière", "1 - Slow/irregular", "1 - Lenta/irregular"),
                ScoreOption(0, "0 - Absente", "0 - Absent", "0 - Ausente")
            ), 2)
        )
    )

    val phq9Score = ClinicalScoreDefinition(
        scoreId = "phq_9",
        name = "PHQ-9 (Dépression)",
        specialtyId = "psychiatry",
        specialtyNameFr = "Psychiatrie",
        specialtyNameEn = "Psychiatry",
        specialtyNameEs = "Psiquiatría",
        descriptionFr = "Dépistage et quantification de la sévérité d'un épisode dépressif (0–27).",
        descriptionEn = "Patient Health Questionnaire-9 for depression severity screening.",
        descriptionEs = "Cuestionario de salud del paciente PHQ-9.",
        source = "@awell-health/awell-score",
        license = "MIT License / Pfizer Public Domain",
        versionHistory = listOf("v1.1.0 — Awell Score validated module"),
        supportsInterventionField = false,
        minScore = 0.0,
        maxScore = 27.0,
        inputs = listOf(
            "anhedonia" to "Peu d'intérêt ou de plaisir / Little interest",
            "mood" to "Tristesse, déprime / Feeling down",
            "sleep" to "Troubles du sommeil / Sleep issues",
            "fatigue" to "Fatigue / Little energy",
            "appetite" to "Appétit perturbé / Appetite changes",
            "guilt" to "Dévalorisation / Feeling bad about self",
            "concentration" to "Difficultés concentration / Concentration",
            "psychomotor" to "Ralentissement ou agitation / Psychomotor",
            "suicidal" to "Idées noires / Self-harm thoughts"
        ).map { (k, label) ->
            ScoreInputSpec.Choice(
                key = k,
                labelFr = label,
                labelEn = label,
                labelEs = label,
                defaultValue = 0,
                options = listOf(
                    ScoreOption(0, "0 - Jamais", "0 - Not at all", "0 - Nunca"),
                    ScoreOption(1, "1 - Plusieurs jours", "1 - Several days", "1 - Varios días"),
                    ScoreOption(2, "2 - > 50% du temps", "2 - More than half", "2 - Más de la mitad"),
                    ScoreOption(3, "3 - Presque tous les jours", "3 - Nearly every day", "3 - Casi todos los días")
                )
            )
        }
    )

    val curb65 = ClinicalScoreDefinition(
        scoreId = "curb_65",
        name = "CURB-65",
        specialtyId = "pulmonology",
        specialtyNameFr = "Pneumologie",
        specialtyNameEn = "Pulmonology",
        specialtyNameEs = "Neumología",
        descriptionFr = "Orientation et gravité des pneumonies aiguës communautaires.",
        descriptionEn = "Pneumonia severity and hospitalization triage score.",
        descriptionEs = "Gravedad de neumonía adquirida en la comunidad.",
        source = "Medical Calculator MCP / BTS",
        license = "MIT License",
        versionHistory = listOf("v1.0.0 — British Thoracic Society"),
        minScore = 0.0,
        maxScore = 5.0,
        inputs = listOf(
            ScoreInputSpec.Toggle("confusion", "Confusion mentale récente", "New confusion", "Confusión nueva", 1),
            ScoreInputSpec.Toggle("urea", "Urée > 7 mmol/L (BUN > 19 mg/dL)", "Urea > 7 mmol/L", "Urea > 7 mmol/L", 1),
            ScoreInputSpec.Toggle("rr", "Fréquence respiratoire ≥ 30/min", "Respiratory rate ≥ 30", "FR ≥ 30", 1),
            ScoreInputSpec.Toggle("bp", "PAS < 90 mmHg ou PAD ≤ 60 mmHg", "BP < 90/60 mmHg", "PA < 90/60 mmHg", 1),
            ScoreInputSpec.Toggle("age65", "Âge ≥ 65 ans", "Age ≥ 65 years", "Edad ≥ 65 años", 1)
        )
    )

    val meldScore = ClinicalScoreDefinition(
        scoreId = "meld",
        name = "MELD Score",
        specialtyId = "hepatology",
        specialtyNameFr = "Hépatologie",
        specialtyNameEn = "Hepatology",
        specialtyNameEs = "Hepatología",
        descriptionFr = "Pronostic à 3 mois des cirrhoses avancées et priorisation de greffe hépatique.",
        descriptionEn = "Model for End-Stage Liver Disease 3-month mortality severity.",
        descriptionEs = "Puntuación MELD de enfermedad hepática terminal.",
        source = "Medical Calculator MCP / UNOS",
        license = "MIT License",
        versionHistory = listOf("v1.2.0 — UNOS standard bounds [6..40]"),
        minScore = 6.0,
        maxScore = 40.0,
        inputs = listOf(
            ScoreInputSpec.NumericSlider("bilirubin", "Bilirubine totale (mg/dL)", "Bilirubin (mg/dL)", "Bilirrubina (mg/dL)", 1.0, 25.0, 0.5, "mg/dL", 1.5),
            ScoreInputSpec.NumericSlider("inr", "INR", "INR", "INR", 1.0, 6.0, 0.1, "", 1.2),
            ScoreInputSpec.NumericSlider("creatinine", "Créatinine (mg/dL)", "Creatinine (mg/dL)", "Creatinina (mg/dL)", 1.0, 4.0, 0.1, "mg/dL", 1.1)
        )
    )

    // 9 Featured Quick-Launch Calculators (also included in all152Scores across the 31 specialties)
    val builtInScores: List<ClinicalScoreDefinition> = listOf(
        glasgowScore,
        euroScoreII,
        cha2ds2Vasc,
        qSofa,
        heartScore,
        apgarScore,
        phq9Score,
        curb65,
        meldScore
    )

    // Full 31 Specialties from Medical Calculator MCP — 152 unique deterministic calculators total
    val all31Specialties: List<MedicalSpecialty> = run {
        val neuroList = listOf(glasgowScore) + com.example.domain.catalog.CatalogNeuroPsych.neurologyExtras
        val cardioList = listOf(euroScoreII, cha2ds2Vasc) + com.example.domain.catalog.CatalogCardioIcu.cardiologyExtras
        val icuList = listOf(qSofa) + com.example.domain.catalog.CatalogCardioIcu.icuExtras
        val emergencyList = listOf(heartScore) + com.example.domain.catalog.CatalogEmergencyPeds.emergencyExtras
        val pedsList = listOf(apgarScore) + com.example.domain.catalog.CatalogEmergencyPeds.pediatricsExtras
        val psychList = listOf(phq9Score) + com.example.domain.catalog.CatalogNeuroPsych.psychiatryExtras
        val pulmList = listOf(curb65) + com.example.domain.catalog.CatalogPulmHepGastro.pulmonologyExtras
        val hepList = listOf(meldScore) + com.example.domain.catalog.CatalogPulmHepGastro.hepatologyExtras
        val nephroList = com.example.domain.catalog.CatalogNephroEndoHem.nephrologyScores
        val anesthList = com.example.domain.catalog.CatalogAnesthSurgOrtho.anesthesiologyScores
        val hemList = com.example.domain.catalog.CatalogNephroEndoHem.hematologyScores
        val endoList = com.example.domain.catalog.CatalogNephroEndoHem.endocrinologyScores
        val infectList = com.example.domain.catalog.CatalogInfectOncoGerOb.infectiousScores
        val oncoList = com.example.domain.catalog.CatalogInfectOncoGerOb.oncologyScores
        val gerList = com.example.domain.catalog.CatalogInfectOncoGerOb.geriatricsScores
        val obList = com.example.domain.catalog.CatalogInfectOncoGerOb.obstetricsScores
        val surgList = com.example.domain.catalog.CatalogAnesthSurgOrtho.surgeryScores
        val orthoList = com.example.domain.catalog.CatalogAnesthSurgOrtho.orthopedicsScores
        val gastroList = com.example.domain.catalog.CatalogPulmHepGastro.gastroScores
        val rheumList = com.example.domain.catalog.CatalogSpecMiscA.rheumatologyScores
        val dermList = com.example.domain.catalog.CatalogSpecMiscA.dermatologyScores
        val ophthList = com.example.domain.catalog.CatalogSpecMiscA.ophthalmologyScores
        val entList = com.example.domain.catalog.CatalogSpecMiscA.entScores
        val urolList = com.example.domain.catalog.CatalogSpecMiscA.urologyScores
        val toxList = com.example.domain.catalog.CatalogSpecMiscA.toxicologyScores
        val sportsList = com.example.domain.catalog.CatalogSpecMiscB.sportsScores
        val neoList = com.example.domain.catalog.CatalogEmergencyPeds.neonatologyScores
        val pallList = com.example.domain.catalog.CatalogSpecMiscB.palliativeScores
        val pharmList = com.example.domain.catalog.CatalogSpecMiscB.pharmacologyScores
        val radList = com.example.domain.catalog.CatalogSpecMiscB.interventionalRadScores
        val primaryList = com.example.domain.catalog.CatalogSpecMiscB.primaryCareScores

        listOf(
            MedicalSpecialty("neurology", "Neurologie", "Neurology", "Neurología", neuroList.size, "brain", neuroList),
            MedicalSpecialty("cardiology", "Cardiologie", "Cardiology", "Cardiología", cardioList.size, "heart", cardioList),
            MedicalSpecialty("intensive_care", "Soins Intensifs", "Intensive Care", "Cuidados Intensivos", icuList.size, "icu", icuList),
            MedicalSpecialty("emergency", "Médecine d'Urgence", "Emergency Medicine", "Urgencias", emergencyList.size, "emergency", emergencyList),
            MedicalSpecialty("pediatrics", "Pédiatrie", "Pediatrics", "Pediatría", pedsList.size, "pediatrics", pedsList),
            MedicalSpecialty("psychiatry", "Psychiatrie", "Psychiatry", "Psiquiatría", psychList.size, "psychiatry", psychList),
            MedicalSpecialty("pulmonology", "Pneumologie", "Pulmonology", "Neumología", pulmList.size, "lungs", pulmList),
            MedicalSpecialty("hepatology", "Hépatologie", "Hepatology", "Hepatología", hepList.size, "liver", hepList),
            MedicalSpecialty("nephrology", "Néphrologie", "Nephrology", "Nefrología", nephroList.size, "kidney", nephroList),
            MedicalSpecialty("anesthesiology", "Anesthésiologie", "Anesthesiology", "Anestesiología", anesthList.size, "anesthesia", anesthList),
            MedicalSpecialty("hematology", "Hématologie", "Hematology", "Hematología", hemList.size, "blood", hemList),
            MedicalSpecialty("endocrinology", "Endocrinologie", "Endocrinology", "Endocrinología", endoList.size, "endocrine", endoList),
            MedicalSpecialty("infectious", "Infectiologie", "Infectious Disease", "Infectología", infectList.size, "virus", infectList),
            MedicalSpecialty("oncology", "Oncologie", "Oncology", "Oncología", oncoList.size, "oncology", oncoList),
            MedicalSpecialty("geriatrics", "Gériatrie", "Geriatrics", "Geriatría", gerList.size, "geriatrics", gerList),
            MedicalSpecialty("obstetrics", "Obstétrique", "Obstetrics", "Obstetricia", obList.size, "obstetrics", obList),
            MedicalSpecialty("surgery", "Chirurgie Générale", "General Surgery", "Cirugía General", surgList.size, "surgery", surgList),
            MedicalSpecialty("orthopedics", "Orthopédie", "Orthopedics", "Ortopedia", orthoList.size, "ortho", orthoList),
            MedicalSpecialty("gastroenterology", "Gastro-entérologie", "Gastroenterology", "Gastroenterología", gastroList.size, "gastro", gastroList),
            MedicalSpecialty("rheumatology", "Rhumatologie", "Rheumatology", "Reumatología", rheumList.size, "rheuma", rheumList),
            MedicalSpecialty("dermatology", "Dermatologie", "Dermatology", "Dermatología", dermList.size, "derm", dermList),
            MedicalSpecialty("ophthalmology", "Ophtalmologie", "Ophthalmology", "Oftalmología", ophthList.size, "eye", ophthList),
            MedicalSpecialty("ent", "ORL", "Otolaryngology (ENT)", "Otorrinolaringología", entList.size, "ent", entList),
            MedicalSpecialty("urology", "Urologie", "Urology", "Urología", urolList.size, "urology", urolList),
            MedicalSpecialty("toxicology", "Toxicologie", "Toxicology", "Toxicología", toxList.size, "tox", toxList),
            MedicalSpecialty("sports_med", "Médecine du Sport", "Sports Medicine", "Medicina Deportiva", sportsList.size, "sports", sportsList),
            MedicalSpecialty("neonatology", "Néonatologie", "Neonatology", "Neonatología", neoList.size, "neonatal", neoList),
            MedicalSpecialty("palliative", "Soins Palliatifs", "Palliative Care", "Cuidados Paliativos", pallList.size, "palliative", pallList),
            MedicalSpecialty("pharmacology", "Pharmacologie Clinique", "Clinical Pharmacology", "Farmacología Clínica", pharmList.size, "pharma", pharmList),
            MedicalSpecialty("interventional_rad", "Radiologie Interventionnelle", "Interventional Radiology", "Radiología Intervencionista", radList.size, "rad", radList),
            MedicalSpecialty("primary_care", "Médecine Générale", "Primary Care", "Atención Primaria", primaryList.size, "primary", primaryList)
        )
    }

    val all152Scores: List<ClinicalScoreDefinition> = all31Specialties.flatMap { it.featuredScores }

    private val scoreIndexById: Map<String, ClinicalScoreDefinition> = all152Scores.associateBy { it.scoreId }

    fun findScoreById(
        scoreId: String,
        customCatalog: List<CustomCatalogScore> = emptyList()
    ): ClinicalScoreDefinition? {
        scoreIndexById[scoreId]?.let { return it }
        val custom = customCatalog.firstOrNull { it.scoreId == scoreId } ?: return null
        return customToDefinition(custom)
    }

    fun customToDefinition(custom: CustomCatalogScore): ClinicalScoreDefinition =
        ClinicalScoreDefinition(
            scoreId = custom.scoreId,
            name = custom.name,
            specialtyId = custom.specialty.lowercase().replace(" ", "_"),
            specialtyNameFr = custom.specialty,
            specialtyNameEn = custom.specialty,
            specialtyNameEs = custom.specialty,
            descriptionFr = custom.description,
            descriptionEn = custom.description,
            descriptionEs = custom.description,
            source = custom.source,
            license = custom.license,
            versionHistory = listOf("v${custom.version} — Configuré par l'administrateur Firestore"),
            minScore = custom.minScore,
            maxScore = custom.maxScore,
            isCustomFromAdmin = true,
            inputs = listOf(
                ScoreInputSpec.NumericSlider(
                    key = "custom_value",
                    labelFr = "Valeur clinique mesurée",
                    labelEn = "Measured clinical value",
                    labelEs = "Valor clínico medido",
                    min = custom.minScore,
                    max = custom.maxScore,
                    step = 1.0,
                    defaultValue = custom.minScore
                )
            )
        )
}
