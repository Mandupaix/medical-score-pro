package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogPulmHepGastro {

    val pulmMeta = SpecialtyMeta("pulmonology", "Pneumologie", "Pulmonology", "Neumología")
    val hepMeta = SpecialtyMeta("hepatology", "Hépatologie", "Hepatology", "Hepatología")
    val gastroMeta = SpecialtyMeta("gastroenterology", "Gastro-entérologie", "Gastroenterology", "Gastroenterología")

    // 5 additional Pulmonology calculators (+ curb_65 = 6 total)
    val pulmonologyExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "psi_port",
            name = "Score PSI / PORT (Pneumonie)",
            specialty = pulmMeta,
            descFr = "Pneumonia Severity Index stratifiant le risque de mortalité et l'hospitalisation.",
            descEn = "Pneumonia Severity Index (PORT) mortality risk classification.",
            descEs = "Índice de gravedad de neumonía PSI/PORT.",
            minScore = 10.0,
            maxScore = 250.0,
            severeBoundary = 131.0,
            moderateBoundary = 71.0,
            formulaKey = "psi_port",
            inputs = listOf(
                slider("age", "Âge (ans)", "Age", "Edad", 18.0, 100.0, 58.0, "ans"),
                toggle("female", "Sexe féminin (-10)", "Female sex (-10)", "Sexo femenino (-10)", 1),
                toggle("nursing", "Résident EHPAD / institution (+10)", "Nursing home resident (+10)", "Residencia (+10)", 10),
                toggle("cancer", "Néoplasie (+30)", "Neoplastic disease (+30)", "Neoplasia (+30)", 30),
                toggle("liver", "Hépatopathie (+20)", "Liver disease (+20)", "Hepatopatía (+20)", 20),
                toggle("chf", "Insuffisance cardiaque (+10)", "CHF (+10)", "ICC (+10)", 10),
                toggle("cvd", "Maladie cérébrovasculaire (+10)", "Cerebrovascular disease (+10)", "ECV (+10)", 10),
                toggle("renal", "Néphropathie (+10)", "Renal disease (+10)", "Nefropatía (+10)", 10),
                toggle("mental", "Confusion / altération mentale (+20)", "Altered mental status (+20)", "Alteración mental (+20)", 20),
                toggle("hypoxia", "PaO₂ < 60 mmHg ou SpO₂ < 90% (+10)", "Hypoxemia (+10)", "Hipoxemia (+10)", 10)
            )
        ),
        mcpScore(
            scoreId = "bode_copd",
            name = "Index BODE (BPCO)",
            specialty = pulmMeta,
            descFr = "Score pronostique multidimensionnel de survie dans la BPCO (0–10).",
            descEn = "BODE Index for COPD survival prognosis (BMI, Obstruction, Dyspnea, Exercise).",
            descEs = "Índice BODE pronóstico en EPOC (0–10).",
            minScore = 0.0,
            maxScore = 10.0,
            severeBoundary = 7.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("bmi", "IMC (Body Mass Index)", "BMI", "IMC", listOf(opt(0, "> 21 kg/m² (+0)"), opt(1, "≤ 21 kg/m² (+1)"))),
                choice("fev1", "VEMS (% théorique)", "FEV1 (% predicted)", "FEV1 (%)", listOf(opt(0, "≥ 65% (+0)"), opt(1, "50–64% (+1)"), opt(2, "36–49% (+2)"), opt(3, "≤ 35% (+3)"))),
                choice("mmrc", "Dyspnée mMRC", "mMRC Dyspnea", "Disnea mMRC", listOf(opt(0, "0–1 (+0)"), opt(1, "2 (+1)"), opt(2, "3 (+2)"), opt(3, "4 (+3)"))),
                choice("walk", "Test de marche 6 min", "6-min walk distance", "Prueba marcha 6 min", listOf(opt(0, "≥ 350 m (+0)"), opt(1, "250–349 m (+1)"), opt(2, "150–249 m (+2)"), opt(3, "≤ 149 m (+3)")))
            )
        ),
        mcpScore(
            scoreId = "mmrc_dyspnea",
            name = "Échelle de Dyspnée mMRC",
            specialty = pulmMeta,
            descFr = "Évaluation clinique du retentissement fonctionnel de la dyspnée (0–4).",
            descEn = "Modified Medical Research Council dyspnea severity scale (0–4).",
            descEs = "Escala de disnea mMRC (0–4).",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "Grade",
            inputs = listOf(
                choice("grade", "Seuil de dyspnée", "Dyspnea threshold", "Umbral de disnea", listOf(
                    opt(0, "0 - Effort intense uniquement"),
                    opt(1, "1 - Marche rapide ou légère côte"),
                    opt(2, "2 - Marche plus lentement que les pairs"),
                    opt(3, "3 - Arrêt après 100 m sur terrain plat"),
                    opt(4, "4 - Essoufflé au déshabillage / ne sort plus")
                ))
            )
        ),
        mcpScore(
            scoreId = "cat_copd",
            name = "Test CAT (Impact BPCO)",
            specialty = pulmMeta,
            descFr = "COPD Assessment Test mesurant la qualité de vie respiratoire (0–40).",
            descEn = "COPD Assessment Test for daily symptom burden (0–40).",
            descEs = "Cuestionario CAT de impacto en EPOC (0–40).",
            minScore = 0.0,
            maxScore = 40.0,
            severeBoundary = 21.0,
            moderateBoundary = 10.0,
            supportsIntervention = false,
            inputs = listOf(
                slider("cough", "Toux", "Cough", "Tos", 0.0, 5.0, 1.0),
                slider("phlegm", "Expectorations", "Phlegm", "Flema", 0.0, 5.0, 1.0),
                slider("chest", "Oppression thoracique", "Chest tightness", "Opresión torácica", 0.0, 5.0, 1.0),
                slider("breathless", "Essoufflement en côte", "Breathlessness on hills", "Disnea en cuestas", 0.0, 5.0, 2.0),
                slider("activities", "Limitation activités", "Home activity limitation", "Limitación actividades", 0.0, 5.0, 1.0),
                slider("confidence", "Confiance pour sortir", "Confidence leaving home", "Confianza al salir", 0.0, 5.0, 1.0),
                slider("sleep", "Qualité du sommeil", "Sleep quality", "Calidad del sueño", 0.0, 5.0, 1.0),
                slider("energy", "Niveau d'énergie", "Energy level", "Nivel de energía", 0.0, 5.0, 2.0)
            )
        ),
        mcpScore(
            scoreId = "stop_bang",
            name = "Score STOP-BANG (SAOS)",
            specialty = pulmMeta,
            descFr = "Dépistage du syndrome d'apnées obstructives du sommeil (0–8).",
            descEn = "STOP-BANG screening score for obstructive sleep apnea (0–8).",
            descEs = "Cuestionario STOP-BANG para apnea obstructiva del sueño (0–8).",
            minScore = 0.0,
            maxScore = 8.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                toggle("snore", "S - Ronflement sonore", "Snoring loudly", "Ronquidos fuertes", 1),
                toggle("tired", "T - Fatigue / somnolence diurne", "Tiredness / daytime sleepiness", "Cansancio diurno", 1),
                toggle("observed", "O - Apnées observées", "Observed apneas", "Apneas observadas", 1),
                toggle("pressure", "P - Hypertension artérielle", "High blood Pressure", "Hipertensión arterial", 1),
                toggle("bmi", "B - IMC > 35 kg/m²", "BMI > 35 kg/m²", "IMC > 35 kg/m²", 1),
                toggle("age", "A - Âge > 50 ans", "Age > 50", "Edad > 50", 1),
                toggle("neck", "N - Tour de cou > 40 cm", "Neck circumference > 40 cm", "Cuello > 40 cm", 1),
                toggle("gender", "G - Sexe masculin", "Male gender", "Sexo masculino", 1)
            )
        )
    )

    // 4 additional Hepatology calculators (+ meld = 5 total)
    val hepatologyExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "meld_na",
            name = "MELD-Na Score",
            specialty = hepMeta,
            descFr = "Score MELD ajusté à la natrémie pour priorisation de transplantation hépatique (6–40).",
            descEn = "Sodium-adjusted MELD score for liver transplant allocation (6–40).",
            descEs = "Puntuación MELD-Na ajustada por sodio sérico (6–40).",
            minScore = 6.0,
            maxScore = 40.0,
            severeBoundary = 25.0,
            moderateBoundary = 15.0,
            formulaKey = "meld_na",
            inputs = listOf(
                slider("bilirubin", "Bilirubine totale (mg/dL)", "Total bilirubin", "Bilirrubina", 1.0, 25.0, 1.8, "mg/dL", 0.5),
                slider("inr", "INR", "INR", "INR", 1.0, 6.0, 1.3, "", 0.1),
                slider("creatinine", "Créatinine (mg/dL)", "Creatinine", "Creatinina", 1.0, 4.0, 1.1, "mg/dL", 0.1),
                slider("sodium", "Natrémie (mEq/L)", "Serum sodium", "Sodio sérico", 120.0, 145.0, 134.0, "mEq/L")
            )
        ),
        mcpScore(
            scoreId = "child_pugh",
            name = "Score de Child-Pugh (Cirrhose)",
            specialty = hepMeta,
            descFr = "Classification pronostique de la cirrhose hépatique en stades A, B et C (5–15).",
            descEn = "Child-Turcotte-Pugh classification of cirrhosis severity (5–15).",
            descEs = "Clasificación de Child-Pugh de gravedad de cirrosis (5–15).",
            minScore = 5.0,
            maxScore = 15.0,
            severeBoundary = 10.0,
            moderateBoundary = 7.0,
            inputs = listOf(
                choice("enceph", "Encéphalopathie hépatique", "Encephalopathy", "Encefalopatía",
                    listOf(opt(1, "1 - Absente"), opt(2, "2 - Grade 1–2"), opt(3, "3 - Grade 3–4")), 1),
                choice("ascites", "Ascite", "Ascites", "Ascitis",
                    listOf(opt(1, "1 - Absente"), opt(2, "2 - Modérée / diurétiques"), opt(3, "3 - Réfractaire / tendue")), 1),
                choice("bili", "Bilirubine (mg/dL)", "Bilirubin", "Bilirrubina",
                    listOf(opt(1, "1 - < 2.0"), opt(2, "2 - 2.0–3.0"), opt(3, "3 - > 3.0")), 1),
                choice("alb", "Albumine (g/dL)", "Albumin", "Albúmina",
                    listOf(opt(1, "1 - > 3.5"), opt(2, "2 - 2.8–3.5"), opt(3, "3 - < 2.8")), 1),
                choice("inr", "INR / TP", "INR", "INR",
                    listOf(opt(1, "1 - < 1.7"), opt(2, "2 - 1.7–2.3"), opt(3, "3 - > 2.3")), 1)
            )
        ),
        mcpScore(
            scoreId = "fib_4",
            name = "Index FIB-4 (Fibrose Hépatique)",
            specialty = hepMeta,
            descFr = "Estimation non invasive de la fibrose hépatique avancée : (Âge × ASAT) / (Plaquettes × √ALAT).",
            descEn = "Non-invasive FIB-4 index for advanced liver fibrosis screening.",
            descEs = "Índice FIB-4 no invasivo de fibrosis hepática.",
            minScore = 0.1,
            maxScore = 20.0,
            severeBoundary = 2.67,
            moderateBoundary = 1.3,
            unitDisplay = "FIB-4",
            formulaKey = "fib_4",
            inputs = listOf(
                slider("age", "Âge (ans)", "Age", "Edad", 18.0, 90.0, 52.0, "ans"),
                slider("ast", "ASAT / TGO (UI/L)", "AST (U/L)", "AST (U/L)", 10.0, 400.0, 48.0, "UI/L"),
                slider("alt", "ALAT / TGP (UI/L)", "ALT (U/L)", "ALT (U/L)", 10.0, 400.0, 42.0, "UI/L"),
                slider("platelets", "Plaquettes (G/L)", "Platelets (10⁹/L)", "Plaquetas (10⁹/L)", 30.0, 450.0, 180.0, "G/L")
            )
        ),
        mcpScore(
            scoreId = "maddrey_df",
            name = "Score de Maddrey (Hépatite Alcoolique)",
            specialty = hepMeta,
            descFr = "Fonction discriminante de Maddrey guidant la corticothérapie (≥ 32 = sévère).",
            descEn = "Maddrey's Discriminant Function for alcoholic hepatitis severity (≥ 32 severe).",
            descEs = "Función discriminante de Maddrey en hepatitis alcohólica.",
            minScore = 0.0,
            maxScore = 150.0,
            severeBoundary = 32.0,
            moderateBoundary = 20.0,
            unitDisplay = "DF",
            formulaKey = "maddrey_df",
            inputs = listOf(
                slider("pt_patient", "Temps de Quick patient (s)", "Patient PT (sec)", "TP paciente (s)", 10.0, 45.0, 18.0, "s", 0.5),
                slider("pt_control", "Temps de Quick témoin (s)", "Control PT (sec)", "TP control (s)", 10.0, 15.0, 12.0, "s", 0.5),
                slider("bilirubin", "Bilirubine totale (mg/dL)", "Bilirubin (mg/dL)", "Bilirrubina (mg/dL)", 0.5, 35.0, 8.0, "mg/dL", 0.5)
            )
        )
    )

    // 4 Gastroenterology calculators (4 total)
    val gastroScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "ranson_admission",
            name = "Critères de Ranson (Admission)",
            specialty = gastroMeta,
            descFr = "Gravité initiale des pancréatites aiguës à l'admission (0–5).",
            descEn = "Ranson Criteria at admission for acute pancreatitis severity (0–5).",
            descEs = "Criterios de Ranson al ingreso en pancreatitis aguda (0–5).",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("age55", "Âge > 55 ans", "Age > 55", "Edad > 55", 1),
                toggle("wbc16", "Leucocytes > 16 000/mm³", "WBC > 16,000/µL", "Leucocitos > 16,000", 1),
                toggle("gluc200", "Glycémie > 11 mmol/L (200 mg/dL)", "Glucose > 200 mg/dL", "Glucosa > 200 mg/dL", 1),
                toggle("ldh350", "LDH > 350 UI/L", "LDH > 350 U/L", "LDH > 350 U/L", 1),
                toggle("ast250", "ASAT > 250 UI/L", "AST > 250 U/L", "AST > 250 U/L", 1)
            )
        ),
        mcpScore(
            scoreId = "bisap_pancreatitis",
            name = "Score BISAP (Pancréatite Aiguë)",
            specialty = gastroMeta,
            descFr = "Score précoce de mortalité dans les 24 premières heures d'une pancréatite aiguë (0–5).",
            descEn = "Bedside Index of Severity in Acute Pancreatitis (0–5).",
            descEs = "Índice BISAP de gravedad en pancreatitis aguda (0–5).",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("bun", "B - Urée > 8.9 mmol/L (BUN > 25 mg/dL)", "BUN > 25 mg/dL", "Urea > 25 mg/dL", 1),
                toggle("impaired", "I - Altération conscience (GCS < 15)", "Impaired mental status", "Alteración mental", 1),
                toggle("sirs", "S - ≥ 2 critères SIRS", "SIRS ≥ 2 criteria", "SIRS ≥ 2", 1),
                toggle("age60", "A - Âge > 60 ans", "Age > 60", "Edad > 60", 1),
                toggle("pleural", "P - Épanchement pleural", "Pleural effusion", "Derrame pleural", 1)
            )
        ),
        mcpScore(
            scoreId = "rockall_gib",
            name = "Score de Rockall Clinique (HDH)",
            specialty = gastroMeta,
            descFr = "Prédiction de récidive hémorragique et mortalité pré-endoscopie (0–7).",
            descEn = "Pre-endoscopy Clinical Rockall Score for upper GI bleed (0–7).",
            descEs = "Puntuación clínica de Rockall en hemorragia digestiva alta (0–7).",
            minScore = 0.0,
            maxScore = 7.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("age", "Âge", "Age", "Edad", listOf(opt(0, "< 60 (+0)"), opt(1, "60–79 (+1)"), opt(2, "≥ 80 (+2)"))),
                choice("shock", "État hémodynamique", "Shock status", "Shock", listOf(opt(0, "PAS ≥ 100 & FC < 100 (+0)"), opt(1, "PAS ≥ 100 & FC ≥ 100 (+1)"), opt(2, "PAS < 100 (+2)"))),
                choice("comorb", "Comorbidités", "Comorbidity", "Comorbilidad", listOf(opt(0, "Aucune majeure (+0)"), opt(2, "Cardiaque / majeure (+2)"), opt(3, "IR, cirrhose ou néoplasie (+3)")))
            )
        ),
        mcpScore(
            scoreId = "apri_liver",
            name = "Score APRI (ASAT / Plaquettes)",
            specialty = gastroMeta,
            descFr = "Dépistage rapide de cirrhose et fibrose significative : ((ASAT / LSN) / Plaquettes) × 100.",
            descEn = "AST to Platelet Ratio Index for hepatic fibrosis screening.",
            descEs = "Índice APRI de fibrosis hepática.",
            minScore = 0.05,
            maxScore = 15.0,
            severeBoundary = 1.5,
            moderateBoundary = 0.7,
            unitDisplay = "APRI",
            formulaKey = "apri_liver",
            inputs = listOf(
                slider("ast", "ASAT (UI/L)", "AST (U/L)", "AST", 10.0, 400.0, 55.0, "UI/L"),
                slider("ast_uln", "Limite supérieure ASAT (LSN)", "AST Upper Limit Normal", "LSN AST", 25.0, 55.0, 40.0, "UI/L"),
                slider("platelets", "Plaquettes (G/L)", "Platelets (10⁹/L)", "Plaquetas", 30.0, 450.0, 150.0, "G/L")
            )
        )
    )
}
