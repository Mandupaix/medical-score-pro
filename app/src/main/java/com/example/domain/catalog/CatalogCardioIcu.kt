package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogCardioIcu {

    val cardioMeta = SpecialtyMeta("cardiology", "Cardiologie", "Cardiology", "Cardiología")
    val icuMeta = SpecialtyMeta("intensive_care", "Soins Intensifs", "Intensive Care", "Cuidados Intensivos")

    // 11 additional Cardiology calculators (+ euroscore_ii + cha2ds2_vasc = 13 total)
    val cardiologyExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "has_bled",
            name = "HAS-BLED (Risque Hémorragique)",
            specialty = cardioMeta,
            descFr = "Évaluation du risque de saignement majeur sous anticoagulation pour FA (0–9).",
            descEn = "Major bleeding risk assessment on anticoagulation for atrial fibrillation (0–9).",
            descEs = "Riesgo de sangrado mayor con anticoagulación en fibrilación auricular (0–9).",
            minScore = 0.0,
            maxScore = 9.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            isCardiac = true,
            inputs = listOf(
                toggle("htn", "H - HTA non contrôlée (PAS > 160 mmHg)", "Hypertension (SBP > 160)", "HTA > 160 mmHg", 1),
                toggle("renal", "A - Insuffisance rénale (Dialyse/Cr ≥ 200 µmol/L)", "Abnormal renal function", "Disfunción renal", 1),
                toggle("liver", "A - Insuffisance hépatique (Cirrhose/Bili > 2N)", "Abnormal liver function", "Disfunción hepática", 1),
                toggle("stroke", "S - Antécédent d'AVC", "Prior stroke", "Ictus previo", 1),
                toggle("bleed", "B - Antécédent ou prédisposition hémorragique", "Prior major bleeding", "Sangrado previo", 1),
                toggle("inr", "L - INR labile (TTR < 60%)", "Labile INR", "INR lábil", 1),
                toggle("elderly", "E - Âge > 65 ans", "Age > 65 years", "Edad > 65 años", 1),
                toggle("drugs", "D - Antiagrégants / AINS", "Antiplatelets / NSAIDs", "Antiagregantes / AINE", 1),
                toggle("alcohol", "D - Consommation excessive d'alcool", "Alcohol excess", "Consumo excesivo alcohol", 1)
            )
        ),
        mcpScore(
            scoreId = "timi_nstemi",
            name = "TIMI Risk Score (SCA ST-)",
            specialty = cardioMeta,
            descFr = "Risque de mortalité ou événement ischémique à 14 jours dans le SCA sans sus-décalage ST (0–7).",
            descEn = "14-day mortality and ischemic event risk in UA/NSTEMI (0–7).",
            descEs = "Riesgo a 14 días en síndrome coronario agudo sin elevación ST (0–7).",
            minScore = 0.0,
            maxScore = 7.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            isCardiac = true,
            inputs = listOf(
                toggle("age65", "Âge ≥ 65 ans", "Age ≥ 65", "Edad ≥ 65", 1),
                toggle("cad_rf", "≥ 3 facteurs de risque CV", "≥ 3 CAD risk factors", "≥ 3 factores riesgo CV", 1),
                toggle("known_cad", "Coronaropathie connue (sténose ≥ 50%)", "Known CAD (≥ 50% stenosis)", "Enfermedad coronaria conocida", 1),
                toggle("asa", "Prise d'aspirine < 7 jours", "Aspirin use in past 7 days", "Aspirina en últimos 7 días", 1),
                toggle("angina", "≥ 2 épisodes angineux < 24h", "Severe angina (≥ 2 in 24h)", "Angina severa ≥ 2 en 24h", 1),
                toggle("st_dev", "Déviation du segment ST ≥ 0.5 mm", "ST deviation ≥ 0.5 mm", "Desviación ST ≥ 0.5 mm", 1),
                toggle("markers", "Troponine cardiaque positive", "Elevated cardiac markers", "Troponina elevada", 1)
            )
        ),
        mcpScore(
            scoreId = "timi_stemi",
            name = "TIMI Risk Score (STEMI)",
            specialty = cardioMeta,
            descFr = "Risque de mortalité à 30 jours après infarctus avec sus-décalage du ST (0–14).",
            descEn = "30-day mortality risk prediction in ST-elevation MI (0–14).",
            descEs = "Mortalidad a 30 días en infarto con elevación ST (0–14).",
            minScore = 0.0,
            maxScore = 14.0,
            severeBoundary = 6.0,
            moderateBoundary = 3.0,
            isCardiac = true,
            inputs = listOf(
                choice("age", "Âge", "Age", "Edad", listOf(opt(0, "< 65 ans (+0)"), opt(2, "65–74 ans (+2)"), opt(3, "≥ 75 ans (+3)"))),
                toggle("dm_htn_ang", "Diabète, HTA ou angor (+1)", "Diabetes, HTN or angina (+1)", "DM, HTA o angina (+1)", 1),
                toggle("sbp100", "PAS < 100 mmHg (+3)", "SBP < 100 mmHg (+3)", "PAS < 100 mmHg (+3)", 3),
                toggle("hr100", "FC > 100 bpm (+2)", "HR > 100 bpm (+2)", "FC > 100 lpm (+2)", 2),
                toggle("killip", "Killip II–IV (+2)", "Killip Class II–IV (+2)", "Killip II–IV (+2)", 2),
                toggle("wt67", "Poids < 67 kg (+1)", "Weight < 67 kg (+1)", "Peso < 67 kg (+1)", 1),
                toggle("ant_ste", "STEMI antérieur ou BBG (+1)", "Anterior STE or LBBB (+1)", "IAM anterior o BRI (+1)", 1),
                toggle("time4h", "Délai traitement > 4h (+1)", "Time to treatment > 4h (+1)", "Tiempo > 4h (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "grace_acs",
            name = "GRACE Score (SCA)",
            specialty = cardioMeta,
            descFr = "Stratification du risque de mortalité hospitalière et à 6 mois dans le SCA.",
            descEn = "In-hospital and 6-month mortality risk stratification in ACS.",
            descEs = "Estratificación de mortalidad intrahospitalaria y a 6 meses en SCA.",
            minScore = 1.0,
            maxScore = 300.0,
            severeBoundary = 140.0,
            moderateBoundary = 109.0,
            isCardiac = true,
            formulaKey = "grace_acs",
            inputs = listOf(
                slider("age", "Âge (ans)", "Age (years)", "Edad", 25.0, 95.0, 62.0, "ans"),
                slider("hr", "Fréquence cardiaque", "Heart rate", "Frecuencia cardíaca", 40.0, 180.0, 82.0, "bpm"),
                slider("sbp", "Pression artérielle systolique", "Systolic BP", "PAS", 60.0, 220.0, 125.0, "mmHg"),
                slider("creat", "Créatinine sérique (mg/dL)", "Serum creatinine", "Creatinina", 0.5, 5.0, 1.0, "mg/dL", 0.1),
                choice("killip", "Classe de Killip", "Killip Class", "Clase Killip", listOf(opt(0, "Killip I"), opt(20, "Killip II"), opt(39, "Killip III"), opt(59, "Killip IV"))),
                toggle("arrest", "Arrêt cardiaque à l'admission", "Cardiac arrest at admission", "Paro cardíaco al ingreso", 39),
                toggle("st_dev", "Modification du segment ST", "ST-segment deviation", "Desviación ST", 28),
                toggle("troponin", "Élévation des enzymes cardiaques", "Elevated cardiac enzymes", "Enzimas elevadas", 14)
            )
        ),
        mcpScore(
            scoreId = "qtc_bazett",
            name = "QTc (Formule de Bazett)",
            specialty = cardioMeta,
            descFr = "Correction de l'intervalle QT par la fréquence cardiaque (QT / √RR).",
            descEn = "Heart-rate corrected QT interval calculation using Bazett formula.",
            descEs = "Cálculo de intervalo QTc corregido por fórmula de Bazett.",
            minScore = 250.0,
            maxScore = 700.0,
            severeBoundary = 500.0,
            moderateBoundary = 460.0,
            unitDisplay = "ms",
            isCardiac = true,
            formulaKey = "qtc_bazett",
            inputs = listOf(
                slider("qt_ms", "Intervalle QT mesuré (ms)", "Measured QT (ms)", "QT medido (ms)", 250.0, 650.0, 400.0, "ms"),
                slider("hr", "Fréquence cardiaque (bpm)", "Heart rate (bpm)", "Frecuencia cardíaca", 35.0, 180.0, 75.0, "bpm")
            )
        ),
        mcpScore(
            scoreId = "map_bp",
            name = "Pression Artérielle Moyenne (PAM)",
            specialty = cardioMeta,
            descFr = "Calcul de la pression de perfusion tissulaire moyenne : (PAS + 2 × PAD) / 3.",
            descEn = "Mean Arterial Pressure perfusion metric: (SBP + 2 × DBP) / 3.",
            descEs = "Presión arterial media de perfusión tisular.",
            minScore = 30.0,
            maxScore = 180.0,
            severeBoundary = 65.0,
            moderateBoundary = 75.0,
            higherIsWorse = false,
            unitDisplay = "mmHg",
            formulaKey = "map_bp",
            inputs = listOf(
                slider("sbp", "Pression systolique (PAS)", "Systolic BP", "PAS", 50.0, 240.0, 120.0, "mmHg"),
                slider("dbp", "Pression diastolique (PAD)", "Diastolic BP", "PAD", 20.0, 140.0, 75.0, "mmHg")
            )
        ),
        mcpScore(
            scoreId = "duke_criteria",
            name = "Critères de Duke Modifiés",
            specialty = cardioMeta,
            descFr = "Critères diagnostiques de l'endocardite infectieuse (majeurs + mineurs).",
            descEn = "Modified Duke Criteria for infective endocarditis diagnosis.",
            descEs = "Criterios de Duke modificados para endocarditis infecciosa.",
            minScore = 0.0,
            maxScore = 11.0,
            severeBoundary = 6.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                toggle("major_bc", "Majeur : Hémocultures typiques positives (+3)", "Major: Positive typical blood cultures (+3)", "Mayor: Hemocultivos (+3)", 3),
                toggle("major_echo", "Majeur : Imagerie endocardique positive (végétation/abcès) (+3)", "Major: Positive echo/imaging (+3)", "Mayor: Ecocardiografía positiva (+3)", 3),
                toggle("predisp", "Mineur : Cardiopathie à risque ou toxicomanie IV (+1)", "Minor: Predisposition (+1)", "Menor: Predisposición (+1)", 1),
                toggle("fever", "Mineur : Fièvre ≥ 38.0°C (+1)", "Minor: Fever ≥ 38.0°C (+1)", "Menor: Fiebre ≥ 38.0°C (+1)", 1),
                toggle("vasc", "Mineur : Phénomènes vasculaires / emboliques (+1)", "Minor: Vascular phenomena (+1)", "Menor: Fenómenos vasculares (+1)", 1),
                toggle("immuno", "Mineur : Phénomènes immunologiques (+1)", "Minor: Immunologic phenomena (+1)", "Menor: Fenómenos inmunológicos (+1)", 1),
                toggle("micro_min", "Mineur : Microbiologie compatible non majeure (+1)", "Minor: Microbiologic evidence (+1)", "Menor: Evidencia microbiológica (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "wells_dvt",
            name = "Score de Wells (TVP)",
            specialty = cardioMeta,
            descFr = "Probabilité clinique de thrombose veineuse profonde des membres inférieurs.",
            descEn = "Clinical probability assessment for deep vein thrombosis (DVT).",
            descEs = "Probabilidad clínica de trombosis venosa profunda (TVP).",
            minScore = -2.0,
            maxScore = 9.0,
            severeBoundary = 3.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("cancer", "Cancer actif (+1)", "Active cancer (+1)", "Cáncer activo (+1)", 1),
                toggle("paralysis", "Paralysie, parésie ou plâtre récent (+1)", "Paralysis/paresis/cast (+1)", "Parálisis o inmovilización (+1)", 1),
                toggle("bedridden", "Alitement ≥ 3j ou chirurgie < 12 sem (+1)", "Bedridden ≥ 3d or major surgery (+1)", "Encamamiento o cirugía (+1)", 1),
                toggle("tenderness", "Douleur sur trajet veineux profond (+1)", "Localized tenderness (+1)", "Dolor trayecto venoso (+1)", 1),
                toggle("swollen_leg", "Œdème de tout le membre (+1)", "Entire leg swollen (+1)", "Edema toda la pierna (+1)", 1),
                toggle("calf_3cm", "Asymétrie mollet > 3 cm (+1)", "Calf swelling > 3 cm (+1)", "Asimetría > 3 cm (+1)", 1),
                toggle("pitting", "Œdème prenant le godet unilatéral (+1)", "Pitting edema (+1)", "Edema con fóvea (+1)", 1),
                toggle("collateral", "Veines superficielles collatérales (+1)", "Collateral superficial veins (+1)", "Venas colaterales (+1)", 1),
                toggle("prev_dvt", "Antécédent documenté de TVP (+1)", "Previously documented DVT (+1)", "TVP previa (+1)", 1),
                toggle("alt_diag", "Diagnostic alternatif au moins aussi probable (-2)", "Alternative diagnosis as likely (-2)", "Diagnóstico alternativo (-2)", -2)
            )
        ),
        mcpScore(
            scoreId = "rcri_lee",
            name = "Score de Lee (RCRI)",
            specialty = cardioMeta,
            descFr = "Risque de complication cardiaque péri-opératoire en chirurgie non cardiaque (0–6).",
            descEn = "Revised Cardiac Risk Index for non-cardiac surgery (0–6).",
            descEs = "Índice de riesgo cardíaco revisado en cirugía no cardíaca (0–6).",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 3.0,
            moderateBoundary = 1.0,
            isCardiac = true,
            inputs = listOf(
                toggle("high_risk_surg", "Chirurgie à haut risque (vasculaire/intrapéritonéale/thoracique)", "High-risk surgery", "Cirugía alto riesgo", 1),
                toggle("ihd", "Antécédent de cardiopathie ischémique", "Ischemic heart disease", "Cardiopatía isquémica", 1),
                toggle("hf", "Antécédent d'insuffisance cardiaque", "Congestive heart failure", "Insuficiencia cardíaca", 1),
                toggle("cvd", "Antécédent d'AVC ou d'AIT", "Cerebrovascular disease", "Enfermedad cerebrovascular", 1),
                toggle("insulin", "Diabète insulinotraité", "Insulin therapy", "Tratamiento con insulina", 1),
                toggle("creat2", "Créatininémie pré-op > 2.0 mg/dL (177 µmol/L)", "Pre-op creatinine > 2.0 mg/dL", "Creatinina > 2.0 mg/dL", 1)
            )
        ),
        mcpScore(
            scoreId = "nyha_class",
            name = "Classification NYHA",
            specialty = cardioMeta,
            descFr = "Limitation fonctionnelle de l'insuffisance cardiaque (Classes I à IV).",
            descEn = "New York Heart Association functional classification of heart failure (I–IV).",
            descEs = "Clasificación funcional NYHA de insuficiencia cardíaca (I–IV).",
            minScore = 1.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "Classe",
            isCardiac = true,
            inputs = listOf(
                choice("nyha", "Tolérance à l'effort", "Exercise tolerance", "Tolerancia al esfuerzo",
                    listOf(
                        opt(1, "Classe I - Aucune limitation"),
                        opt(2, "Classe II - Limitation légère à l'effort ordinaire"),
                        opt(3, "Classe III - Limitation marquée pour effort minime"),
                        opt(4, "Classe IV - Symptômes au repos")
                    ), 1)
            )
        ),
        mcpScore(
            scoreId = "framingham_10y",
            name = "Score de Risque Cardiovasculaire 10 ans",
            specialty = cardioMeta,
            descFr = "Estimation multifactorielle du risque coronarien à 10 ans (%).",
            descEn = "Multivariable 10-year cardiovascular event risk estimation (%).",
            descEs = "Estimación multifactorial de riesgo cardiovascular a 10 años (%).",
            minScore = 1.0,
            maxScore = 50.0,
            severeBoundary = 20.0,
            moderateBoundary = 10.0,
            unitDisplay = "%",
            isCardiac = true,
            formulaKey = "framingham_proxy",
            inputs = listOf(
                slider("age", "Âge (ans)", "Age", "Edad", 30.0, 80.0, 54.0, "ans"),
                slider("sbp", "PAS (mmHg)", "SBP", "PAS", 90.0, 200.0, 130.0, "mmHg"),
                slider("chol", "Cholestérol total (mg/dL)", "Total Cholesterol", "Colesterol total", 130.0, 340.0, 205.0, "mg/dL"),
                slider("hdl", "HDL-C (mg/dL)", "HDL-C", "HDL-C", 25.0, 95.0, 48.0, "mg/dL"),
                toggle("smoker", "Tabagisme actif", "Current smoker", "Tabaquismo activo", 1),
                toggle("diabetes", "Diabète", "Diabetes", "Diabetes", 1),
                toggle("treated_htn", "HTA traitée", "Treated hypertension", "HTA tratada", 1)
            )
        )
    )

    // 10 additional Intensive Care calculators (+ qsofa = 11 total)
    val icuExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "sofa",
            name = "Score SOFA complet (Sepsis-3)",
            specialty = icuMeta,
            descFr = "Défaillance séquentielle de 6 systèmes d'organes en réanimation (0–24).",
            descEn = "Sequential Organ Failure Assessment across 6 organ systems (0–24).",
            descEs = "Evaluación secuencial de fallo orgánico SOFA (0–24).",
            minScore = 0.0,
            maxScore = 24.0,
            severeBoundary = 10.0,
            moderateBoundary = 4.0,
            inputs = listOf(
                choice("resp", "Respiratoire (PaO₂/FiO₂)", "Respiration (PaO2/FiO2)", "Respiración",
                    listOf(opt(0, "0 - ≥ 400"), opt(1, "1 - < 400"), opt(2, "2 - < 300"), opt(3, "3 - < 200 vent."), opt(4, "4 - < 100 vent."))),
                choice("coag", "Coagulation (Plaquettes G/L)", "Coagulation (Platelets)", "Coagulación",
                    listOf(opt(0, "0 - ≥ 150"), opt(1, "1 - < 150"), opt(2, "2 - < 100"), opt(3, "3 - < 50"), opt(4, "4 - < 20"))),
                choice("liver", "Foie (Bilirubine mg/dL)", "Liver (Bilirubin)", "Hígado",
                    listOf(opt(0, "0 - < 1.2"), opt(1, "1 - 1.2–1.9"), opt(2, "2 - 2.0–5.9"), opt(3, "3 - 6.0–11.9"), opt(4, "4 - ≥ 12.0"))),
                choice("cv", "Cardiovasculaire (PAM / Catécholamines)", "Cardiovascular", "Cardiovascular",
                    listOf(opt(0, "0 - PAM ≥ 70"), opt(1, "1 - PAM < 70"), opt(2, "2 - Dopa ≤ 5 / Dobu"), opt(3, "3 - Noradré ≤ 0.1"), opt(4, "4 - Noradré > 0.1"))),
                choice("cns", "Neurologique (GCS)", "CNS (GCS)", "SNC (GCS)",
                    listOf(opt(0, "0 - GCS 15"), opt(1, "1 - 13–14"), opt(2, "2 - 10–12"), opt(3, "3 - 6–9"), opt(4, "4 - < 6"))),
                choice("renal", "Rénal (Créatinine mg/dL)", "Renal (Creatinine)", "Renal",
                    listOf(opt(0, "0 - < 1.2"), opt(1, "1 - 1.2–1.9"), opt(2, "2 - 2.0–3.4"), opt(3, "3 - 3.5–4.9"), opt(4, "4 - ≥ 5.0")))
            )
        ),
        mcpScore(
            scoreId = "apache_ii",
            name = "APACHE II",
            specialty = icuMeta,
            descFr = "Gravité aiguë physiologique et morbidité chronique en soins intensifs (0–71).",
            descEn = "Acute Physiology and Chronic Health Evaluation II mortality score (0–71).",
            descEs = "Escala de gravedad APACHE II en UCI (0–71).",
            minScore = 0.0,
            maxScore = 71.0,
            severeBoundary = 25.0,
            moderateBoundary = 15.0,
            inputs = listOf(
                choice("age_pts", "Âge", "Age", "Edad", listOf(opt(0, "≤ 44 (+0)"), opt(2, "45–54 (+2)"), opt(3, "55–64 (+3)"), opt(5, "65–74 (+5)"), opt(6, "≥ 75 (+6)"))),
                slider("aps", "Points physiologiques aigus (APS)", "Acute Physiology Score (APS)", "Puntos fisiología aguda", 0.0, 40.0, 8.0, "pts"),
                choice("gcs_def", "Déficit Glasgow (15 - GCS)", "GCS deficit (15 - GCS)", "Déficit GCS (15 - GCS)",
                    listOf(opt(0, "GCS 15 (+0)"), opt(2, "GCS 13 (+2)"), opt(5, "GCS 10 (+5)"), opt(9, "GCS 6 (+9)"), opt(12, "GCS 3 (+12)"))),
                choice("chronic", "Pathologie chronique sévère", "Severe organ insufficiency", "Enfermedad crónica grave",
                    listOf(opt(0, "Aucune (+0)"), opt(2, "Post-chirurgie programmée (+2)"), opt(5, "Médical / Urgence post-op (+5)")))
            )
        ),
        mcpScore(
            scoreId = "saps_ii",
            name = "SAPS II (IGS II)",
            specialty = icuMeta,
            descFr = "Indice de gravité simplifié II pour pronostic en réanimation (0–163).",
            descEn = "Simplified Acute Physiology Score II for ICU mortality estimation (0–163).",
            descEs = "Índice de gravedad simplificado SAPS II en UCI (0–163).",
            minScore = 0.0,
            maxScore = 163.0,
            severeBoundary = 50.0,
            moderateBoundary = 30.0,
            inputs = listOf(
                choice("age_s", "Âge", "Age", "Edad", listOf(opt(0, "< 40 (+0)"), opt(7, "40–59 (+7)"), opt(12, "60–69 (+12)"), opt(15, "70–74 (+15)"), opt(18, "≥ 80 (+18)"))),
                choice("hr_s", "Fréquence cardiaque", "Heart rate", "Frecuencia cardíaca", listOf(opt(0, "70–119 (+0)"), opt(4, "120–159 (+4)"), opt(7, "≥ 160 (+7)"), opt(11, "< 40 (+11)"))),
                choice("sbp_s", "Pression systolique", "Systolic BP", "PAS", listOf(opt(0, "100–199 (+0)"), opt(5, "70–99 (+5)"), opt(13, "< 70 (+13)"))),
                choice("gcs_s", "Score de Glasgow", "GCS", "GCS", listOf(opt(0, "14–15 (+0)"), opt(5, "11–13 (+5)"), opt(7, "9–10 (+7)"), opt(13, "6–8 (+13)"), opt(26, "< 6 (+26)"))),
                slider("bio_pts", "Points biologiques (Urée, Na, K, HCO₃, Bili, GB)", "Lab physiological points", "Puntos analíticos", 0.0, 60.0, 6.0, "pts"),
                choice("adm_type", "Type d'admission", "Admission type", "Tipo ingreso", listOf(opt(0, "Chirurgie programmée (+0)"), opt(6, "Médicale (+6)"), opt(8, "Chirurgie urgente (+8)")))
            )
        ),
        mcpScore(
            scoreId = "mods_score",
            name = "Score MODS",
            specialty = icuMeta,
            descFr = "Multiple Organ Dysfunction Score (Marshall) sur 6 défaillances (0–24).",
            descEn = "Multiple Organ Dysfunction Score across 6 organ domains (0–24).",
            descEs = "Puntuación de disfunción multiorgánica MODS (0–24).",
            minScore = 0.0,
            maxScore = 24.0,
            severeBoundary = 13.0,
            moderateBoundary = 5.0,
            inputs = listOf(
                choice("resp", "PaO₂/FiO₂", "PaO2/FiO2", "PaO2/FiO2", listOf(opt(0, "> 300"), opt(1, "226–300"), opt(2, "151–225"), opt(3, "76–150"), opt(4, "≤ 75"))),
                choice("renal", "Créatinine (µmol/L)", "Creatinine", "Creatinina", listOf(opt(0, "≤ 100"), opt(1, "101–200"), opt(2, "201–350"), opt(3, "351–500"), opt(4, "> 500"))),
                choice("hepatic", "Bilirubine (µmol/L)", "Bilirubin", "Bilirrubina", listOf(opt(0, "≤ 20"), opt(1, "21–60"), opt(2, "61–120"), opt(3, "121–240"), opt(4, "> 240"))),
                choice("cv", "PAR (FC × PVC / PAM)", "Pressure-adjusted HR", "FC ajustada presión", listOf(opt(0, "≤ 10"), opt(1, "10.1–15"), opt(2, "15.1–20"), opt(3, "20.1–30"), opt(4, "> 30"))),
                choice("heme", "Plaquettes (G/L)", "Platelets", "Plaquetas", listOf(opt(0, "> 120"), opt(1, "81–120"), opt(2, "51–80"), opt(3, "21–50"), opt(4, "≤ 20"))),
                choice("neuro", "Glasgow", "GCS", "GCS", listOf(opt(0, "15"), opt(1, "13–14"), opt(2, "10–12"), opt(3, "7–9"), opt(4, "≤ 6")))
            )
        ),
        mcpScore(
            scoreId = "rass",
            name = "Échelle RASS (Sédation-Agitation)",
            specialty = icuMeta,
            descFr = "Richmond Agitation-Sedation Scale pour titration de sédation en réanimation (-5 à +4).",
            descEn = "Richmond Agitation-Sedation Scale (-5 to +4).",
            descEs = "Escala de agitación-sedación RASS (-5 a +4).",
            minScore = -5.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 1.0,
            unitDisplay = "RASS",
            formulaKey = "rass_abs",
            inputs = listOf(
                choice("rass_level", "Niveau observé", "Observed state", "Estado observado",
                    listOf(
                        opt(4, "+4 - Combatif, violent"),
                        opt(3, "+3 - Très agité, arrache sondes"),
                        opt(2, "+2 - Agité, mouvements fréquents"),
                        opt(1, "+1 - Anxieux sans agressivité"),
                        opt(0, "0 - Alerte et calme (Cible)"),
                        opt(-1, "-1 - Somnolent, éveil > 10s"),
                        opt(-2, "-2 - Sédation légère, contact < 10s"),
                        opt(-3, "-3 - Sédation modérée, mouvement à la voix"),
                        opt(-4, "-4 - Sédation profonde, réaction douleur"),
                        opt(-5, "-5 - Non réveillable")
                    ), 0)
            )
        ),
        mcpScore(
            scoreId = "cam_icu",
            name = "Score CAM-ICU (Délire)",
            specialty = icuMeta,
            descFr = "Dépistage du delirium en réanimation selon les 4 critères CAM-ICU.",
            descEn = "Confusion Assessment Method for the ICU delirium screening.",
            descEs = "Detección de delirio en UCI (CAM-ICU).",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("acute_onset", "1. Début brutal ou évolution fluctuante", "1. Acute onset or fluctuating course", "1. Inicio agudo o fluctuante", 1),
                toggle("inattention", "2. Inattention (≥ 3 erreurs ASE)", "2. Inattention", "2. Inatención", 1),
                toggle("rass_altered", "3. Niveau de conscience altéré (RASS ≠ 0)", "3. Altered level of consciousness", "3. Nivel conciencia alterado", 1),
                toggle("disorganized", "4. Pensée désorganisée", "4. Disorganized thinking", "4. Pensamiento desorganizado", 1)
            )
        ),
        mcpScore(
            scoreId = "murray_ards",
            name = "Score de Murray (SDRA)",
            specialty = icuMeta,
            descFr = "Lung Injury Score quantifiant la sévérité d'un SDRA (0 à 4).",
            descEn = "Murray Lung Injury Score for ARDS severity (0–4).",
            descEs = "Puntuación de lesión pulmonar de Murray (0–4).",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 2.5,
            moderateBoundary = 1.0,
            formulaKey = "murray_avg",
            inputs = listOf(
                choice("cxr", "Radiographie thoracique (quadrants)", "Chest X-ray quadrants", "Radiografía cuadrantes",
                    listOf(opt(0, "0 - Normale"), opt(1, "1 - 1 quadrant"), opt(2, "2 - 2 quadrants"), opt(3, "3 - 3 quadrants"), opt(4, "4 - 4 quadrants"))),
                choice("pf", "PaO₂/FiO₂", "PaO2/FiO2", "PaO2/FiO2",
                    listOf(opt(0, "0 - ≥ 300"), opt(1, "1 - 225–299"), opt(2, "2 - 175–224"), opt(3, "3 - 100–174"), opt(4, "4 - < 100"))),
                choice("peep", "PEP (cmH₂O)", "PEEP (cmH2O)", "PEEP",
                    listOf(opt(0, "0 - ≤ 5"), opt(1, "1 - 6–8"), opt(2, "2 - 9–11"), opt(3, "3 - 12–14"), opt(4, "4 - ≥ 15"))),
                choice("comp", "Compliance respiratoire", "Compliance", "Distensibilidad",
                    listOf(opt(0, "0 - ≥ 80"), opt(1, "1 - 60–79"), opt(2, "2 - 40–59"), opt(3, "3 - 20–39"), opt(4, "4 - ≤ 19")))
            )
        ),
        mcpScore(
            scoreId = "pf_ratio",
            name = "Rapport PaO₂ / FiO₂ (Berlin SDRA)",
            specialty = icuMeta,
            descFr = "Index d'oxygénation artérielle selon les critères de Berlin pour le SDRA.",
            descEn = "PaO2/FiO2 oxygenation ratio per Berlin ARDS classification.",
            descEs = "Cociente PaO2/FiO2 según criterios de Berlín para SDRA.",
            minScore = 30.0,
            maxScore = 600.0,
            severeBoundary = 100.0,
            moderateBoundary = 200.0,
            higherIsWorse = false,
            unitDisplay = "mmHg",
            formulaKey = "pf_ratio",
            inputs = listOf(
                slider("pao2", "PaO₂ artérielle (mmHg)", "Arterial PaO2 (mmHg)", "PaO2 (mmHg)", 35.0, 400.0, 90.0, "mmHg"),
                slider("fio2", "FiO₂ (%)", "FiO2 (%)", "FiO2 (%)", 21.0, 100.0, 40.0, "%")
            )
        ),
        mcpScore(
            scoreId = "shock_index",
            name = "Index de Choc (FC / PAS)",
            specialty = icuMeta,
            descFr = "Dépistage précoce de choc hypovolémique ou septique (FC / PAS).",
            descEn = "Hemodynamic Shock Index (Heart Rate / Systolic BP).",
            descEs = "Índice de choque hemodinámico (FC / PAS).",
            minScore = 0.2,
            maxScore = 3.0,
            severeBoundary = 1.0,
            moderateBoundary = 0.8,
            unitDisplay = "ratio",
            formulaKey = "shock_index",
            inputs = listOf(
                slider("hr", "Fréquence cardiaque (bpm)", "Heart rate (bpm)", "FC (lpm)", 40.0, 190.0, 95.0, "bpm"),
                slider("sbp", "PAS (mmHg)", "SBP (mmHg)", "PAS (mmHg)", 50.0, 220.0, 110.0, "mmHg")
            )
        ),
        mcpScore(
            scoreId = "news2",
            name = "Score NEWS2 (Alerte Précoce)",
            specialty = icuMeta,
            descFr = "National Early Warning Score 2 pour détection de détérioration clinique (0–20).",
            descEn = "National Early Warning Score 2 for acute illness severity (0–20).",
            descEs = "Escala de alerta temprana NEWS2 (0–20).",
            minScore = 0.0,
            maxScore = 20.0,
            severeBoundary = 7.0,
            moderateBoundary = 5.0,
            inputs = listOf(
                choice("rr", "Fréquence respiratoire", "Respiratory rate", "FR", listOf(opt(0, "12–20 (+0)"), opt(1, "9–11 (+1)"), opt(2, "21–24 (+2)"), opt(3, "≤ 8 ou ≥ 25 (+3)"))),
                choice("spo2", "SpO₂", "SpO2", "SpO2", listOf(opt(0, "≥ 96% (+0)"), opt(1, "94–95% (+1)"), opt(2, "92–93% (+2)"), opt(3, "≤ 91% (+3)"))),
                toggle("o2", "Oxygénothérapie supplémentaire (+2)", "Supplemental O2 (+2)", "Oxígeno suplementario (+2)", 2),
                choice("sbp", "PAS", "Systolic BP", "PAS", listOf(opt(0, "111–219 (+0)"), opt(1, "101–110 (+1)"), opt(2, "91–100 (+2)"), opt(3, "≤ 90 ou ≥ 220 (+3)"))),
                choice("hr", "Pouls", "Pulse", "Pulso", listOf(opt(0, "51–90 (+0)"), opt(1, "41–50 ou 91–110 (+1)"), opt(2, "111–130 (+2)"), opt(3, "≤ 40 ou ≥ 131 (+3)"))),
                choice("neuro", "Vigilance (ACVPU)", "Consciousness", "Conciencia", listOf(opt(0, "Alerte (+0)"), opt(3, "C/V/P/U (+3)"))),
                choice("temp", "Température", "Temperature", "Temperatura", listOf(opt(0, "36.1–38.0°C (+0)"), opt(1, "35.1–36.0 ou 38.1–39.0 (+1)"), opt(2, "≥ 39.1 (+2)"), opt(3, "≤ 35.0 (+3)")))
            )
        )
    )
}
