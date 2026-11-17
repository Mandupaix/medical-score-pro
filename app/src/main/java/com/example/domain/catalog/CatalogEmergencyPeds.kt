package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogEmergencyPeds {

    val emergencyMeta = SpecialtyMeta("emergency", "Médecine d'Urgence", "Emergency Medicine", "Urgencias")
    val pedsMeta = SpecialtyMeta("pediatrics", "Pédiatrie", "Pediatrics", "Pediatría")
    val neoMeta = SpecialtyMeta("neonatology", "Néonatologie", "Neonatology", "Neonatología")

    // 10 additional Emergency Medicine calculators (+ heart_score = 11 total)
    val emergencyExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "wells_pe",
            name = "Score de Wells (Embolie Pulmonaire)",
            specialty = emergencyMeta,
            descFr = "Probabilité clinique pré-test d'embolie pulmonaire aiguë.",
            descEn = "Pre-test probability scoring for acute pulmonary embolism (PE).",
            descEs = "Probabilidad clínica de embolia pulmonar (Wells).",
            minScore = 0.0,
            maxScore = 12.5,
            severeBoundary = 6.5,
            moderateBoundary = 2.0,
            formulaKey = "wells_pe",
            inputs = listOf(
                toggle("dvt_signs", "Signes cliniques de TVP (+3)", "Clinical signs of DVT (+3)", "Signos clínicos TVP (+3)", 3),
                toggle("pe_likely", "EP diagnostic le plus probable (+3)", "PE is #1 diagnosis (+3)", "EP diagnóstico más probable (+3)", 3),
                toggle("hr100", "FC > 100 bpm (+1.5)", "HR > 100 bpm (+1.5)", "FC > 100 lpm (+1.5)", 15),
                toggle("immob", "Immobilisation ≥ 3j ou chirurgie < 4 sem (+1.5)", "Immobilization / surgery (+1.5)", "Inmovilización / cirugía (+1.5)", 15),
                toggle("prev_vte", "Antécédent EP / TVP (+1.5)", "Prior PE / DVT (+1.5)", "TEP / TVP previa (+1.5)", 15),
                toggle("hemoptysis", "Hémoptysie (+1)", "Hemoptysis (+1)", "Hemoptisis (+1)", 1),
                toggle("cancer", "Cancer actif ou < 6 mois (+1)", "Active malignancy (+1)", "Cáncer activo (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "perc_rule",
            name = "Règle PERC (Exclusion EP)",
            specialty = emergencyMeta,
            descFr = "8 critères permettant d'écarter l'EP sans D-dimères si score = 0 (faible probabilité).",
            descEn = "Pulmonary Embolism Rule-out Criteria (0 = PE ruled out in low-risk).",
            descEs = "Criterios PERC para descartar embolia pulmonar (0 = descartada).",
            minScore = 0.0,
            maxScore = 8.0,
            severeBoundary = 3.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("age50", "Âge ≥ 50 ans", "Age ≥ 50", "Edad ≥ 50", 1),
                toggle("hr100", "FC ≥ 100 bpm", "HR ≥ 100 bpm", "FC ≥ 100", 1),
                toggle("spo2", "SpO₂ < 95%", "SpO2 < 95%", "SpO2 < 95%", 1),
                toggle("leg_swelling", "Œdème unilatéral de jambe", "Unilateral leg swelling", "Edema unilateral pierna", 1),
                toggle("hemoptysis", "Hémoptysie", "Hemoptysis", "Hemoptisis", 1),
                toggle("surgery", "Chirurgie ou traumatisme < 4 sem", "Recent surgery/trauma", "Cirugía o trauma reciente", 1),
                toggle("prev_vte", "Antécédent TVP/EP", "Prior DVT/PE", "TVP/TEP previo", 1),
                toggle("estrogen", "Œstrogénothérapie orale", "Hormone use", "Uso de estrógenos", 1)
            )
        ),
        mcpScore(
            scoreId = "revised_geneva",
            name = "Score de Genève Révisé (EP)",
            specialty = emergencyMeta,
            descFr = "Score objectif de probabilité clinique d'embolie pulmonaire (0–22).",
            descEn = "Revised Geneva score for pulmonary embolism probability (0–22).",
            descEs = "Puntuación de Ginebra revisada para embolia pulmonar (0–22).",
            minScore = 0.0,
            maxScore = 22.0,
            severeBoundary = 11.0,
            moderateBoundary = 4.0,
            inputs = listOf(
                toggle("age65", "Âge > 65 ans (+1)", "Age > 65 (+1)", "Edad > 65 (+1)", 1),
                toggle("prev_vte", "Antécédent TVP/EP (+3)", "Prior DVT/PE (+3)", "TVP/TEP previo (+3)", 3),
                toggle("surg_fx", "Chirurgie ou fracture < 1 mois (+2)", "Surgery/fracture < 1 mo (+2)", "Cirugía o fractura (+2)", 2),
                toggle("cancer", "Cancer actif (+2)", "Active malignancy (+2)", "Cáncer activo (+2)", 2),
                toggle("leg_pain", "Douleur unilatérale jambe (+3)", "Unilateral leg pain (+3)", "Dolor unilateral pierna (+3)", 3),
                toggle("hemoptysis", "Hémoptysie (+2)", "Hemoptysis (+2)", "Hemoptisis (+2)", 2),
                choice("hr", "Fréquence cardiaque", "Heart rate", "FC", listOf(opt(0, "< 75 (+0)"), opt(3, "75–94 (+3)"), opt(5, "≥ 95 (+5)"))),
                toggle("dvt_palp", "Douleur trajet veineux + œdème unilatéral (+4)", "Pain on palpation + edema (+4)", "Dolor palpación + edema (+4)", 4)
            )
        ),
        mcpScore(
            scoreId = "ottawa_ankle",
            name = "Critères d'Ottawa (Cheville & Pied)",
            specialty = emergencyMeta,
            descFr = "Indication de radiographie devant un traumatisme aigu de cheville ou médio-pied.",
            descEn = "Ottawa Ankle Rules determining need for X-ray in acute ankle/midfoot trauma.",
            descEs = "Reglas de Ottawa para indicación de radiografía de tobillo/pie.",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("malleolus_lat", "Douleur bord postérieur/pointe malléole latérale", "Posterior lateral malleolus tenderness", "Dolor maléolo lateral", 1),
                toggle("malleolus_med", "Douleur bord postérieur/pointe malléole médiale", "Posterior medial malleolus tenderness", "Dolor maléolo medial", 1),
                toggle("navicular", "Douleur os naviculaire (médio-pied)", "Navicular tenderness", "Dolor escafoides", 1),
                toggle("fifth_mt", "Douleur base du 5e métatarsien", "Base of 5th metatarsal tenderness", "Base 5º metatarsiano", 1),
                toggle("weight_bear", "Incapacité à faire 4 pas immédiatement et aux urgences", "Inability to bear weight (4 steps)", "Incapacidad apoyo 4 pasos", 1)
            )
        ),
        mcpScore(
            scoreId = "canadian_cspine",
            name = "Canadian C-Spine Rule",
            specialty = emergencyMeta,
            descFr = "Évaluation du besoin d'imagerie cervicale après traumatisme chez patient alerte.",
            descEn = "Canadian C-Spine Rule for cervical spine radiography after trauma.",
            descEs = "Regla canadiense de columna cervical en trauma.",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("age65", "Haut risque : Âge ≥ 65 ans", "High risk: Age ≥ 65", "Edad ≥ 65", 1),
                toggle("mech", "Haut risque : Mécanisme dangereux", "High risk: Dangerous mechanism", "Mecanismo peligroso", 1),
                toggle("paresthesia", "Haut risque : Paresthésies des membres", "High risk: Paresthesias", "Parestesias", 1),
                toggle("no_rot", "Incapacité de rotation active du cou à 45° G/D", "Unable to rotate neck 45°", "Incapaz rotar cuello 45°", 1),
                toggle("midline_pain", "Douleur médiane à la palpation", "Midline tenderness", "Dolor línea media", 1)
            )
        ),
        mcpScore(
            scoreId = "canadian_ct_head",
            name = "Canadian CT Head Rule",
            specialty = emergencyMeta,
            descFr = "Indication du scanner cérébral devant un traumatisme crânien léger (GCS 13–15).",
            descEn = "Canadian CT Head Rule for minor head injury (GCS 13–15).",
            descEs = "Regla canadiense de TC craneal en trauma leve.",
            minScore = 0.0,
            maxScore = 7.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("gcs15", "GCS < 15 à 2h post-traumatisme", "GCS < 15 at 2h", "GCS < 15 a las 2h", 1),
                toggle("open_fx", "Suspicion fracture ouverte ou embarrure", "Suspected open/depressed skull fx", "Sospecha fractura abierta", 1),
                toggle("basal_fx", "Signe de fracture base du crâne", "Basal skull fracture sign", "Signos fractura base", 1),
                toggle("vomit", "≥ 2 épisodes de vomissements", "≥ 2 episodes of vomiting", "≥ 2 vómitos", 1),
                toggle("age65", "Âge ≥ 65 ans", "Age ≥ 65", "Edad ≥ 65", 1),
                toggle("amnesia", "Amnésie rétrograde > 30 min", "Retrograde amnesia > 30 min", "Amnesia retrógrada > 30 min", 1),
                toggle("mech", "Mécanisme dangereux (piéton, éjection, chute > 1m)", "Dangerous mechanism", "Mecanismo peligroso", 1)
            )
        ),
        mcpScore(
            scoreId = "nexus_cspine",
            name = "Critères NEXUS (Rachis Cervical)",
            specialty = emergencyMeta,
            descFr = "5 critères cliniques d'exclusion d'imagerie cervicale après traumatisme (0 = imagerie inutile).",
            descEn = "NEXUS Criteria for C-spine clearance (0 = no imaging required).",
            descEs = "Criterios NEXUS para columna cervical.",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("midline", "Douleur cervicale postérieure médiane", "Posterior midline tenderness", "Dolor línea media posterior", 1),
                toggle("neuro", "Déficit neurologique focal", "Focal neurologic deficit", "Déficit neurológico focal", 1),
                toggle("alert", "Altération de la vigilance", "Altered level of alertness", "Alteración conciencia", 1),
                toggle("intox", "Intoxication aiguë", "Evidence of intoxication", "Intoxicación", 1),
                toggle("distract", "Douleur distrayante majeure", "Painful distracting injury", "Lesión distractora", 1)
            )
        ),
        mcpScore(
            scoreId = "glasgow_blatchford",
            name = "Glasgow-Blatchford (Hémorragie Digestive)",
            specialty = emergencyMeta,
            descFr = "Stratification d'urgence des hémorragies digestives hautes avant endoscopie (0–23).",
            descEn = "Upper GI bleeding triage score for urgent endoscopic intervention (0–23).",
            descEs = "Puntuación Glasgow-Blatchford en hemorragia digestiva alta (0–23).",
            minScore = 0.0,
            maxScore = 23.0,
            severeBoundary = 7.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("bun", "Urée sanguine (mmol/L)", "BUN (mmol/L)", "Urea (mmol/L)",
                    listOf(opt(0, "< 6.5 (+0)"), opt(2, "6.5–7.9 (+2)"), opt(3, "8.0–9.9 (+3)"), opt(4, "10.0–24.9 (+4)"), opt(6, "≥ 25.0 (+6)"))),
                choice("hb", "Hémoglobine (g/dL)", "Hemoglobin (g/dL)", "Hemoglobina",
                    listOf(opt(0, "≥ 13 H / ≥ 12 F (+0)"), opt(1, "12–12.9 H / 10–11.9 F (+1)"), opt(3, "10–11.9 H (+3)"), opt(6, "< 10 (+6)"))),
                choice("sbp", "PAS (mmHg)", "SBP (mmHg)", "PAS",
                    listOf(opt(0, "≥ 110 (+0)"), opt(1, "100–109 (+1)"), opt(2, "90–99 (+2)"), opt(3, "< 90 (+3)"))),
                toggle("hr100", "FC ≥ 100 bpm (+1)", "Pulse ≥ 100 (+1)", "FC ≥ 100 (+1)", 1),
                toggle("melena", "Méléna (+1)", "Melena (+1)", "Melena (+1)", 1),
                toggle("syncope", "Syncope (+2)", "Syncope (+2)", "Síncope (+2)", 2),
                toggle("hepatic", "Hépatopathie (+2)", "Hepatic disease (+2)", "Hepatopatía (+2)", 2),
                toggle("cardiac", "Insuffisance cardiaque (+2)", "Cardiac failure (+2)", "Insuficiencia cardíaca (+2)", 2)
            )
        ),
        mcpScore(
            scoreId = "pesi_score",
            name = "Score sPESI (Embolie Pulmonaire)",
            specialty = emergencyMeta,
            descFr = "Simplified PESI estimant la mortalité à 30 jours dans l'EP (0–6).",
            descEn = "Simplified Pulmonary Embolism Severity Index for 30-day mortality (0–6).",
            descEs = "Índice PESI simplificado en embolia pulmonar (0–6).",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("age80", "Âge > 80 ans", "Age > 80", "Edad > 80", 1),
                toggle("cancer", "Antécédent de cancer", "History of cancer", "Antecedente cáncer", 1),
                toggle("cardiopulm", "Insuffisance cardiaque ou BPCO chronique", "Chronic cardiopulmonary disease", "Enfermedad cardiopulmonar", 1),
                toggle("hr110", "FC ≥ 110 bpm", "HR ≥ 110 bpm", "FC ≥ 110", 1),
                toggle("sbp100", "PAS < 100 mmHg", "SBP < 100 mmHg", "PAS < 100", 1),
                toggle("spo2_90", "SpO₂ < 90%", "SaO2 < 90%", "SatO2 < 90%", 1)
            )
        ),
        mcpScore(
            scoreId = "san_francisco_syncope",
            name = "Règle de San Francisco (Syncope)",
            specialty = emergencyMeta,
            descFr = "Dépistage des syncopes à risque de complication sévère à 7 jours (CHESS).",
            descEn = "San Francisco Syncope Rule (CHESS) for short-term serious outcome.",
            descEs = "Regla de San Francisco para síncope.",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("chf", "C - Insuffisance cardiaque", "CHF history", "Insuficiencia cardíaca", 1),
                toggle("hct", "H - Hématocrite < 30%", "Hematocrit < 30%", "Hematocrito < 30%", 1),
                toggle("ecg", "E - ECG anormal", "Abnormal ECG", "ECG anormal", 1),
                toggle("sob", "S - Dyspnée", "Shortness of breath", "Disnea", 1),
                toggle("sbp90", "S - PAS triage < 90 mmHg", "SBP < 90 mmHg", "PAS < 90 mmHg", 1)
            )
        )
    )

    // 7 additional Pediatrics calculators (+ apgar = 8 total)
    val pediatricsExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "pews",
            name = "Score PEWS (Alerte Pédiatrique)",
            specialty = pedsMeta,
            descFr = "Pediatric Early Warning Score détectant précocement l'aggravation en pédiatrie (0–9).",
            descEn = "Pediatric Early Warning Score for clinical deterioration (0–9).",
            descEs = "Escala de alerta temprana pediátrica PEWS (0–9).",
            minScore = 0.0,
            maxScore = 9.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("behav", "Comportement", "Behavior", "Comportamiento",
                    listOf(opt(0, "0 - Joue / adapté"), opt(1, "1 - Somnolent"), opt(2, "2 - Irritable"), opt(3, "3 - Léthargique / confus"))),
                choice("cv", "Cardiovasculaire (TRC / FC)", "Cardiovascular", "Cardiovascular",
                    listOf(opt(0, "0 - Rose, TRC 1–2s"), opt(1, "1 - Pâle, TRC 3s"), opt(2, "2 - Gris, TRC 4s, tachycardie"), opt(3, "3 - Marbrures, TRC ≥ 5s"))),
                choice("resp", "Respiratoire", "Respiratory", "Respiratorio",
                    listOf(opt(0, "0 - Normal"), opt(1, "1 - Polpynée > +10/min"), opt(2, "2 - Tirage, FiO₂ > 40%"), opt(3, "3 - Épuisement, geignement")))
            )
        ),
        mcpScore(
            scoreId = "westley_croup",
            name = "Score de Westley (Laryngite / Croup)",
            specialty = pedsMeta,
            descFr = "Gravité de la laryngite aiguë sous-glottique de l'enfant (0–17).",
            descEn = "Westley Croup Score for pediatric laryngotracheobronchitis severity (0–17).",
            descEs = "Escala de Westley para crup pediátrico (0–17).",
            minScore = 0.0,
            maxScore = 17.0,
            severeBoundary = 8.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("stridor", "Stridor inspiratoire", "Inspiratory stridor", "Estridor",
                    listOf(opt(0, "0 - Absent"), opt(1, "1 - À l'agitation"), opt(2, "2 - Au repos"))),
                choice("retraction", "Tirage", "Retractions", "Tiraje",
                    listOf(opt(0, "0 - Absent"), opt(1, "1 - Léger"), opt(2, "2 - Modéré"), opt(3, "3 - Sévère"))),
                choice("air_entry", "Murmure vésiculaire", "Air entry", "Entrada de aire",
                    listOf(opt(0, "0 - Normal"), opt(1, "1 - Diminué"), opt(2, "2 - Très diminué"))),
                choice("cyanosis", "Cyanose", "Cyanosis", "Cianosis",
                    listOf(opt(0, "0 - Absente"), opt(4, "4 - À l'agitation"), opt(5, "5 - Au repos"))),
                choice("loc", "Conscience", "Level of consciousness", "Conciencia",
                    listOf(opt(0, "0 - Normale"), opt(5, "5 - Désorienté / Léthargique")))
            )
        ),
        mcpScore(
            scoreId = "pediatric_gcs",
            name = "Glasgow Pédiatrique",
            specialty = pedsMeta,
            descFr = "Adaptation de l'échelle de Glasgow au nourrisson et jeune enfant (3–15).",
            descEn = "Pediatric Glasgow Coma Scale adapted for pre-verbal children (3–15).",
            descEs = "Escala de coma de Glasgow pediátrica (3–15).",
            minScore = 3.0,
            maxScore = 15.0,
            severeBoundary = 8.0,
            moderateBoundary = 12.0,
            higherIsWorse = false,
            inputs = listOf(
                choice("eye", "Ouverture des yeux", "Eye opening", "Apertura ocular",
                    listOf(opt(4, "4 - Spontanée"), opt(3, "3 - À la parole"), opt(2, "2 - À la douleur"), opt(1, "1 - Aucune")), 4),
                choice("verbal", "Réponse verbale / Cris", "Verbal / Cry response", "Respuesta verbal",
                    listOf(opt(5, "5 - Babille / mots adaptés"), opt(4, "4 - Pleurs consolables"), opt(3, "3 - Pleurs inconsolables"), opt(2, "2 - Gémissements"), opt(1, "1 - Aucune")), 5),
                choice("motor", "Réponse motrice", "Motor response", "Respuesta motora",
                    listOf(opt(6, "6 - Mouvements spontanés adaptés"), opt(5, "5 - Retrait au toucher"), opt(4, "4 - Retrait à la douleur"), opt(3, "3 - Flexion anormale"), opt(2, "2 - Extension"), opt(1, "1 - Aucune")), 6)
            )
        ),
        mcpScore(
            scoreId = "pas_appendicitis",
            name = "Score PAS (Appendicite Pédiatrique)",
            specialty = pedsMeta,
            descFr = "Pediatric Appendicitis Score (Samuel) pour trier la douleur abdominale aiguë (0–10).",
            descEn = "Pediatric Appendicitis Score for acute abdominal pain triage (0–10).",
            descEs = "Puntuación PAS de apendicitis pediátrica (0–10).",
            minScore = 0.0,
            maxScore = 10.0,
            severeBoundary = 7.0,
            moderateBoundary = 4.0,
            inputs = listOf(
                toggle("cough_pain", "Douleur FID à la toux / percussion (+2)", "Cough/percussion tenderness RLQ (+2)", "Dolor FID al toser (+2)", 2),
                toggle("anorexia", "Anorexie (+1)", "Anorexia (+1)", "Anorexia (+1)", 1),
                toggle("fever", "Fièvre > 38°C (+1)", "Pyrexia (+1)", "Fiebre (+1)", 1),
                toggle("nausea", "Nausées / Vomissements (+1)", "Nausea/emesis (+1)", "Náuseas/vómitos (+1)", 1),
                toggle("rlq_tend", "Sensibilité palpation FID (+2)", "RLQ tenderness (+2)", "Dolor palpación FID (+2)", 2),
                toggle("wbc", "Leucocytes > 10 000/mm³ (+1)", "WBC > 10,000 (+1)", "Leucocitos > 10,000 (+1)", 1),
                toggle("neutro", "Polynucléaires neutrophiles > 75% (+1)", "Neutrophilia > 75% (+1)", "Neutrofilia > 75% (+1)", 1),
                toggle("migration", "Migration douleur en FID (+1)", "Migration of pain to RLQ (+1)", "Migración del dolor (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "pecarn_head",
            name = "PECARN (Trauma Crânien Enfant)",
            specialty = pedsMeta,
            descFr = "Identification des enfants à risque d'atteinte intracrânienne cliniquement importante.",
            descEn = "PECARN Pediatric Head Injury CT decision rule.",
            descEs = "Regla PECARN en traumatismo craneal pediátrico.",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("gcs_alt", "GCS ≤ 14 ou altération conscience", "GCS ≤ 14 or altered mental status", "GCS ≤ 14 o alteración mental", 2),
                toggle("skull_fx", "Signes fracture crâne ou hématome pariétal/occipital", "Palpable skull fracture / non-frontal hematoma", "Fractura palpable o hematoma", 1),
                toggle("loc_5s", "Perte de connaissance ≥ 5 secondes", "Loss of consciousness ≥ 5s", "Pérdida conciencia ≥ 5s", 1),
                toggle("vomit", "Vomissements répétés", "Vomiting", "Vómitos", 1),
                toggle("severe_mech", "Mécanisme sévère", "Severe injury mechanism", "Mecanismo severo", 1)
            )
        ),
        mcpScore(
            scoreId = "centor_peds",
            name = "Score de McIsaac (Angine Pédiatrique)",
            specialty = pedsMeta,
            descFr = "Probabilité d'angine à streptocoque du groupe A ajustée à l'âge (0–5).",
            descEn = "Modified Centor (McIsaac) strep pharyngitis score (0–5).",
            descEs = "Escala McIsaac para faringitis estreptocócica (0–5).",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("fever", "Fièvre > 38°C (+1)", "Temp > 38°C (+1)", "Fiebre > 38°C (+1)", 1),
                toggle("no_cough", "Absence de toux (+1)", "Absence of cough (+1)", "Ausencia de tos (+1)", 1),
                toggle("nodes", "Adénopathies cervicales antérieures douloureuses (+1)", "Tender anterior cervical nodes (+1)", "Adenopatías anteriores (+1)", 1),
                toggle("exudate", "Exsudat ou hypertrophie amygdalienne (+1)", "Tonsillar swelling/exudate (+1)", "Exudado amigdalar (+1)", 1),
                choice("age_grp", "Âge", "Age group", "Edad", listOf(opt(1, "3–14 ans (+1)"), opt(0, "15–44 ans (+0)"), opt(-1, "≥ 45 ans (-1)")))
            )
        ),
        mcpScore(
            scoreId = "flacc_pain",
            name = "Échelle FLACC (Douleur Pédiatrique)",
            specialty = pedsMeta,
            descFr = "Évaluation comportementale de la douleur chez l'enfant de 2 mois à 7 ans (0–10).",
            descEn = "Face, Legs, Activity, Cry, Consolability pediatric pain score (0–10).",
            descEs = "Escala de dolor pediátrica FLACC (0–10).",
            minScore = 0.0,
            maxScore = 10.0,
            severeBoundary = 7.0,
            moderateBoundary = 4.0,
            inputs = listOf(
                choice("face", "Visage (Face)", "Face", "Cara", listOf(opt(0, "0 - Détendu"), opt(1, "1 - Grimace occasionnelle"), opt(2, "2 - Mâchoire serrée"))),
                choice("legs", "Jambes (Legs)", "Legs", "Piernas", listOf(opt(0, "0 - Normales"), opt(1, "1 - Agitées"), opt(2, "2 - Coups de pied/repli"))),
                choice("activity", "Activité (Activity)", "Activity", "Actividad", listOf(opt(0, "0 - Calme"), opt(1, "1 - Se tortille"), opt(2, "2 - Arqué/rigide"))),
                choice("cry", "Pleurs (Cry)", "Cry", "Llanto", listOf(opt(0, "0 - Aucun"), opt(1, "1 - Gémissements"), opt(2, "2 - Pleurs continus"))),
                choice("consol", "Consolabilité", "Consolability", "Consolabilidad", listOf(opt(0, "0 - Content"), opt(1, "1 - Rassuré au toucher"), opt(2, "2 - Inconsolable")))
            )
        )
    )

    // 3 Neonatology calculators (3 total)
    val neonatologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "silverman_andersen",
            name = "Score de Silverman-Andersen",
            specialty = neoMeta,
            descFr = "Évaluation clinique de la détresse respiratoire du nouveau-né (0–10).",
            descEn = "Neonatal respiratory distress clinical severity index (0–10).",
            descEs = "Índice de dificultad respiratoria neonatal de Silverman (0–10).",
            minScore = 0.0,
            maxScore = 10.0,
            severeBoundary = 6.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("chest", "Balancement thoraco-abdominal", "Chest movement", "Disociación toracoabdominal",
                    listOf(opt(0, "0 - Synchrone"), opt(1, "1 - Thorax immobile"), opt(2, "2 - Balancement"))),
                choice("intercostal", "Tirage intercostal", "Intercostal retractions", "Tiraje intercostal",
                    listOf(opt(0, "0 - Absent"), opt(1, "1 - Modéré"), opt(2, "2 - Marqué"))),
                choice("xiphoid", "Entonnoir xiphoïdien", "Xiphoid retraction", "Retracción xifoidea",
                    listOf(opt(0, "0 - Absent"), opt(1, "1 - Modéré"), opt(2, "2 - Marqué"))),
                choice("nares", "Battement des ailes du nez", "Nasal flaring", "Aleteo nasal",
                    listOf(opt(0, "0 - Absent"), opt(1, "1 - Modéré"), opt(2, "2 - Marqué"))),
                choice("grunt", "Geignement expiratoire", "Expiratory grunt", "Quejido espiratorio",
                    listOf(opt(0, "0 - Absent"), opt(1, "1 - Au stéthoscope"), opt(2, "2 - Audible à distance")))
            )
        ),
        mcpScore(
            scoreId = "ballard_maturity",
            name = "Score de Ballard (Maturité)",
            specialty = neoMeta,
            descFr = "Estimation de l'âge gestationnel néonatal par critères neuromusculaires et physiques (-10 à 50).",
            descEn = "New Ballard Score for neonatal gestational age maturity assessment.",
            descEs = "Puntuación de Ballard de madurez neonatal.",
            minScore = -10.0,
            maxScore = 50.0,
            severeBoundary = 15.0,
            moderateBoundary = 30.0,
            higherIsWorse = false,
            inputs = listOf(
                slider("neuromusc", "Score neuromusculaire (6 items)", "Neuromuscular score", "Puntuación neuromuscular", -5.0, 25.0, 15.0, "pts"),
                slider("physical", "Score maturité physique (6 items)", "Physical maturity score", "Madurez física", -5.0, 25.0, 15.0, "pts")
            )
        ),
        mcpScore(
            scoreId = "snappe_ii",
            name = "Score SNAPPE-II (Néonatalogie)",
            specialty = neoMeta,
            descFr = "Score de gravité physiologique et de risque néonatal en USI néonatale (0–162).",
            descEn = "Score for Neonatal Acute Physiology Perinatal Extension II.",
            descEs = "Escala de gravedad neonatal SNAPPE-II.",
            minScore = 0.0,
            maxScore = 162.0,
            severeBoundary = 40.0,
            moderateBoundary = 20.0,
            inputs = listOf(
                choice("mbp", "PAM la plus basse", "Lowest mean BP", "PAM mínima", listOf(opt(0, "≥ 30 mmHg"), opt(9, "20–29 mmHg (+9)"), opt(19, "< 20 mmHg (+19)"))),
                choice("temp", "Température la plus basse", "Lowest temp", "Temperatura mínima", listOf(opt(0, "> 35.6°C"), opt(8, "35.0–35.6°C (+8)"), opt(15, "< 35.0°C (+15)"))),
                choice("pf", "Rapport PaO₂/FiO₂", "PO2/FiO2 ratio", "PO2/FiO2", listOf(opt(0, "> 2.49"), opt(5, "1.0–2.49 (+5)"), opt(16, "0.3–0.99 (+16)"), opt(28, "< 0.3 (+28)"))),
                toggle("ph", "pH sérique le plus bas < 7.10 (+16)", "Lowest serum pH < 7.10 (+16)", "pH < 7.10 (+16)", 16),
                toggle("seizures", "Convulsions multiples (+19)", "Multiple seizures (+19)", "Convulsiones múltiples (+19)", 19),
                toggle("apgar7", "APGAR à 5 min < 7 (+18)", "5-min APGAR < 7 (+18)", "APGAR 5 min < 7 (+18)", 18),
                toggle("bw_1000", "Poids de naissance < 1000 g (+17)", "Birth weight < 1000g (+17)", "Peso < 1000 g (+17)", 17)
            )
        )
    )
}
