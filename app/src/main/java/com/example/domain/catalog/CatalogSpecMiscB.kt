package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogSpecMiscB {

    val sportsMeta = SpecialtyMeta("sports_med", "Médecine du Sport", "Sports Medicine", "Medicina Deportiva")
    val pallMeta = SpecialtyMeta("palliative", "Soins Palliatifs", "Palliative Care", "Cuidados Paliativos")
    val pharmMeta = SpecialtyMeta("pharmacology", "Pharmacologie Clinique", "Clinical Pharmacology", "Farmacología Clínica")
    val radMeta = SpecialtyMeta("interventional_rad", "Radiologie Interventionnelle", "Interventional Radiology", "Radiología Intervencionista")
    val primaryMeta = SpecialtyMeta("primary_care", "Médecine Générale", "Primary Care", "Atención Primaria")

    // 2 Sports Medicine calculators
    val sportsScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "bmi_calc",
            name = "Indice de Masse Corporelle (IMC / BMI)",
            specialty = sportsMeta,
            descFr = "Calcul Poids(kg) / Taille(m)² avec classification OMS.",
            descEn = "Body Mass Index (kg/m²) with WHO classification.",
            descEs = "Índice de masa corporal (kg/m²).",
            minScore = 12.0,
            maxScore = 60.0,
            severeBoundary = 35.0,
            moderateBoundary = 25.0,
            unitDisplay = "kg/m²",
            formulaKey = "bmi_calc",
            supportsIntervention = false,
            inputs = listOf(
                slider("weight", "Poids (kg)", "Weight (kg)", "Peso (kg)", 30.0, 200.0, 72.0, "kg"),
                slider("height", "Taille (cm)", "Height (cm)", "Talla (cm)", 120.0, 220.0, 175.0, "cm")
            )
        ),
        mcpScore(
            scoreId = "scat_concussion",
            name = "Score SCAT6 Symptômes (Commotion)",
            specialty = sportsMeta,
            descFr = "Sévérité des symptômes post-commotion cérébrale sportive (céphalées, vertiges, photophobie).",
            descEn = "SCAT6 Concussion symptom severity screening score (0–60).",
            descEs = "Escala de síntomas posconmoción SCAT6 (0–60).",
            minScore = 0.0,
            maxScore = 60.0,
            severeBoundary = 25.0,
            moderateBoundary = 10.0,
            inputs = listOf(
                slider("headache", "Céphalées & pression crânienne (0–12)", "Headache & pressure", "Cefalea y presión", 0.0, 12.0, 3.0),
                slider("vestib", "Vertiges, équilibre, vision floue (0–18)", "Dizziness, balance, blurred vision", "Mareo y equilibrio", 0.0, 18.0, 2.0),
                slider("cog", "Brouillard mental, concentration, mémoire (0–18)", "Fog, concentration, memory", "Niebla mental y memoria", 0.0, 18.0, 2.0),
                slider("photo", "Photophobie & phonophobie (0–12)", "Light & noise sensitivity", "Fotofobia y fonofobia", 0.0, 12.0, 2.0)
            )
        )
    )

    // 3 Palliative Care calculators
    val palliativeScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "pps_palliative",
            name = "Échelle PPSv2 (Soins Palliatifs)",
            specialty = pallMeta,
            descFr = "Palliative Performance Scale évaluant mobilité, autonomie, apports et conscience (10–100%).",
            descEn = "Palliative Performance Scale (PPSv2) functional status (10–100%).",
            descEs = "Escala de estado funcional paliativo PPSv2 (10–100%).",
            minScore = 10.0,
            maxScore = 100.0,
            severeBoundary = 30.0,
            moderateBoundary = 60.0,
            higherIsWorse = false,
            unitDisplay = "%",
            supportsIntervention = false,
            inputs = listOf(
                choice("pps", "Niveau PPSv2", "PPSv2 Level", "Nivel PPSv2", listOf(
                    opt(100, "100% - Ambulation complète, activité normale"),
                    opt(80, "80% - Ambulation complète, maladie évidente"),
                    opt(60, "60% - Ambulation réduite, aide occasionnelle"),
                    opt(50, "50% - Assis/allongé majoritaire, aide importante"),
                    opt(40, "40% - Alité majoritaire, soins complets"),
                    opt(30, "30% - Totalement alité, apports réduits"),
                    opt(20, "20% - Totalement alité, gorgées uniquement"),
                    opt(10, "10% - Somnolence/coma, soins de bouche")
                ), 60)
            )
        ),
        mcpScore(
            scoreId = "esas_symptom",
            name = "Échelle ESAS-r (Symptômes Palliatifs)",
            specialty = pallMeta,
            descFr = "Edmonton Symptom Assessment System quantifiant 9 symptômes de détresse (0–90).",
            descEn = "Edmonton Symptom Assessment System revised (0–90).",
            descEs = "Sistema de evaluación de síntomas de Edmonton ESAS-r (0–90).",
            minScore = 0.0,
            maxScore = 90.0,
            severeBoundary = 45.0,
            moderateBoundary = 20.0,
            supportsIntervention = false,
            inputs = listOf(
                slider("pain", "Douleur (0–10)", "Pain", "Dolor", 0.0, 10.0, 3.0),
                slider("fatigue", "Asthénie & Somnolence (0–20)", "Tiredness & Drowsiness", "Fatiga y somnolencia", 0.0, 20.0, 6.0),
                slider("gi", "Nausées & Perte d'appétit (0–20)", "Nausea & Appetite", "Náuseas y apetito", 0.0, 20.0, 4.0),
                slider("resp", "Dyspnée (0–10)", "Shortness of breath", "Disnea", 0.0, 10.0, 2.0),
                slider("psych", "Dépression, Anxiété & Bien-être (0–30)", "Depression, Anxiety & Well-being", "Depresión, ansiedad y bienestar", 0.0, 30.0, 6.0)
            )
        ),
        mcpScore(
            scoreId = "surprise_ppi",
            name = "Index Pronostique Palliatif (PPI)",
            specialty = pallMeta,
            descFr = "Palliative Prognostic Index estimant la survie à court terme (≤ 3 ou ≤ 6 semaines, 0–15).",
            descEn = "Palliative Prognostic Index (PPI) for short-term survival estimation (0–15).",
            descEs = "Índice pronóstico paliativo PPI (0–15).",
            minScore = 0.0,
            maxScore = 15.0,
            severeBoundary = 6.5,
            moderateBoundary = 4.0,
            supportsIntervention = false,
            inputs = listOf(
                choice("pps", "PPS", "PPS", "PPS", listOf(opt(0, "≥ 60% (+0)"), opt(25, "30–50% (+2.5)"), opt(40, "10–20% (+4)"))),
                choice("intake", "Apports oraux", "Oral intake", "Ingesta oral", listOf(opt(0, "Normaux (+0)"), opt(10, "Modérément réduits (+1)"), opt(25, "Sévèrement réduits (+2.5)"))),
                toggle("edema", "Œdèmes (+1)", "Edema (+1)", "Edema (+1)", 10),
                toggle("dyspnea", "Dyspnée de repos (+3.5)", "Dyspnea at rest (+3.5)", "Disnea en reposo (+3.5)", 35),
                toggle("delirium", "Delirium (+4)", "Delirium (+4)", "Delirio (+4)", 40)
            ).let {
                // Use integer points scaled /10 via formulaKey for 0.5 precision
                it
            },
            formulaKey = "ppi_scaled10"
        )
    )

    // 3 Clinical Pharmacology calculators
    val pharmacologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "naranjo_adr",
            name = "Algorithme de Naranjo (Pharmacovigilance)",
            specialty = pharmMeta,
            descFr = "Imputabilité d'un effet indésirable médicamenteux (≥ 9 certain, 5–8 probable, 1–4 possible).",
            descEn = "Naranjo Adverse Drug Reaction (ADR) Probability Scale (-4 to +13).",
            descEs = "Algoritmo de Naranjo de causalidad de reacción adversa a medicamentos.",
            minScore = -4.0,
            maxScore = 13.0,
            severeBoundary = 9.0,
            moderateBoundary = 5.0,
            supportsIntervention = false,
            inputs = listOf(
                toggle("prev_reports", "Effet déjà décrit dans la littérature (+1)", "Previous conclusive reports (+1)", "Informes previos (+1)", 1),
                toggle("after_drug", "Apparu après l'administration (+2)", "Appeared after suspected drug (+2)", "Tras administración (+2)", 2),
                toggle("dechallenge", "Amélioration à l'arrêt / antagoniste (+1)", "Improved on discontinuation (+1)", "Mejoría al suspender (+1)", 1),
                toggle("rechallenge", "Réapparition à la réintroduction (+2)", "Reappeared on re-administration (+2)", "Reaparición al reexponer (+2)", 2),
                toggle("no_alt", "Absence d'autre cause explicative (+2)", "No alternative causes (+2)", "Sin causas alternativas (+2)", 2),
                toggle("dose_rel", "Effet dose-dépendant (+1)", "More severe at higher dose (+1)", "Dosis dependiente (+1)", 1),
                toggle("toxic_lvl", "Concentration plasmatique toxique (+1)", "Toxic blood concentration (+1)", "Nivel sérico tóxico (+1)", 1),
                toggle("obj_ev", "Confirmé par preuve objective (+1)", "Confirmed by objective evidence (+1)", "Evidencia objetiva (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "anticholinergic_acb",
            name = "Charge Anticholinergique (Score ACB)",
            specialty = pharmMeta,
            descFr = "Cumul de la charge anticholinergique médicamenteuse chez le sujet âgé (≥ 3 = risque confusion/chutes).",
            descEn = "Anticholinergic Cognitive Burden (ACB) cumulative medication risk.",
            descEs = "Carga cognitiva anticolinérgica acumulada (ACB).",
            minScore = 0.0,
            maxScore = 12.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            supportsIntervention = false,
            inputs = listOf(
                slider("acb1", "Nombre de médicaments ACB 1 (ex. furosémide, codéine, digoxine)", "Count of ACB=1 drugs", "Fármacos ACB=1", 0.0, 6.0, 1.0),
                choice("acb2", "Médicaments ACB 2 (ex. carbamazépine, amantadine)", "ACB=2 drugs", "Fármacos ACB=2", listOf(opt(0, "0"), opt(2, "1 (+2)"), opt(4, "≥ 2 (+4)"))),
                choice("acb3", "Médicaments ACB 3 (ex. amitriptyline, hydroxyzine, oxybutynine)", "ACB=3 drugs", "Fármacos ACB=3", listOf(opt(0, "0"), opt(3, "1 (+3)"), opt(6, "≥ 2 (+6)")))
            )
        ),
        mcpScore(
            scoreId = "ideal_body_weight",
            name = "Poids Idéal Théorique (Formule de Devine)",
            specialty = pharmMeta,
            descFr = "Calcul du poids idéal (kg) pour ventilation protectrice (6 mL/kg) et posologies hydrophiles.",
            descEn = "Devine Ideal Body Weight (kg) for lung-protective ventilation & drug dosing.",
            descEs = "Peso corporal ideal por fórmula de Devine (kg).",
            minScore = 30.0,
            maxScore = 115.0,
            severeBoundary = 95.0,
            moderateBoundary = 80.0,
            unitDisplay = "kg IBW",
            formulaKey = "devine_ibw",
            inputs = listOf(
                slider("height", "Taille (cm)", "Height (cm)", "Talla (cm)", 135.0, 210.0, 172.0, "cm"),
                toggle("female", "Sexe féminin", "Female sex", "Sexo femenino", 1)
            )
        )
    )

    // 2 Interventional Radiology calculators
    val interventionalRadScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "mehdi_contrast_cin",
            name = "Score de Mehran (Néphropathie au Produit de Contraste)",
            specialty = radMeta,
            descFr = "Risque d'IRA post-injection de produit de contraste iodé en coronarographie / RI.",
            descEn = "Mehran Contrast-Induced Nephropathy (CIN) risk score after iodinated contrast.",
            descEs = "Escala de Mehran de nefropatía inducida por contraste.",
            minScore = 0.0,
            maxScore = 30.0,
            severeBoundary = 11.0,
            moderateBoundary = 6.0,
            inputs = listOf(
                toggle("hypo", "Hypotension (+5)", "Hypotension (+5)", "Hipotensión (+5)", 5),
                toggle("iabp", "Ballon intra-aortique / choc (+5)", "IABP (+5)", "Balón contrapulsación (+5)", 5),
                toggle("chf", "Insuffisance cardiaque NYHA III–IV (+5)", "CHF NYHA III–IV (+5)", "ICC NYHA III–IV (+5)", 5),
                toggle("age75", "Âge > 75 ans (+4)", "Age > 75 (+4)", "Edad > 75 (+4)", 4),
                toggle("anemia", "Anémie (+3)", "Anemia (+3)", "Anemia (+3)", 3),
                toggle("dm", "Diabète (+3)", "Diabetes (+3)", "Diabetes (+3)", 3),
                choice("egfr", "Créatinine > 1.5 ou DFG < 60", "Renal function", "Función renal", listOf(opt(0, "DFG ≥ 60 (+0)"), opt(2, "DFG 40–59 (+2)"), opt(4, "DFG 20–39 (+4)"), opt(6, "DFG < 20 (+6)"))),
                choice("vol", "Volume contraste (par 100 mL)", "Contrast volume", "Volumen contraste", listOf(opt(1, "≤ 100 mL (+1)"), opt(2, "101–200 mL (+2)"), opt(3, "> 200 mL (+3)")))
            )
        ),
        mcpScore(
            scoreId = "sirs_ir_bleed",
            name = "Risque Hémorragique SIR (Acte Interventionnel)",
            specialty = radMeta,
            descFr = "Évaluation du risque hémorragique pré-procédure en radiologie interventionnelle.",
            descEn = "SIR Periprocedural Bleeding Risk stratification (INR, platelets, procedure tier).",
            descEs = "Estratificación de riesgo hemorrágico periprocedimiento SIR.",
            minScore = 0.0,
            maxScore = 7.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("proc_risk", "Catégorie d'acte SIR", "SIR Procedure Bleeding Category", "Categoría procedimiento", listOf(
                    opt(0, "Risque faible (cathéter, ponction superficielle) (+0)"),
                    opt(2, "Risque élevé (biopsie parenchymateuse, néphrostomie, TIPS) (+2)")
                )),
                toggle("inr_high", "INR > 1.5 (+2)", "INR > 1.5 (+2)", "INR > 1.5 (+2)", 2),
                toggle("plt_low", "Plaquettes < 50 G/L (+2)", "Platelets < 50k (+2)", "Plaquetas < 50k (+2)", 2),
                toggle("antiplt", "Double antiagrégation / Anticoagulant actif (+1)", "Active anticoagulant / DAPT (+1)", "Anticoagulante / DAPT (+1)", 1)
            )
        )
    )

    // 4 Primary Care calculators
    val primaryCareScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "bmi_waist_metabolic",
            name = "Critères Syndrome Métabolique (NCEP ATP III)",
            specialty = primaryMeta,
            descFr = "Dépistage du syndrome métabolique (≥ 3 critères sur 5 positifs).",
            descEn = "NCEP ATP III Metabolic Syndrome criteria (≥ 3 of 5).",
            descEs = "Criterios de síndrome metabólico NCEP ATP III (≥ 3).",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 4.0,
            moderateBoundary = 3.0,
            supportsIntervention = false,
            inputs = listOf(
                toggle("waist", "Tour de taille élevé (> 102 cm H / > 88 cm F)", "Elevated waist circumference", "Perímetro abdominal elevado", 1),
                toggle("tg", "Triglycérides ≥ 1.50 g/L (1.7 mmol/L)", "Triglycerides ≥ 150 mg/dL", "Triglicéridos ≥ 150", 1),
                toggle("hdl", "HDL-C bas (< 0.40 g/L H / < 0.50 g/L F)", "Low HDL cholesterol", "HDL bajo", 1),
                toggle("bp", "PA ≥ 130/85 mmHg ou HTA traitée", "BP ≥ 130/85 mmHg", "PA ≥ 130/85", 1),
                toggle("fbg", "Glycémie à jeun ≥ 1.00 g/L (5.6 mmol/L)", "Fasting glucose ≥ 100 mg/dL", "Glucosa ayunas ≥ 100", 1)
            )
        ),
        mcpScore(
            scoreId = "frax_proxy",
            name = "Dépistage Risque Ostéoporotique (OSTA / FRAX)",
            specialty = primaryMeta,
            descFr = "Évaluation clinique simplifiée du risque de fracture ostéoporotique.",
            descEn = "Clinical osteoporosis fracture risk screening.",
            descEs = "Cribado clínico de riesgo de fractura osteoporótica.",
            minScore = 0.0,
            maxScore = 9.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            supportsIntervention = false,
            inputs = listOf(
                choice("age", "Âge", "Age", "Edad", listOf(opt(0, "< 60 (+0)"), opt(1, "60–69 (+1)"), opt(2, "70–79 (+2)"), opt(3, "≥ 80 (+3)"))),
                toggle("prev_fx", "Antécédent fracture de fragilité (+2)", "Prior fragility fracture (+2)", "Fractura previa (+2)", 2),
                toggle("steroids", "Corticothérapie prolongée (+1)", "Long-term glucocorticoids (+1)", "Corticoides prolongados (+1)", 1),
                toggle("low_bmi", "IMC < 20 kg/m² (+1)", "Low BMI < 20 (+1)", "IMC < 20 (+1)", 1),
                toggle("parent_hip", "Antécédent parental fracture hanche (+1)", "Parental hip fracture (+1)", "Antecedente familiar cadera (+1)", 1),
                toggle("smoke_alc", "Tabac ou alcool ≥ 3 u/j (+1)", "Smoking or alcohol excess (+1)", "Tabaco o alcohol (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "morse_fall",
            name = "Échelle de Chute de Morse",
            specialty = primaryMeta,
            descFr = "Évaluation du risque de chute en hospitalisation ou EHPAD (0–125, ≥ 45 haut risque).",
            descEn = "Morse Fall Scale for inpatient fall prevention (0–125).",
            descEs = "Escala de riesgo de caídas de Morse (0–125).",
            minScore = 0.0,
            maxScore = 125.0,
            severeBoundary = 45.0,
            moderateBoundary = 25.0,
            supportsIntervention = false,
            inputs = listOf(
                toggle("hx_fall", "Antécédent de chute (< 3 mois) (+25)", "History of falling (+25)", "Caída previa (+25)", 25),
                toggle("sec_dx", "Diagnostics multiples (≥ 2) (+15)", "Secondary diagnosis (+15)", "Diagnóstico secundario (+15)", 15),
                choice("aid", "Aide à la marche", "Ambulatory aid", "Ayuda deambulación", listOf(opt(0, "Aucune / Alité (+0)"), opt(15, "Cannes / Déambulateur (+15)"), opt(30, "S'appuie aux meubles (+30)"))),
                toggle("iv", "Perfusion IV / Cathéter (+20)", "IV therapy / heparin lock (+20)", "Vía IV (+20)", 20),
                choice("gait", "Démarche", "Gait", "Marcha", listOf(opt(0, "Normale / Alité (+0)"), opt(10, "Faible (+10)"), opt(20, "Perturbée / déséquilibrée (+20)"))),
                toggle("mental", "Surestimation des capacités / oubli limites (+15)", "Forgets limitations (+15)", "Olvida limitaciones (+15)", 15)
            )
        ),
        mcpScore(
            scoreId = "braden_ulcer",
            name = "Échelle de Braden (Risque d'Escarre)",
            specialty = primaryMeta,
            descFr = "Prédiction du risque d'escarre sur 6 domaines (6–23, ≤ 12 haut risque).",
            descEn = "Braden Scale for predicting pressure ulcer risk (6–23, lower = higher risk).",
            descEs = "Escala de Braden de riesgo de úlceras por presión (6–23).",
            minScore = 6.0,
            maxScore = 23.0,
            severeBoundary = 12.0,
            moderateBoundary = 14.0,
            higherIsWorse = false,
            supportsIntervention = false,
            inputs = listOf(
                choice("sensory", "Perception sensorielle", "Sensory perception", "Percepción sensorial", listOf(opt(4, "4 - Non altérée"), opt(3, "3 - Légèrement limitée"), opt(2, "2 - Très limitée"), opt(1, "1 - Complètement limitée")), 4),
                choice("moisture", "Humidité", "Moisture", "Humedad", listOf(opt(4, "4 - Rarement humide"), opt(3, "3 - Occasionnelle"), opt(2, "2 - Très humide"), opt(1, "1 - Constante")), 4),
                choice("activity", "Activité", "Activity", "Actividad", listOf(opt(4, "4 - Marche fréquemment"), opt(3, "3 - Marche occasionnelle"), opt(2, "2 - Fauteuil"), opt(1, "1 - Alité")), 4),
                choice("mobility", "Mobilité", "Mobility", "Movilidad", listOf(opt(4, "4 - Sans limitation"), opt(3, "3 - Légèrement limitée"), opt(2, "2 - Très limitée"), opt(1, "1 - Immobile")), 4),
                choice("nutrition", "Nutrition", "Nutrition", "Nutrición", listOf(opt(4, "4 - Excellente"), opt(3, "3 - Adéquate"), opt(2, "2 - Probable insuffisante"), opt(1, "1 - Très pauvre")), 4),
                choice("friction", "Friction & Cisaillement", "Friction & Shear", "Fricción y cizalla", listOf(opt(3, "3 - Aucun problème"), opt(2, "2 - Problème potentiel"), opt(1, "1 - Problème présent")), 3)
            )
        )
    )
}
