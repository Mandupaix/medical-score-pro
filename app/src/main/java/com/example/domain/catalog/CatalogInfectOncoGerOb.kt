package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogInfectOncoGerOb {

    val infectMeta = SpecialtyMeta("infectious", "Infectiologie", "Infectious Disease", "Infectología")
    val oncoMeta = SpecialtyMeta("oncology", "Oncologie", "Oncology", "Oncología")
    val gerMeta = SpecialtyMeta("geriatrics", "Gériatrie", "Geriatrics", "Geriatría")
    val obMeta = SpecialtyMeta("obstetrics", "Obstétrique", "Obstetrics", "Obstetricia")

    // 5 Infectious Disease calculators
    val infectiousScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "lrinec",
            name = "Score LRINEC (Fasciite Nécrosante)",
            specialty = infectMeta,
            descFr = "Laboratory Risk Indicator for Necrotizing Fasciitis (≥ 6 suspect, ≥ 8 élevé).",
            descEn = "LRINEC Score for differentiating necrotizing fasciitis from cellulitis (0–13).",
            descEs = "Índice LRINEC para fascitis necrotizante (0–13).",
            minScore = 0.0,
            maxScore = 13.0,
            severeBoundary = 8.0,
            moderateBoundary = 6.0,
            inputs = listOf(
                choice("crp", "CRP (mg/L)", "CRP (mg/L)", "PCR (mg/L)", listOf(opt(0, "< 150 (+0)"), opt(4, "≥ 150 (+4)"))),
                choice("wbc", "Leucocytes (G/L)", "WBC (×10³/µL)", "Leucocitos", listOf(opt(0, "< 15 (+0)"), opt(1, "15–25 (+1)"), opt(2, "> 25 (+2)"))),
                choice("hb", "Hémoglobine (g/dL)", "Hemoglobin", "Hemoglobina", listOf(opt(0, "> 13.5 (+0)"), opt(1, "11–13.5 (+1)"), opt(2, "< 11 (+2)"))),
                choice("na", "Natrémie (mEq/L)", "Sodium", "Sodio", listOf(opt(0, "≥ 135 (+0)"), opt(2, "< 135 (+2)"))),
                choice("creat", "Créatinine (µmol/L)", "Creatinine", "Creatinina", listOf(opt(0, "≤ 141 (+0)"), opt(2, "> 141 (+2)"))),
                choice("gluc", "Glycémie (mmol/L)", "Glucose", "Glucosa", listOf(opt(0, "≤ 10 (+0)"), opt(1, "> 10 (+1)")))
            )
        ),
        mcpScore(
            scoreId = "centor_adult",
            name = "Score de Centor (Angine Adulte)",
            specialty = infectMeta,
            descFr = "Probabilité d'infection streptococcique du groupe A chez l'adulte (0–4).",
            descEn = "Centor Criteria for Group A Strep pharyngitis in adults (0–4).",
            descEs = "Criterios de Centor para faringitis estreptocócica en adultos (0–4).",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("exudate", "Exsudat amygdalien (+1)", "Tonsillar exudates (+1)", "Exudado amigdalar (+1)", 1),
                toggle("nodes", "Adénopathies cervicales antérieures douloureuses (+1)", "Tender anterior cervical nodes (+1)", "Adenopatías cervicales (+1)", 1),
                toggle("fever", "Fièvre > 38°C (+1)", "Fever history (+1)", "Fiebre (+1)", 1),
                toggle("no_cough", "Absence de toux (+1)", "Absence of cough (+1)", "Ausencia de tos (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "mascc_fn",
            name = "Score MASCC (Neutropénie Fébrile)",
            specialty = infectMeta,
            descFr = "Identification des neutropénies fébriles à faible risque (≥ 21) vs haut risque (< 21).",
            descEn = "MASCC Risk Index for febrile neutropenia (≥ 21 low risk).",
            descEs = "Índice MASCC de riesgo en neutropenia febril.",
            minScore = 0.0,
            maxScore = 26.0,
            severeBoundary = 15.0,
            moderateBoundary = 20.0,
            higherIsWorse = false,
            inputs = listOf(
                choice("burden", "Symptômes cliniques", "Burden of illness", "Carga clínica",
                    listOf(opt(5, "Aucun ou légers (+5)"), opt(3, "Modérés (+3)"), opt(0, "Sévères (+0)")), 5),
                toggle("no_hypo", "Absence d'hypotension (PAS > 90) (+5)", "No hypotension (+5)", "Sin hipotensión (+5)", 5, true),
                toggle("no_copd", "Absence de BPCO (+4)", "No COPD (+4)", "Sin EPOC (+4)", 4, true),
                toggle("solid_no_fungal", "Tumeur solide ou pas d'infection fongique (+4)", "Solid tumor / no prior fungal inf (+4)", "Tumor sólido (+4)", 4, true),
                toggle("no_dehyd", "Absence de déshydratation IV (+3)", "No dehydration (+3)", "Sin deshidratación (+3)", 3, true),
                toggle("outpatient", "Début ambulatoire (+3)", "Outpatient status (+3)", "Ambulatorio (+3)", 3),
                toggle("age60", "Âge < 60 ans (+2)", "Age < 60 (+2)", "Edad < 60 (+2)", 2, true)
            )
        ),
        mcpScore(
            scoreId = "duke_sepsis_sofa",
            name = "Score de Pitt (Bactériémie)",
            specialty = infectMeta,
            descFr = "Gravité aiguë des bactériémies à l'admission (≥ 4 = état critique, 0–14).",
            descEn = "Pitt Bacteremia Score for acute mortality risk in bloodstream infections (0–14).",
            descEs = "Puntuación de bacteriemia de Pitt (0–14).",
            minScore = 0.0,
            maxScore = 14.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("temp", "Température", "Temperature", "Temperatura", listOf(opt(0, "36.1–38.9°C (+0)"), opt(1, "35.1–36.0 ou 39.0–39.9 (+1)"), opt(2, "≤ 35.0 ou ≥ 40.0 (+2)"))),
                toggle("hypo", "Hypotension / Vasopresseurs (+2)", "Hypotension (+2)", "Hipotensión (+2)", 2),
                toggle("vent", "Ventilation mécanique (+2)", "Mechanical ventilation (+2)", "Ventilación mecánica (+2)", 2),
                toggle("arrest", "Arrêt cardiaque (+4)", "Cardiac arrest (+4)", "Paro cardíaco (+4)", 4),
                choice("mental", "État mental", "Mental status", "Estado mental", listOf(opt(0, "Alerte (+0)"), opt(1, "Désorienté (+1)"), opt(2, "Stupeur (+2)"), opt(4, "Coma (+4)")))
            )
        ),
        mcpScore(
            scoreId = "sirs_criteria",
            name = "Critères SIRS (Réponse Inflammatoire)",
            specialty = infectMeta,
            descFr = "Syndrome de réponse inflammatoire systémique (≥ 2 critères positifs sur 4).",
            descEn = "Systemic Inflammatory Response Syndrome (SIRS) criteria (0–4).",
            descEs = "Criterios SIRS de respuesta inflamatoria sistémica (0–4).",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("temp", "Température > 38.0°C ou < 36.0°C", "Temp > 38°C or < 36°C", "Temperatura > 38 o < 36°C", 1),
                toggle("hr", "Fréquence cardiaque > 90 bpm", "Heart rate > 90 bpm", "FC > 90 lpm", 1),
                toggle("rr", "FR > 20/min ou PaCO₂ < 32 mmHg", "RR > 20 or PaCO2 < 32", "FR > 20 o PaCO2 < 32", 1),
                toggle("wbc", "Leucocytes > 12 000 ou < 4 000/mm³", "WBC > 12k or < 4k or > 10% bands", "Leucocitos > 12k o < 4k", 1)
            )
        )
    )

    // 4 Oncology calculators
    val oncologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "ecog_ps",
            name = "Indice de Performance ECOG / OMS",
            specialty = oncoMeta,
            descFr = "Échelle fonctionnelle oncologique standard de l'OMS (0 = autonome à 5 = décès).",
            descEn = "ECOG / WHO Performance Status scale for oncology eligibility (0–5).",
            descEs = "Escala funcional ECOG / OMS en oncología (0–5).",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "ECOG",
            inputs = listOf(
                choice("grade", "Grade ECOG", "ECOG Grade", "Grado ECOG", listOf(
                    opt(0, "0 - Activité normale sans restriction"),
                    opt(1, "1 - Ambulatoire, travaux physiques intenses limités"),
                    opt(2, "2 - Autonome, incapable travailler, debout > 50%"),
                    opt(3, "3 - Soins limités, alité/fauteuil > 50% du temps"),
                    opt(4, "4 - Totalement dépendant, alité 100%"),
                    opt(5, "5 - Décès")
                ))
            )
        ),
        mcpScore(
            scoreId = "karnofsky",
            name = "Indice de Karnofsky (KPS)",
            specialty = oncoMeta,
            descFr = "Évaluation de l'autonomie fonctionnelle en oncologie de 0% à 100%.",
            descEn = "Karnofsky Performance Status functional impairment scale (0–100%).",
            descEs = "Índice de estado funcional de Karnofsky (0–100%).",
            minScore = 0.0,
            maxScore = 100.0,
            severeBoundary = 40.0,
            moderateBoundary = 70.0,
            higherIsWorse = false,
            unitDisplay = "%",
            inputs = listOf(
                choice("kps", "Score KPS", "KPS Score", "Puntuación KPS", listOf(
                    opt(100, "100% - Normal, aucun symptôme"),
                    opt(90, "90% - Activité normale, symptômes mineurs"),
                    opt(80, "80% - Activité normale avec effort"),
                    opt(70, "70% - Autonome, incapable de travailler"),
                    opt(60, "60% - Aide occasionnelle requise"),
                    opt(50, "50% - Aide et soins fréquents requis"),
                    opt(40, "40% - Invalide, soins spécialisés"),
                    opt(30, "30% - Sévèrement invalide, hospitalisation"),
                    opt(20, "20% - Très malade, soins intensifs de support"),
                    opt(10, "10% - Moribond")
                ), 90)
            )
        ),
        mcpScore(
            scoreId = "bsa_mosteller",
            name = "Surface Corporelle (Mosteller BSA)",
            specialty = oncoMeta,
            descFr = "Calcul de la surface corporelle pour posologie de chimiothérapie : √((Taille × Poids)/3600).",
            descEn = "Mosteller Body Surface Area (m²) for oncology dosing.",
            descEs = "Superficie corporal por fórmula de Mosteller (m²).",
            minScore = 0.3,
            maxScore = 3.5,
            severeBoundary = 2.6,
            moderateBoundary = 2.2,
            unitDisplay = "m²",
            formulaKey = "bsa_mosteller",
            inputs = listOf(
                slider("height", "Taille (cm)", "Height (cm)", "Talla (cm)", 60.0, 220.0, 172.0, "cm"),
                slider("weight", "Poids (kg)", "Weight (kg)", "Peso (kg)", 15.0, 180.0, 70.0, "kg")
            )
        ),
        mcpScore(
            scoreId = "calvert_carboplatin",
            name = "Formule de Calvert (Carboplatine)",
            specialty = oncoMeta,
            descFr = "Calcul posologique du Carboplatine (mg) = AUC cible × (DFG + 25).",
            descEn = "Calvert Formula for Carboplatin dosing: Dose (mg) = AUC × (GFR + 25).",
            descEs = "Fórmula de Calvert para dosis de carboplatino (mg).",
            minScore = 100.0,
            maxScore = 1500.0,
            severeBoundary = 900.0,
            moderateBoundary = 650.0,
            unitDisplay = "mg",
            formulaKey = "calvert_carboplatin",
            inputs = listOf(
                slider("auc", "AUC cible (mg/mL·min)", "Target AUC", "AUC objetivo", 2.0, 7.5, 5.0, "AUC", 0.5),
                slider("gfr", "DFG / Clairance créatinine (mL/min)", "GFR (mL/min)", "FG (mL/min)", 15.0, 125.0, 75.0, "mL/min")
            )
        )
    )

    // 4 Geriatrics calculators
    val geriatricsScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "mmse_screen",
            name = "MMSE (Mini-Mental State)",
            specialty = gerMeta,
            descFr = "Évaluation cognitive globale de Folstein sur 30 points (Orientation, Mémoire, Calcul, Langage).",
            descEn = "Mini-Mental State Examination cognitive screening summary (0–30).",
            descEs = "Examen cognitivo Mini-Mental State (0–30).",
            minScore = 0.0,
            maxScore = 30.0,
            severeBoundary = 17.0,
            moderateBoundary = 23.0,
            higherIsWorse = false,
            supportsIntervention = false,
            inputs = listOf(
                slider("orient", "Orientation temporelle & spatiale", "Orientation (0–10)", "Orientación", 0.0, 10.0, 9.0),
                slider("reg", "Apprentissage & Rappel", "Registration & Recall (0–6)", "Registro y recuerdo", 0.0, 6.0, 5.0),
                slider("attn", "Attention et calcul", "Attention & Calculation (0–5)", "Atención y cálculo", 0.0, 5.0, 4.0),
                slider("lang", "Langage & Praxies constructives", "Language & Praxis (0–9)", "Lenguaje y praxias", 0.0, 9.0, 8.0)
            )
        ),
        mcpScore(
            scoreId = "moca_screen",
            name = "Score MoCA (Montréal)",
            specialty = gerMeta,
            descFr = "Dépistage sensible des troubles cognitifs légers (MCI) sur 30 points (≥ 26 normal).",
            descEn = "Montreal Cognitive Assessment (MoCA) for mild cognitive impairment (0–30).",
            descEs = "Evaluación cognitiva de Montreal MoCA (0–30).",
            minScore = 0.0,
            maxScore = 30.0,
            severeBoundary = 17.0,
            moderateBoundary = 25.0,
            higherIsWorse = false,
            supportsIntervention = false,
            inputs = listOf(
                slider("visuo", "Visuo-spatial / Exécutif (0–5)", "Visuospatial/Executive", "Visuoespacial", 0.0, 5.0, 4.0),
                slider("naming", "Dénomination & Attention (0–9)", "Naming & Attention", "Denominación y atención", 0.0, 9.0, 8.0),
                slider("lang_abs", "Langage & Abstraction (0–5)", "Language & Abstraction", "Lenguaje y abstracción", 0.0, 5.0, 4.0),
                slider("recall_or", "Rappel différé & Orientation (0–11)", "Delayed Recall & Orientation", "Recuerdo y orientación", 0.0, 11.0, 10.0),
                toggle("edu12", "Scolarité ≤ 12 ans (+1)", "Education ≤ 12 yrs (+1)", "Escolaridad ≤ 12 años (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "katz_adl",
            name = "Grille ADL de Katz (Autonomie)",
            specialty = gerMeta,
            descFr = "Autonomie dans les 6 activités de base de la vie quotidienne (0 = dépendant à 6 = autonome).",
            descEn = "Katz Index of Independence in Activities of Daily Living (0–6).",
            descEs = "Índice de Katz de independencia en actividades diarias (0–6).",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 2.0,
            moderateBoundary = 4.0,
            higherIsWorse = false,
            supportsIntervention = false,
            inputs = listOf(
                toggle("bath", "Toilette autonome", "Bathing independent", "Baño independiente", 1, true),
                toggle("dress", "Habillage autonome", "Dressing independent", "Vestido independiente", 1, true),
                toggle("toilet", "Aller aux toilettes autonome", "Toileting independent", "Uso WC independiente", 1, true),
                toggle("transfer", "Transferts autonomes", "Transferring independent", "Traslados independientes", 1, true),
                toggle("cont", "Continence", "Continence", "Continencia", 1, true),
                toggle("feed", "Alimentation autonome", "Feeding independent", "Alimentación independiente", 1, true)
            )
        ),
        mcpScore(
            scoreId = "charlson_cci",
            name = "Index de Comorbidité de Charlson (CCI)",
            specialty = gerMeta,
            descFr = "Prédiction de survie à 10 ans pondérée par l'âge et les comorbidités chroniques.",
            descEn = "Charlson Comorbidity Index for 10-year survival stratification.",
            descEs = "Índice de comorbilidad de Charlson.",
            minScore = 0.0,
            maxScore = 24.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("age_dec", "Tranche d'âge", "Age bracket", "Edad", listOf(opt(0, "< 50 (+0)"), opt(1, "50–59 (+1)"), opt(2, "60–69 (+2)"), opt(3, "70–79 (+3)"), opt(4, "≥ 80 (+4)"))),
                toggle("mi_hf", "IDM ou Insuffisance cardiaque (+1)", "MI or CHF (+1)", "IAM o ICC (+1)", 1),
                toggle("copd_dm", "BPCO ou Diabète non compliqué (+1)", "COPD or uncomplicated DM (+1)", "EPOC o DM (+1)", 1),
                toggle("dementia", "Démence ou AVC/AIT (+1)", "Dementia or CVA (+1)", "Demencia o ACV (+1)", 1),
                toggle("ckd_mod", "IRC modérée/sévère ou Diabète compliqué (+2)", "Moderate/severe CKD or DM end-organ (+2)", "ERC o DM complicada (+2)", 2),
                toggle("cancer", "Tumeur solide localisée / Leucémie (+2)", "Solid tumor / Leukemia (+2)", "Tumor sólido / Leucemia (+2)", 2),
                toggle("metastatic", "Tumeur métastatique ou Cirrhose sévère (+6)", "Metastatic solid tumor (+6)", "Tumor metastásico (+6)", 6)
            )
        )
    )

    // 4 Obstetrics calculators
    val obstetricsScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "bishop_score",
            name = "Score de Bishop (Maturation Cervicale)",
            specialty = obMeta,
            descFr = "Prédiction du succès d'un déclenchement du travail obstétrical (0–13, ≥ 8 favorable).",
            descEn = "Bishop Score assessing cervical readiness for labor induction (0–13).",
            descEs = "Índice de Bishop de maduración cervical para inducción del parto (0–13).",
            minScore = 0.0,
            maxScore = 13.0,
            severeBoundary = 4.0,
            moderateBoundary = 7.0,
            higherIsWorse = false,
            inputs = listOf(
                choice("dilation", "Dilatation (cm)", "Dilation", "Dilatación", listOf(opt(0, "Fermé (0)"), opt(1, "1–2 cm (+1)"), opt(2, "3–4 cm (+2)"), opt(3, "≥ 5 cm (+3)"))),
                choice("efface", "Effacement (%)", "Effacement", "Borramiento", listOf(opt(0, "0–30% (+0)"), opt(1, "40–50% (+1)"), opt(2, "60–70% (+2)"), opt(3, "≥ 80% (+3)"))),
                choice("station", "Présentation / Hauteur", "Station", "Altura presentación", listOf(opt(0, "-3 (+0)"), opt(1, "-2 (+1)"), opt(2, "-1/0 (+2)"), opt(3, "+1/+2 (+3)"))),
                choice("consist", "Consistance", "Consistency", "Consistencia", listOf(opt(0, "Ferme (+0)"), opt(1, "Moyenne (+1)"), opt(2, "Molle (+2)"))),
                choice("position", "Position du col", "Position", "Posición", listOf(opt(0, "Postérieure (+0)"), opt(1, "Médiane (+1)"), opt(2, "Antérieure (+2)")))
            )
        ),
        mcpScore(
            scoreId = "fullpiers_preeclampsia",
            name = "Score de Sévérité Pré-éclampsie",
            specialty = obMeta,
            descFr = "Évaluation des critères de gravité materno-fœtale devant une hypertension gravidique.",
            descEn = "Preeclampsia severe maternal feature screening checklist.",
            descEs = "Criterios de gravedad materna en preeclampsia.",
            minScore = 0.0,
            maxScore = 7.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("bp160", "HTA sévère (PAS ≥ 160 ou PAD ≥ 110)", "Severe BP ≥ 160/110", "PA ≥ 160/110", 2),
                toggle("plt100", "Thrombopénie < 100 000/mm³", "Platelets < 100,000", "Plaquetas < 100,000", 1),
                toggle("liver", "Cytolyse hépatique (ASAT/ALAT ≥ 2N) ou barre épigastrique", "Transaminases ≥ 2× ULN", "Transaminasas ≥ 2N", 1),
                toggle("renal", "Créatinine > 97 µmol/L (1.1 mg/dL)", "Creatinine > 1.1 mg/dL", "Creatinina > 1.1", 1),
                toggle("pulm", "Œdème aigu pulmonaire", "Pulmonary edema", "Edema pulmonar", 1),
                toggle("neuro", "Céphalées sévères / troubles visuels / ROT vifs", "Cerebral or visual disturbances", "Síntomas neurológicos", 1)
            )
        ),
        mcpScore(
            scoreId = "edinburg_epds",
            name = "Échelle d'Édimbourg (EPDS Post-Partum)",
            specialty = obMeta,
            descFr = "Dépistage de la dépression du post-partum en 10 items (0–30, ≥ 13 positif).",
            descEn = "Edinburgh Postnatal Depression Scale (EPDS) screening (0–30).",
            descEs = "Escala de depresión posparto de Edimburgo EPDS (0–30).",
            minScore = 0.0,
            maxScore = 30.0,
            severeBoundary = 13.0,
            moderateBoundary = 10.0,
            supportsIntervention = false,
            inputs = listOf(
                slider("anhedonia", "Capacité à rire / plaisir (Items 1–2)", "Laugh & enjoyment (0–6)", "Risa y disfrute", 0.0, 6.0, 1.0),
                slider("guilt_anx", "Culpabilité & anxiété (Items 3–5)", "Blame & worry (0–9)", "Culpa y ansiedad", 0.0, 9.0, 2.0),
                slider("overwhelm", "Débordement & sommeil (Items 6–7)", "Overwhelmed & sleep (0–6)", "Sobrecarga y sueño", 0.0, 6.0, 1.0),
                slider("sad_self", "Tristesse, pleurs & idées noires (Items 8–10)", "Sadness & self-harm (0–9)", "Tristeza y autolesión", 0.0, 9.0, 0.0)
            )
        ),
        mcpScore(
            scoreId = "obstetric_shock_index",
            name = "Index de Choc Obstétrical (OSI)",
            specialty = obMeta,
            descFr = "Dépistage précoce d'hémorragie du post-partum (FC / PAS, seuil alerte ≥ 0.9–1.0).",
            descEn = "Obstetric Shock Index (HR / SBP) for postpartum hemorrhage triage.",
            descEs = "Índice de choque obstétrico para hemorragia posparto.",
            minScore = 0.3,
            maxScore = 3.0,
            severeBoundary = 1.0,
            moderateBoundary = 0.9,
            unitDisplay = "OSI",
            formulaKey = "shock_index",
            inputs = listOf(
                slider("hr", "Fréquence cardiaque maternelle", "Maternal HR", "FC materna", 50.0, 180.0, 92.0, "bpm"),
                slider("sbp", "Pression systolique (PAS)", "Systolic BP", "PAS", 55.0, 200.0, 110.0, "mmHg")
            )
        )
    )
}
