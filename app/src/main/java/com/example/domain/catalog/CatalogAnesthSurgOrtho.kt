package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogAnesthSurgOrtho {

    val anesthMeta = SpecialtyMeta("anesthesiology", "Anesthésiologie", "Anesthesiology", "Anestesiología")
    val surgMeta = SpecialtyMeta("surgery", "Chirurgie Générale", "General Surgery", "Cirugía General")
    val orthoMeta = SpecialtyMeta("orthopedics", "Orthopédie", "Orthopedics", "Ortopedia")

    // 5 Anesthesiology calculators
    val anesthesiologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "asa_ps",
            name = "Classification ASA (Risque Anesthésique)",
            specialty = anesthMeta,
            descFr = "État physique pré-opératoire de l'American Society of Anesthesiologists (ASA I–V).",
            descEn = "ASA Physical Status Classification System (I–V).",
            descEs = "Clasificación del estado físico ASA (I–V).",
            minScore = 1.0,
            maxScore = 5.0,
            severeBoundary = 4.0,
            moderateBoundary = 3.0,
            unitDisplay = "ASA",
            inputs = listOf(
                choice("asa", "Classe ASA", "ASA Class", "Clase ASA", listOf(
                    opt(1, "ASA I - Patient sain"),
                    opt(2, "ASA II - Maladie systémique modérée"),
                    opt(3, "ASA III - Maladie systémique sévère"),
                    opt(4, "ASA IV - Menace vitale constante"),
                    opt(5, "ASA V - Moribond, survie improbable sans chirurgie")
                ), 1)
            )
        ),
        mcpScore(
            scoreId = "mallampati",
            name = "Score de Mallampati Modifié",
            specialty = anesthMeta,
            descFr = "Évaluation pré-anesthésique de la difficulté d'intubation orotrachéale (Classes I–IV).",
            descEn = "Modified Mallampati airway visualization class (I–IV).",
            descEs = "Clasificación de Mallampati modificada de vía aérea (I–IV).",
            minScore = 1.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "Classe",
            inputs = listOf(
                choice("class", "Visibilité oropharyngée", "Oropharyngeal view", "Visión orofaríngea", listOf(
                    opt(1, "Classe I - Palais mou, luette, piliers visibles"),
                    opt(2, "Classe II - Palais mou, majeure partie luette visibles"),
                    opt(3, "Classe III - Palais mou et base de luette visibles"),
                    opt(4, "Classe IV - Palais dur uniquement visible")
                ), 1)
            )
        ),
        mcpScore(
            scoreId = "apfel_ponv",
            name = "Score d'Apfel (NVPO)",
            specialty = anesthMeta,
            descFr = "Prédiction des nausées et vomissements post-opératoires (0–4 facteurs).",
            descEn = "Apfel Score for Postoperative Nausea and Vomiting (PONV) risk (0–4).",
            descEs = "Escala de Apfel de náuseas y vómitos postoperatorios (0–4).",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("female", "Sexe féminin (+1)", "Female sex (+1)", "Sexo femenino (+1)", 1),
                toggle("nonsmoker", "Non-fumeur (+1)", "Non-smoker (+1)", "No fumador (+1)", 1),
                toggle("history", "Antécédent NVPO ou mal des transports (+1)", "History of PONV / motion sickness (+1)", "Antecedente NVPO (+1)", 1),
                toggle("opioids", "Utilisation prévue d'opioïdes post-op (+1)", "Post-op opioids planned (+1)", "Opioides postoperatorios (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "aldrete_pacu",
            name = "Score d'Aldrete Modifié (SSPI)",
            specialty = anesthMeta,
            descFr = "Critères de sortie de salle de surveillance post-interventionnelle (0–10, ≥ 9 requis).",
            descEn = "Modified Aldrete PACU discharge readiness score (0–10).",
            descEs = "Escala de Aldrete modificada para alta de URPA (0–10).",
            minScore = 0.0,
            maxScore = 10.0,
            severeBoundary = 6.0,
            moderateBoundary = 8.0,
            higherIsWorse = false,
            inputs = listOf(
                choice("activity", "Motricité", "Activity", "Actividad", listOf(opt(2, "2 - 4 membres"), opt(1, "1 - 2 membres"), opt(0, "0 - Aucun")), 2),
                choice("resp", "Respiration", "Respiration", "Respiración", listOf(opt(2, "2 - Profonde et toux"), opt(1, "1 - Dyspnée / limitée"), opt(0, "0 - Apnée")), 2),
                choice("circ", "Circulation (PA vs base)", "Circulation", "Circulación", listOf(opt(2, "2 - ± 20% base"), opt(1, "1 - ± 20–50% base"), opt(0, "0 - > ± 50%")), 2),
                choice("cons", "Conscience", "Consciousness", "Conciencia", listOf(opt(2, "2 - Bien réveillé"), opt(1, "1 - Réveillable à l'appel"), opt(0, "0 - Non réveillable")), 2),
                choice("spo2", "Oxygénation (SpO₂)", "O2 Saturation", "Saturación O2", listOf(opt(2, "2 - > 92% AA"), opt(1, "1 - > 90% sous O₂"), opt(0, "0 - < 90% malgré O₂")), 2)
            )
        ),
        mcpScore(
            scoreId = "stop_bang_airway",
            name = "Index d'Intubation Difficile (El-Ganzouri)",
            specialty = anesthMeta,
            descFr = "Évaluation prédictive multivariée de la difficulté d'intubation trachéale (0–12).",
            descEn = "El-Ganzouri Risk Index for difficult tracheal intubation (0–12).",
            descEs = "Índice de riesgo de intubación difícil de El-Ganzouri (0–12).",
            minScore = 0.0,
            maxScore = 12.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("mouth", "Ouverture de bouche", "Mouth opening", "Apertura bucal", listOf(opt(0, "≥ 4 cm (+0)"), opt(1, "< 4 cm (+1)"))),
                choice("tmd", "Distance thyro-mentonnière", "Thyromental distance", "Distancia tiromentoniana", listOf(opt(0, "> 6.5 cm (+0)"), opt(1, "6.0–6.5 cm (+1)"), opt(2, "< 6.0 cm (+2)"))),
                choice("mall", "Mallampati", "Mallampati", "Mallampati", listOf(opt(0, "I (+0)"), opt(1, "II (+1)"), opt(2, "III–IV (+2)"))),
                choice("neck", "Mobilité cervicale", "Neck movement", "Movilidad cervical", listOf(opt(0, "> 90° (+0)"), opt(1, "80–90° (+1)"), opt(2, "< 80° (+2)"))),
                toggle("prog", "Incapacité prognathisme (+1)", "Unable to prognath (+1)", "Sin prognatismo (+1)", 1),
                choice("wt", "Poids", "Weight", "Peso", listOf(opt(0, "< 90 kg (+0)"), opt(1, "90–110 kg (+1)"), opt(2, "> 110 kg (+2)"))),
                choice("hx", "Antécédent intubation difficile", "Prior difficult intubation", "Intubación difícil previa", listOf(opt(0, "Aucun (+0)"), opt(1, "Douteux (+1)"), opt(2, "Certain (+2)")))
            )
        )
    )

    // 5 General Surgery calculators
    val surgeryScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "alvarado",
            name = "Score d'Alvarado (Appendicite)",
            specialty = surgMeta,
            descFr = "Score clinique diagnostique de l'appendicite aiguë (MANTRELS, 0–10).",
            descEn = "Alvarado Score (MANTRELS) for acute appendicitis likelihood (0–10).",
            descEs = "Escala de Alvarado para apendicitis aguda (0–10).",
            minScore = 0.0,
            maxScore = 10.0,
            severeBoundary = 7.0,
            moderateBoundary = 5.0,
            inputs = listOf(
                toggle("migr", "M - Migration douleur en FID (+1)", "Migration of pain to RLQ (+1)", "Migración del dolor (+1)", 1),
                toggle("anor", "A - Anorexie (+1)", "Anorexia (+1)", "Anorexia (+1)", 1),
                toggle("naus", "N - Nausées / vomissements (+1)", "Nausea/vomiting (+1)", "Náuseas/vómitos (+1)", 1),
                toggle("tend", "T - Douleur provoquée FID (+2)", "RLQ tenderness (+2)", "Dolor en FID (+2)", 2),
                toggle("reb", "R - Défense / douleur décompression (+1)", "Rebound tenderness (+1)", "Signo rebote (+1)", 1),
                toggle("temp", "E - Température ≥ 37.3°C (+1)", "Elevated temp (+1)", "Fiebre (+1)", 1),
                toggle("wbc", "L - Leucocytose > 10 000 (+2)", "Leukocytosis > 10,000 (+2)", "Leucocitosis (+2)", 2),
                toggle("shift", "S - Polynucléose neutrophile > 75% (+1)", "Left shift (+1)", "Desviación izquierda (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "caprini_vte",
            name = "Score de Caprini (MTEV Chirurgicale)",
            specialty = surgMeta,
            descFr = "Stratification du risque thromboembolique post-opératoire pour thromboprophylaxie.",
            descEn = "Caprini Risk Assessment Model for surgical VTE prophylaxis.",
            descEs = "Escala de Caprini para profilaxis tromboembólica quirúrgica.",
            minScore = 0.0,
            maxScore = 25.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("age", "Âge", "Age", "Edad", listOf(opt(0, "< 41 (+0)"), opt(1, "41–60 (+1)"), opt(2, "61–74 (+2)"), opt(3, "≥ 75 (+3)"))),
                choice("surg_type", "Type de chirurgie", "Surgery type", "Tipo cirugía", listOf(opt(1, "Mineure (+1)"), opt(2, "Majeure > 45 min (+2)"), opt(5, "Arthroplastie / hanche / trauma (+5)"))),
                toggle("obesity", "IMC > 25 kg/m² (+1)", "BMI > 25 (+1)", "IMC > 25 (+1)", 1),
                toggle("cancer", "Cancer actif ou passé (+2)", "Malignancy (+2)", "Cáncer (+2)", 2),
                toggle("bed", "Alitement > 72h (+2)", "Bedbound > 72h (+2)", "Encamamiento > 72h (+2)", 2),
                toggle("prev_vte", "Antécédent TVP/EP (+3)", "History of DVT/PE (+3)", "TVP/TEP previo (+3)", 3),
                toggle("stroke", "AVC < 1 mois (+5)", "Stroke < 1 mo (+5)", "Ictus < 1 mes (+5)", 5)
            )
        ),
        mcpScore(
            scoreId = "clavien_dindo",
            name = "Classification Clavien-Dindo",
            specialty = surgMeta,
            descFr = "Standard international de graduation des complications chirurgicales (Grades I–V).",
            descEn = "Clavien-Dindo Classification of surgical complications (Grades I–V).",
            descEs = "Clasificación Clavien-Dindo de complicaciones quirúrgicas (I–V).",
            minScore = 1.0,
            maxScore = 5.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "Grade",
            inputs = listOf(
                choice("grade", "Grade de complication", "Complication grade", "Grado", listOf(
                    opt(1, "Grade I - Déviation simple sans traitement invasif"),
                    opt(2, "Grade II - Traitement pharmacologique / transfusion"),
                    opt(3, "Grade III - Réintervention chirurgicale / endoscopique"),
                    opt(4, "Grade IV - Complication menaçant le pronostic vital (USI)"),
                    opt(5, "Grade V - Décès du patient")
                ), 1)
            )
        ),
        mcpScore(
            scoreId = "possom_surg",
            name = "Score Physiologique P-POSSUM (Dépistage)",
            specialty = surgMeta,
            descFr = "Évaluation du risque chirurgical global selon l'âge, état cardiaque et biologie.",
            descEn = "P-POSSUM surgical physiologic severity summary score.",
            descEs = "Evaluación fisiológica prequirúrgica P-POSSUM.",
            minScore = 12.0,
            maxScore = 70.0,
            severeBoundary = 32.0,
            moderateBoundary = 22.0,
            inputs = listOf(
                choice("age", "Âge", "Age", "Edad", listOf(opt(1, "≤ 60 (+1)"), opt(2, "61–70 (+2)"), opt(4, "≥ 71 (+4)")), 1),
                choice("cardiac", "État cardiaque", "Cardiac signs", "Estado cardíaco", listOf(opt(1, "Normal (+1)"), opt(2, "Diurétique/HTA (+2)"), opt(4, "Œdèmes/Warfarine (+4)"), opt(8, "Cardiomégalie/TVJ (+8)")), 1),
                choice("resp", "État respiratoire", "Respiratory", "Respiratorio", listOf(opt(1, "Normal (+1)"), opt(2, "Dyspnée d'effort (+2)"), opt(4, "Dyspnée invalidante (+4)"), opt(8, "Dyspnée repos (+8)")), 1),
                choice("hb", "Hémoglobine (g/dL)", "Hemoglobin", "Hemoglobina", listOf(opt(1, "13–16 (+1)"), opt(2, "11.5–12.9 (+2)"), opt(4, "10–11.4 (+4)"), opt(8, "< 10 (+8)")), 1),
                choice("urgency", "Degré d'urgence", "Operative urgency", "Urgencia", listOf(opt(1, "Programmée (+1)"), opt(4, "Urgente (+4)"), opt(8, "Immédiate (+8)")), 1)
            )
        ),
        mcpScore(
            scoreId = "parkland_burn",
            name = "Formule de Parkland (Brûlés 24h)",
            specialty = surgMeta,
            descFr = "Volume de Ringer Lactate sur 24h chez le grand brûlé : 4 mL × Poids(kg) × % SCT.",
            descEn = "Parkland Formula for 24h fluid resuscitation in burns (4 mL × kg × %TBSA).",
            descEs = "Fórmula de Parkland para reposición hídrica en quemados (mL/24h).",
            minScore = 500.0,
            maxScore = 40000.0,
            severeBoundary = 8000.0,
            moderateBoundary = 4000.0,
            unitDisplay = "mL/24h",
            formulaKey = "parkland_burn",
            inputs = listOf(
                slider("weight", "Poids (kg)", "Weight (kg)", "Peso (kg)", 10.0, 160.0, 75.0, "kg"),
                slider("tbsa", "Surface corporelle brûlée (% SCT)", "% Total Body Surface Area", "% SCT quemada", 5.0, 95.0, 25.0, "%")
            )
        )
    )

    // 3 Orthopedics calculators
    val orthopedicsScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "gustilo_anderson",
            name = "Classification Gustilo-Anderson",
            specialty = orthoMeta,
            descFr = "Graduation des fractures ouvertes pour antibioprophylaxie et couverture (I–IIIc).",
            descEn = "Gustilo-Anderson open fracture severity classification.",
            descEs = "Clasificación de fracturas abiertas de Gustilo-Anderson.",
            minScore = 1.0,
            maxScore = 5.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "Type",
            inputs = listOf(
                choice("type", "Stade lésionnel", "Injury grade", "Grado lesión", listOf(
                    opt(1, "Type I - Plaie < 1 cm, propre"),
                    opt(2, "Type II - Plaie 1–10 cm, dégâts modérés"),
                    opt(3, "Type IIIA - > 10 cm, couverture osseuse possible"),
                    opt(4, "Type IIIB - Perte de substance, lambeau requis"),
                    opt(5, "Type IIIC - Lésion artérielle nécessitant réparation")
                ), 1)
            )
        ),
        mcpScore(
            scoreId = "mirels_fracture",
            name = "Score de Mirels (Fracture Pathologique)",
            specialty = orthoMeta,
            descFr = "Risque de fracture sur métastase osseuse des os longs (≥ 9 = ostéosynthèse préventive).",
            descEn = "Mirels' Score for impending pathologic fracture in metastatic bone disease (4–12).",
            descEs = "Puntuación de Mirels para riesgo de fractura patológica (4–12).",
            minScore = 4.0,
            maxScore = 12.0,
            severeBoundary = 9.0,
            moderateBoundary = 8.0,
            inputs = listOf(
                choice("site", "Localisation", "Site", "Localización", listOf(opt(1, "Membre supérieur (+1)"), opt(2, "Membre inférieur (+2)"), opt(3, "Région péri-trochantérienne (+3)")), 1),
                choice("pain", "Douleur", "Pain", "Dolor", listOf(opt(1, "Légère (+1)"), opt(2, "Modérée (+2)"), opt(3, "Fonctionnelle majeure (+3)")), 1),
                choice("lesion", "Aspect radiologique", "Lesion type", "Tipo lesión", listOf(opt(1, "Condensante / blastique (+1)"), opt(2, "Mixte (+2)"), opt(3, "Lytique (+3)")), 1),
                choice("size", "Atteinte corticale", "Cortical size", "Tamaño cortical", listOf(opt(1, "< 1/3 (+1)"), opt(2, "1/3 à 2/3 (+2)"), opt(3, "> 2/3 (+3)")), 1)
            )
        ),
        mcpScore(
            scoreId = "ottawa_knee",
            name = "Règle d'Ottawa (Genou)",
            specialty = orthoMeta,
            descFr = "Indication de radiographie devant un traumatisme aigu du genou (0–5).",
            descEn = "Ottawa Knee Rule for acute knee injury radiography (0–5).",
            descEs = "Regla de Ottawa para radiografía de rodilla (0–5).",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 2.0,
            moderateBoundary = 1.0,
            inputs = listOf(
                toggle("age55", "Âge ≥ 55 ans", "Age ≥ 55", "Edad ≥ 55", 1),
                toggle("fibula", "Douleur tête du péroné", "Isolated fibular head tenderness", "Dolor cabeza peroné", 1),
                toggle("patella", "Douleur isolée de la rotule", "Isolated patellar tenderness", "Dolor rotuliano", 1),
                toggle("flex90", "Flexion < 90° impossible", "Inability to flex to 90°", "Incapacidad flexión 90°", 1),
                toggle("walk4", "Incapacité à faire 4 pas", "Unable to bear weight 4 steps", "Incapacidad 4 pasos", 1)
            )
        )
    )
}
