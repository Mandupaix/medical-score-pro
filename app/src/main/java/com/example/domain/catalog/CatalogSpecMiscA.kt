package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogSpecMiscA {

    val rheumMeta = SpecialtyMeta("rheumatology", "Rhumatologie", "Rheumatology", "Reumatología")
    val dermMeta = SpecialtyMeta("dermatology", "Dermatologie", "Dermatology", "Dermatología")
    val ophthMeta = SpecialtyMeta("ophthalmology", "Ophtalmologie", "Ophthalmology", "Oftalmología")
    val entMeta = SpecialtyMeta("ent", "ORL", "Otolaryngology (ENT)", "Otorrinolaringología")
    val urolMeta = SpecialtyMeta("urology", "Urologie", "Urology", "Urología")
    val toxMeta = SpecialtyMeta("toxicology", "Toxicologie", "Toxicology", "Toxicología")

    // 3 Rheumatology calculators
    val rheumatologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "das28_esr",
            name = "Score DAS28-VS (Polyarthrite Rhumatoïde)",
            specialty = rheumMeta,
            descFr = "Indice d'activité de la polyarthrite rhumatoïde (0.56√NAD + 0.28√NAG + 0.70ln(VS) + 0.014EVA).",
            descEn = "Disease Activity Score-28 with ESR for Rheumatoid Arthritis.",
            descEs = "Índice de actividad DAS28-VS en artritis reumatoide.",
            minScore = 0.5,
            maxScore = 9.5,
            severeBoundary = 5.1,
            moderateBoundary = 3.2,
            unitDisplay = "DAS28",
            formulaKey = "das28_esr",
            inputs = listOf(
                slider("tjc", "Articulations douloureuses (NAD / 28)", "Tender Joint Count (0–28)", "Articulaciones dolorosas", 0.0, 28.0, 4.0),
                slider("sjc", "Articulations gonflées (NAG / 28)", "Swollen Joint Count (0–28)", "Articulaciones tumefactas", 0.0, 28.0, 3.0),
                slider("esr", "Vitesse de sédimentation (mm/h)", "ESR (mm/hr)", "VSG (mm/h)", 2.0, 120.0, 28.0, "mm/h"),
                slider("gh", "Évaluation globale patient EVA (0–100)", "Patient Global VAS (0–100)", "EVA global (0–100)", 0.0, 100.0, 40.0, "mm")
            )
        ),
        mcpScore(
            scoreId = "acr_gout",
            name = "Score Diagnostique Goutte Aiguë (Janssens)",
            specialty = rheumMeta,
            descFr = "Probabilité clinique de crise de goutte en soins primaires sans ponction (0–13).",
            descEn = "Clinical diagnostic gout rule without joint aspiration (0–13).",
            descEs = "Regla clínica diagnóstica de gota aguda (0–13).",
            minScore = 0.0,
            maxScore = 13.0,
            severeBoundary = 8.0,
            moderateBoundary = 4.0,
            inputs = listOf(
                toggle("male", "Sexe masculin (+2)", "Male sex (+2)", "Sexo masculino (+2)", 2),
                toggle("prev", "Crise antérieure d'arthrite similaire (+2)", "Previous patient-reported attack (+2)", "Ataque previo (+2)", 2),
                toggle("onset1d", "Installation maximale < 24h (+0.5 → +1)", "Onset within 1 day (+1)", "Inicio en 1 día (+1)", 1),
                toggle("redness", "Érythème articulaire (+1)", "Joint redness (+1)", "Eritema articular (+1)", 1),
                toggle("mtp1", "Atteinte 1re métatarso-phalangienne (+2.5 → +3)", "1st MTP joint involvement (+3)", "Afectación 1ª MTF (+3)", 3),
                toggle("htn_cvd", "HTA ou maladie cardiovasculaire (+1.5 → +2)", "HTN or ≥1 CV disease (+2)", "HTA o ECV (+2)", 2),
                toggle("uric", "Uricémie > 5.88 mg/dL (350 µmol/L) (+3.5 → +4)", "Serum urate > 5.88 mg/dL (+4)", "Ácido úrico > 5.88 (+4)", 4)
            )
        ),
        mcpScore(
            scoreId = "sle_sledai_proxy",
            name = "Score d'Activité Lupus (SLEDAI Dépistage)",
            specialty = rheumMeta,
            descFr = "Quantification rapide des poussées systémiques de lupus érythémateux disséminé.",
            descEn = "Simplified SLEDAI systemic lupus disease activity flare assessment.",
            descEs = "Evaluación simplificada SLEDAI de actividad lúpica.",
            minScore = 0.0,
            maxScore = 40.0,
            severeBoundary = 12.0,
            moderateBoundary = 6.0,
            inputs = listOf(
                toggle("cns", "Atteinte neuropsychiatrique / vascularite (+8)", "Seizure/psychosis/vasculitis (+8)", "Afectación SNC/vasculitis (+8)", 8),
                toggle("renal", "Protéinurie / sédiment actif rénal (+4)", "Renal casts/hematuria/proteinuria (+4)", "Nefritis activa (+4)", 4),
                toggle("arthritis", "Arthrite ou myosite (+4)", "Arthritis or myositis (+4)", "Artritis o miositis (+4)", 4),
                toggle("serositis", "Pleurésie ou péricardite (+2)", "Pleurisy or pericarditis (+2)", "Serositis (+2)", 2),
                toggle("skin_alo", "Éruption lupique, alopécie ou aphtes (+2)", "Rash/alopecia/ulcers (+2)", "Lesiones mucocutáneas (+2)", 2),
                toggle("immuno", "Complément bas ou anti-ADNdb élevés (+2)", "Low complement / high anti-dsDNA (+2)", "Complemento bajo / anti-ADN (+2)", 2),
                toggle("fever_hem", "Fièvre, thrombopénie ou leucopénie (+1)", "Fever/thrombocytopenia/leukopenia (+1)", "Fiebre/citopenia (+1)", 1)
            )
        )
    )

    // 2 Dermatology calculators
    val dermatologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "pasi_psoriasis",
            name = "Score PASI (Psoriasis)",
            specialty = dermMeta,
            descFr = "Psoriasis Area and Severity Index évaluant étendue et sévérité des plaques (0–72).",
            descEn = "Psoriasis Area and Severity Index (0–72).",
            descEs = "Índice de gravedad y extensión de psoriasis PASI (0–72).",
            minScore = 0.0,
            maxScore = 72.0,
            severeBoundary = 12.0,
            moderateBoundary = 7.0,
            formulaKey = "pasi_calc",
            inputs = listOf(
                slider("head", "Tête - Sévérité moyenne × Surface (0–12)", "Head severity × area (0–12)", "Cabeza (0–12)", 0.0, 12.0, 2.0, "pts"),
                slider("arms", "Membres sup. - Sévérité × Surface (0–12)", "Upper limbs (0–12)", "Extremidades sup.", 0.0, 12.0, 3.0, "pts"),
                slider("trunk", "Tronc - Sévérité × Surface (0–12)", "Trunk (0–12)", "Tronco", 0.0, 12.0, 4.0, "pts"),
                slider("legs", "Membres inf. - Sévérité × Surface (0–12)", "Lower limbs (0–12)", "Extremidades inf.", 0.0, 12.0, 4.0, "pts")
            )
        ),
        mcpScore(
            scoreId = "scorten_sjs",
            name = "Score SCORTEN (Lyell / SJS)",
            specialty = dermMeta,
            descFr = "Pronostic de mortalité des syndromes de Stevens-Johnson et nécrolyse épidermique toxique (0–7).",
            descEn = "SCORTEN mortality prediction for Stevens-Johnson Syndrome / TEN (0–7).",
            descEs = "Escala pronóstica SCORTEN en síndrome de Stevens-Johnson / NET (0–7).",
            minScore = 0.0,
            maxScore = 7.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                toggle("age40", "Âge > 40 ans", "Age > 40", "Edad > 40", 1),
                toggle("cancer", "Hémopathie ou cancer associé", "Associated malignancy", "Neoplasia asociada", 1),
                toggle("hr120", "Tachycardie > 120 bpm", "Heart rate > 120 bpm", "FC > 120", 1),
                toggle("bsa10", "Décollement cutané initial > 10% SCT", "Initial epidermal detachment > 10%", "Despegamiento > 10%", 1),
                toggle("bun10", "Urée > 10 mmol/L (BUN > 28 mg/dL)", "Serum urea > 10 mmol/L", "Urea > 10 mmol/L", 1),
                toggle("gluc14", "Glycémie > 14 mmol/L (252 mg/dL)", "Glucose > 14 mmol/L", "Glucosa > 14 mmol/L", 1),
                toggle("hco3_20", "Bicarbonates < 20 mmol/L", "Bicarbonate < 20 mmol/L", "Bicarbonato < 20", 1)
            )
        )
    )

    // 2 Ophthalmology calculators
    val ophthalmologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "ocular_trauma_ots",
            name = "Ocular Trauma Score (OTS)",
            specialty = ophthMeta,
            descFr = "Pronostic visuel après traumatisme oculaire sévère à partir de l'acuité initiale.",
            descEn = "Ocular Trauma Score visual acuity prognosis after eye injury.",
            descEs = "Puntuación de trauma ocular OTS.",
            minScore = 0.0,
            maxScore = 100.0,
            severeBoundary = 44.0,
            moderateBoundary = 65.0,
            higherIsWorse = false,
            formulaKey = "ots_calc",
            inputs = listOf(
                choice("initial_va", "Acuité visuelle initiale", "Initial visual acuity", "Agudeza visual inicial", listOf(
                    opt(60, "PL négative / NLP (60)"),
                    opt(70, "PL+ / VBLM (70)"),
                    opt(80, "Compte les doigts 1/200–19/200 (80)"),
                    opt(90, "20/200 à 20/50 (90)"),
                    opt(100, "≥ 20/40 (100)")
                ), 90),
                toggle("rupture", "Rupture du globe (-23)", "Globe rupture (-23)", "Ruptura globo (-23)", 23),
                toggle("endoph", "Endophtalmie (-17)", "Endophthalmitis (-17)", "Endoftalmitis (-17)", 17),
                toggle("perf", "Plaie perforante (-14)", "Perforating injury (-14)", "Herida perforante (-14)", 14),
                toggle("rd", "Décollement de rétine (-11)", "Retinal detachment (-11)", "Desprendimiento retina (-11)", 11),
                toggle("rapd", "Déficit pupillaire afférent (-10)", "RAPD present (-10)", "DPAR presente (-10)", 10)
            )
        ),
        mcpScore(
            scoreId = "glaucoma_iop_risk",
            name = "Risque Conversion Hypertonie → Glaucome (OHTS)",
            specialty = ophthMeta,
            descFr = "Facteurs de risque de progression d'une hypertonie oculaire vers le glaucome.",
            descEn = "Ocular hypertension to primary open-angle glaucoma risk screening.",
            descEs = "Riesgo de progresión de hipertensión ocular a glaucoma.",
            minScore = 0.0,
            maxScore = 8.0,
            severeBoundary = 5.0,
            moderateBoundary = 3.0,
            inputs = listOf(
                choice("iop", "Pression intraoculaire (PIO)", "IOP (mmHg)", "PIO (mmHg)", listOf(opt(0, "< 21 (+0)"), opt(1, "21–24 (+1)"), opt(2, "≥ 25 (+2)"))),
                choice("cct", "Épaisseur cornéenne centrale (CCT)", "Central corneal thickness", "Paquimetría", listOf(opt(0, "> 588 µm (+0)"), opt(1, "555–588 µm (+1)"), opt(2, "< 555 µm (+2)"))),
                choice("cd", "Rapport Cup/Disc vertical", "Vertical Cup-to-Disc ratio", "Cociente excavación/papila", listOf(opt(0, "< 0.4 (+0)"), opt(1, "0.4–0.6 (+1)"), opt(2, "> 0.6 (+2)"))),
                toggle("age60", "Âge > 60 ans (+1)", "Age > 60 (+1)", "Edad > 60 (+1)", 1),
                toggle("family", "Antécédent familial glaucome (+1)", "Family history (+1)", "Antecedente familiar (+1)", 1)
            )
        )
    )

    // 2 ENT calculators
    val entScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "epworth_sleepiness",
            name = "Échelle de Somnolence d'Epworth (ESS)",
            specialty = entMeta,
            descFr = "Quantification de la somnolence diurne excessive sur 8 situations quotidiennes (0–24).",
            descEn = "Epworth Sleepiness Scale assessing daytime sleepiness (0–24).",
            descEs = "Escala de somnolencia de Epworth (0–24).",
            minScore = 0.0,
            maxScore = 24.0,
            severeBoundary = 16.0,
            moderateBoundary = 11.0,
            supportsIntervention = false,
            inputs = listOf(
                slider("read_tv", "Assis en lisant ou devant TV (0–6)", "Reading & TV (0–6)", "Leyendo y TV", 0.0, 6.0, 2.0),
                slider("public_pass", "Inactif en public / passager voiture (0–6)", "Public place & car passenger (0–6)", "Lugar público y coche", 0.0, 6.0, 2.0),
                slider("rest_talk", "Allongé l'après-midi / assis en parlant (0–6)", "Lying down & talking (0–6)", "Tumbado y hablando", 0.0, 6.0, 2.0),
                slider("lunch_traffic", "Après repas / bloqué dans les bouchons (0–6)", "After lunch & traffic (0–6)", "Tras comida y tráfico", 0.0, 6.0, 1.0)
            )
        ),
        mcpScore(
            scoreId = "dhi_vertigo",
            name = "Score de Vertige DHI-S (Handicap ORL)",
            specialty = entMeta,
            descFr = "Dizziness Handicap Inventory Short Form évaluant le retentissement des vertiges (0–40).",
            descEn = "Dizziness Handicap Inventory Short Form for vestibular disability (0–40).",
            descEs = "Cuestionario de discapacidad por vértigo DHI-S (0–40).",
            minScore = 0.0,
            maxScore = 40.0,
            severeBoundary = 26.0,
            moderateBoundary = 14.0,
            supportsIntervention = false,
            inputs = listOf(
                slider("phys", "Composante physique (mouvements tête) (0–16)", "Physical triggers (0–16)", "Componente físico", 0.0, 16.0, 4.0),
                slider("func", "Composante fonctionnelle (marche/travail) (0–12)", "Functional impact (0–12)", "Impacto funcional", 0.0, 12.0, 4.0),
                slider("emot", "Composante émotionnelle (anxiété/chutes) (0–12)", "Emotional impact (0–12)", "Impacto emocional", 0.0, 12.0, 2.0)
            )
        )
    )

    // 3 Urology calculators
    val urologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "ipss_prostate",
            name = "Score IPSS (Hypertrophie Prostate)",
            specialty = urolMeta,
            descFr = "International Prostate Symptom Score quantifiant les SBAU urinaires (0–35).",
            descEn = "International Prostate Symptom Score for LUTS / BPH severity (0–35).",
            descEs = "Puntuación internacional de síntomas prostáticos IPSS (0–35).",
            minScore = 0.0,
            maxScore = 35.0,
            severeBoundary = 20.0,
            moderateBoundary = 8.0,
            inputs = listOf(
                slider("incomplete", "Vidange incomplète", "Incomplete emptying", "Vaciado incompleto", 0.0, 5.0, 1.0),
                slider("freq", "Pollakiurie (< 2h)", "Frequency", "Frecuencia", 0.0, 5.0, 2.0),
                slider("intermit", "Jet intermittent", "Intermittency", "Intermitencia", 0.0, 5.0, 1.0),
                slider("urgency", "Impériosités mictionnelles", "Urgency", "Urgencia", 0.0, 5.0, 1.0),
                slider("weak", "Faiblesse du jet", "Weak stream", "Chorro débil", 0.0, 5.0, 2.0),
                slider("strain", "Efforts de poussée", "Straining", "Esfuerzo", 0.0, 5.0, 1.0),
                slider("nocturia", "Nycturie (levers nocturnes)", "Nocturia", "Nicturia", 0.0, 5.0, 2.0)
            )
        ),
        mcpScore(
            scoreId = "stone_score",
            name = "Score STONE (Colique Néphrétique)",
            specialty = urolMeta,
            descFr = "Probabilité de lithiase urétérale non compliquée devant une douleur lombaire aiguë (0–13).",
            descEn = "STONE Score predicting uncomplicated ureteral stone probability (0–13).",
            descEs = "Puntuación STONE para cólico nefrítico por litiasis ureteral (0–13).",
            minScore = 0.0,
            maxScore = 13.0,
            severeBoundary = 10.0,
            moderateBoundary = 6.0,
            inputs = listOf(
                toggle("sex_male", "S - Sexe masculin (+2)", "Sex: Male (+2)", "Sexo masculino (+2)", 2),
                choice("timing", "T - Durée d'évolution douleur", "Timing (pain duration)", "Duración dolor", listOf(opt(0, "> 24h (+0)"), opt(1, "6–24h (+1)"), opt(3, "< 6h (+3)"))),
                toggle("origin", "O - Absence d'antécédent ethnique/autre (+3)", "Origin / typical presentation (+3)", "Presentación típica (+3)", 3),
                choice("nausea", "N - Nausées / Vomissements", "Nausea/vomiting", "Náuseas/vómitos", listOf(opt(0, "Aucun (+0)"), opt(1, "Nausées seules (+1)"), opt(2, "Vomissements (+2)"))),
                toggle("rbc", "E - Hématurie microscopique (+3)", "Erythrocytes in urine (+3)", "Hematuria (+3)", 3)
            )
        ),
        mcpScore(
            scoreId = "renal_trauma_aast",
            name = "Classification AAST (Traumatisme Rénal)",
            specialty = urolMeta,
            descFr = "Graduation scanographique des traumatismes du rein (Grades I à V).",
            descEn = "AAST Renal Injury Scale for blunt and penetrating kidney trauma (I–V).",
            descEs = "Escala AAST de traumatismo renal (I–V).",
            minScore = 1.0,
            maxScore = 5.0,
            severeBoundary = 4.0,
            moderateBoundary = 3.0,
            unitDisplay = "Grade",
            inputs = listOf(
                choice("grade", "Lésion TDM", "CT Injury Grade", "Lesión TC", listOf(
                    opt(1, "Grade I - Contusion ou hématome sous-capsulaire non expansif"),
                    opt(2, "Grade II - Lacération corticale < 1 cm sans extravasation"),
                    opt(3, "Grade III - Lacération > 1 cm sans atteinte voie excrétrice"),
                    opt(4, "Grade IV - Lacération voie excrétrice ou lésion vasculaire segmentaire"),
                    opt(5, "Grade V - Rein détruit ou avulsion pédicule rénal")
                ), 1)
            )
        )
    )

    // 4 Toxicology calculators
    val toxicologyScores: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "anion_gap",
            name = "Trou Anionique Corrigé (Acidose)",
            specialty = toxMeta,
            descFr = "Calcul du trou anionique corrigé par l'albuminémie (Na - Cl - HCO₃ + 2.5×(4 - Alb)).",
            descEn = "Albumin-corrected Serum Anion Gap (mEq/L).",
            descEs = "Brecha aniónica sérica corregida por albúmina (mEq/L).",
            minScore = 0.0,
            maxScore = 45.0,
            severeBoundary = 20.0,
            moderateBoundary = 14.0,
            unitDisplay = "mEq/L",
            formulaKey = "anion_gap",
            inputs = listOf(
                slider("na", "Natrémie (mEq/L)", "Na (mEq/L)", "Na", 115.0, 165.0, 138.0, "mEq/L"),
                slider("cl", "Chlorémie (mEq/L)", "Cl (mEq/L)", "Cl", 75.0, 125.0, 102.0, "mEq/L"),
                slider("hco3", "Bicarbonates HCO₃⁻ (mEq/L)", "HCO3 (mEq/L)", "HCO3", 4.0, 40.0, 16.0, "mEq/L"),
                slider("alb", "Albuminémie (g/dL)", "Albumin (g/dL)", "Albúmina", 1.5, 5.5, 3.5, "g/dL", 0.1)
            )
        ),
        mcpScore(
            scoreId = "serum_osmolality",
            name = "Osmolalité Plasmatique Calculée",
            specialty = toxMeta,
            descFr = "Calcul 2×Na + Glucose/18 + Urée/2.8 (mOsm/kg) pour dépistage d'intoxication (alcools toxiques).",
            descEn = "Calculated Serum Osmolality (2×Na + Glucose/18 + BUN/2.8 mOsm/kg).",
            descEs = "Osmolalidad sérica calculada (mOsm/kg).",
            minScore = 240.0,
            maxScore = 420.0,
            severeBoundary = 320.0,
            moderateBoundary = 300.0,
            unitDisplay = "mOsm/kg",
            formulaKey = "serum_osmolality",
            inputs = listOf(
                slider("na", "Natrémie (mEq/L)", "Na (mEq/L)", "Na", 115.0, 170.0, 140.0, "mEq/L"),
                slider("glucose", "Glycémie (mg/dL)", "Glucose (mg/dL)", "Glucosa", 50.0, 800.0, 110.0, "mg/dL", 5.0),
                slider("bun", "Urée azote BUN (mg/dL)", "BUN (mg/dL)", "BUN", 5.0, 140.0, 18.0, "mg/dL")
            )
        ),
        mcpScore(
            scoreId = "winters_formula",
            name = "Formule de Winter (Compensation Acidose)",
            specialty = toxMeta,
            descFr = "PaCO₂ attendue lors d'une acidose métabolique : 1.5 × HCO₃⁻ + 8 (± 2 mmHg).",
            descEn = "Winter's Formula expected PaCO2 in metabolic acidosis.",
            descEs = "Fórmula de Winter para PaCO2 esperada en acidosis metabólica.",
            minScore = 10.0,
            maxScore = 60.0,
            severeBoundary = 20.0,
            moderateBoundary = 28.0,
            higherIsWorse = false,
            unitDisplay = "mmHg PaCO₂",
            formulaKey = "winters_formula",
            inputs = listOf(
                slider("hco3", "Bicarbonates HCO₃⁻ (mEq/L)", "HCO3 (mEq/L)", "HCO3 (mEq/L)", 3.0, 26.0, 14.0, "mEq/L")
            )
        ),
        mcpScore(
            scoreId = "rumack_apap_risk",
            name = "Risque Hépatotoxique Paracétamol (Rumack)",
            specialty = toxMeta,
            descFr = "Stratification du risque d'hépatite toxique au paracétamol selon dose ingérée et délai.",
            descEn = "Acetaminophen toxicity risk stratification for N-acetylcysteine (NAC) initiation.",
            descEs = "Estratificación de riesgo tóxico por paracetamol.",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("dose", "Dose ingérée estimée", "Ingested dose", "Dosis ingerida", listOf(opt(0, "< 100 mg/kg (+0)"), opt(1, "100–149 mg/kg (+1)"), opt(2, "≥ 150 mg/kg ou > 7.5 g (+2)"))),
                toggle("above_line", "Paracétamolémie H4 ≥ 150 mg/L (ligne traitement) (+2)", "4h APAP ≥ 150 µg/mL (+2)", "Nivel 4h ≥ 150 µg/mL (+2)", 2),
                toggle("risk_factors", "Facteurs vulnérabilité (éthylisme, jeûne, inducteurs) (+1)", "Chronic alcohol / malnutrition (+1)", "Alcoholismo / desnutrición (+1)", 1),
                toggle("late", "Prise en charge tardive > 8h ou dose inconnue (+1)", "Presentation > 8h or staggered (+1)", "Consulta > 8h (+1)", 1)
            )
        )
    )
}
