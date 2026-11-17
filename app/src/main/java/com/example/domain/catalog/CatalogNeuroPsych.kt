package com.example.domain.catalog

import com.example.domain.ClinicalScoreDefinition

internal object CatalogNeuroPsych {

    val neuroMeta = SpecialtyMeta("neurology", "Neurologie", "Neurology", "Neurología")
    val psychMeta = SpecialtyMeta("psychiatry", "Psychiatrie", "Psychiatry", "Psiquiatría")

    // 8 additional Neurology calculators (+ glasgow = 9 total)
    val neurologyExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "nihss",
            name = "NIH Stroke Scale (NIHSS)",
            specialty = neuroMeta,
            descFr = "Quantification neurologique de la sévérité d'un AVC aigu (0–42).",
            descEn = "Quantitative acute ischemic stroke neurological deficit scale (0–42).",
            descEs = "Escala cuantitativa de déficit neurológico en ictus agudo (0–42).",
            minScore = 0.0,
            maxScore = 42.0,
            severeBoundary = 16.0,
            moderateBoundary = 5.0,
            inputs = listOf(
                choice("loc", "1a. Vigilance", "1a. Level of Consciousness", "1a. Nivel de conciencia",
                    listOf(opt(0, "0 - Alerte"), opt(1, "1 - Somnolent"), opt(2, "2 - Stupeur"), opt(3, "3 - Coma"))),
                choice("loc_q", "1b. Questions & Ordres", "1b-c. Questions & Commands", "1b-c. Preguntas y órdenes",
                    listOf(opt(0, "0 - Tout correct"), opt(1, "1 - 1 erreur"), opt(2, "2 - 2 erreurs"), opt(3, "3 - 3 erreurs"), opt(4, "4 - Aucune réponse"))),
                choice("gaze", "2. Oculomotricité", "2. Best Gaze", "2. Mirada conjugada",
                    listOf(opt(0, "0 - Normale"), opt(1, "1 - Parésie partielle"), opt(2, "2 - Déviation forcée"))),
                choice("visual", "3. Champ visuel", "3. Visual Fields", "3. Campos visuales",
                    listOf(opt(0, "0 - Normal"), opt(1, "1 - Quadranopsie"), opt(2, "2 - Hémianopsie complète"), opt(3, "3 - Cécité bilatérale"))),
                choice("facial", "4. Paralysie faciale", "4. Facial Palsy", "4. Parálisis facial",
                    listOf(opt(0, "0 - Normale"), opt(1, "1 - Asymétrie mineure"), opt(2, "2 - Paralysie partielle"), opt(3, "3 - Paralysie complète"))),
                choice("motor_arm", "5. Motricité membres supérieurs (G+D)", "5. Motor Arms (L+R)", "5. Fuerza brazos (I+D)",
                    listOf(opt(0, "0 - Normale"), opt(2, "2 - Chute partielle"), opt(4, "4 - Effort contre gravité absent"), opt(6, "6 - Déficit bilatéral sévère"), opt(8, "8 - Plégie bilatérale"))),
                choice("motor_leg", "6. Motricité membres inférieurs (G+D)", "6. Motor Legs (L+R)", "6. Fuerza piernas (I+D)",
                    listOf(opt(0, "0 - Normale"), opt(2, "2 - Chute partielle"), opt(4, "4 - Chute immédiate"), opt(6, "6 - Déficit bilatéral sévère"), opt(8, "8 - Plégie bilatérale"))),
                choice("ataxia", "7. Ataxie des membres", "7. Limb Ataxia", "7. Ataxia de extremidades",
                    listOf(opt(0, "0 - Absente"), opt(1, "1 - 1 membre"), opt(2, "2 - 2 membres"))),
                choice("sensory", "8. Sensibilité", "8. Sensory", "8. Sensibilidad",
                    listOf(opt(0, "0 - Normale"), opt(1, "1 - Hypoesthésie"), opt(2, "2 - Anesthésie"))),
                choice("language", "9. Langage / Aphasie", "9. Best Language", "9. Lenguaje / Afasia",
                    listOf(opt(0, "0 - Normal"), opt(1, "1 - Aphasie modérée"), opt(2, "2 - Aphasie sévère"), opt(3, "3 - Mutisme"))),
                choice("dysarthria", "10. Dysarthrie & Extinction", "10-11. Dysarthria & Neglect", "10-11. Disartria y negligencia",
                    listOf(opt(0, "0 - Normal"), opt(1, "1 - Modéré"), opt(2, "2 - Sévère"), opt(4, "4 - Anarthrie + négligence")))
            )
        ),
        mcpScore(
            scoreId = "abcd2",
            name = "ABCD² Score (AIT)",
            specialty = neuroMeta,
            descFr = "Risque d'AVC précoce à 2, 7 et 90 jours après un accident ischémique transitoire.",
            descEn = "Early stroke risk stratification after transient ischemic attack (TIA).",
            descEs = "Riesgo precoz de ictus tras accidente isquémico transitorio.",
            minScore = 0.0,
            maxScore = 7.0,
            severeBoundary = 6.0,
            moderateBoundary = 4.0,
            inputs = listOf(
                toggle("age60", "Âge ≥ 60 ans (+1)", "Age ≥ 60 years (+1)", "Edad ≥ 60 años (+1)", 1),
                toggle("bp_high", "PA ≥ 140/90 mmHg (+1)", "BP ≥ 140/90 mmHg (+1)", "PA ≥ 140/90 mmHg (+1)", 1),
                choice("clinical", "Signes cliniques", "Clinical features", "Clínica",
                    listOf(opt(0, "0 - Autres"), opt(1, "1 - Trouble parole sans déficit moteur"), opt(2, "2 - Déficit moteur unilatéral"))),
                choice("duration", "Durée des symptômes", "Duration", "Duración",
                    listOf(opt(0, "0 - < 10 min"), opt(1, "1 - 10–59 min"), opt(2, "2 - ≥ 60 min"))),
                toggle("diabetes", "Diabète (+1)", "Diabetes (+1)", "Diabetes (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "hunt_hess",
            name = "Échelle de Hunt & Hess (HSA)",
            specialty = neuroMeta,
            descFr = "Classification clinique de gravité d'une hémorragie sous-arachnoïdienne anévrismale.",
            descEn = "Clinical severity grading for aneurysmal subarachnoid hemorrhage (SAH).",
            descEs = "Clasificación clínica de hemorragia subaracnoidea aneurismática.",
            minScore = 1.0,
            maxScore = 5.0,
            severeBoundary = 4.0,
            moderateBoundary = 3.0,
            unitDisplay = "Grade",
            inputs = listOf(
                choice("grade", "Présentation clinique", "Clinical presentation", "Presentación clínica",
                    listOf(
                        opt(1, "Grade 1 - Céphalée minime / raideur nuque"),
                        opt(2, "Grade 2 - Céphalée modérée-sévère, paralysie nerf crânien"),
                        opt(3, "Grade 3 - Somnolence, confusion, déficit focal léger"),
                        opt(4, "Grade 4 - Stupeur, hémiparésie modérée-sévère"),
                        opt(5, "Grade 5 - Coma profond, décérébration")
                    ), 1)
            )
        ),
        mcpScore(
            scoreId = "fisher_sah",
            name = "Échelle de Fisher Modifiée",
            specialty = neuroMeta,
            descFr = "Risque de vasospasme cérébral selon l'imagerie TDM dans l'HSA.",
            descEn = "CT-based vasospasm risk prediction in subarachnoid hemorrhage.",
            descEs = "Riesgo de vasoespasmo según TC en hemorragia subaracnoidea.",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            unitDisplay = "Grade",
            inputs = listOf(
                choice("ct_finding", "Aspect TDM initial", "Initial CT findings", "Hallazgos TC iniciales",
                    listOf(
                        opt(0, "0 - Pas d'HSA ni HIV"),
                        opt(1, "1 - HSA fine sans HIV"),
                        opt(2, "2 - HSA fine avec HIV bilatérale"),
                        opt(3, "3 - HSA épaisse sans HIV"),
                        opt(4, "4 - HSA épaisse avec HIV")
                    ), 1)
            )
        ),
        mcpScore(
            scoreId = "ich_score",
            name = "ICH Score (Hémorragie cérébrale)",
            specialty = neuroMeta,
            descFr = "Pronostic de mortalité à 30 jours après hémorragie intracérébrale spontanée.",
            descEn = "30-day mortality predictor for spontaneous intracerebral hemorrhage.",
            descEs = "Predictor de mortalidad a 30 días en hemorragia intracerebral.",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            inputs = listOf(
                choice("gcs_band", "Score de Glasgow initial", "Admission GCS", "GCS inicial",
                    listOf(opt(0, "13–15 (+0)"), opt(1, "5–12 (+1)"), opt(2, "3–4 (+2)"))),
                toggle("vol30", "Volume hématome ≥ 30 cm³ (+1)", "ICH volume ≥ 30 mL (+1)", "Volumen ≥ 30 mL (+1)", 1),
                toggle("ivh", "Hémorragie intraventriculaire (+1)", "Intraventricular hemorrhage (+1)", "Hemorragia intraventricular (+1)", 1),
                toggle("infratentorial", "Origine sous-tentorielle (+1)", "Infratentorial origin (+1)", "Origen infratentorial (+1)", 1),
                toggle("age80", "Âge ≥ 80 ans (+1)", "Age ≥ 80 years (+1)", "Edad ≥ 80 años (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "aspects",
            name = "Score ASPECTS (TDM AVC)",
            specialty = neuroMeta,
            descFr = "Évaluation topographique des signes ischémiques précoces dans le territoire ACM (0–10).",
            descEn = "Alberta Stroke Program Early CT Score for MCA territory ischemia (0–10).",
            descEs = "Puntuación ASPECTS en TC para territorio de arteria cerebral media (0–10).",
            minScore = 0.0,
            maxScore = 10.0,
            severeBoundary = 5.0,
            moderateBoundary = 7.0,
            higherIsWorse = false,
            formulaKey = "aspects_inverse",
            inputs = listOf(
                toggle("caudate", "Hypodensité Noyau Caudé (-1)", "Caudate hypodensity (-1)", "Hipodensidad caudado (-1)", 1),
                toggle("lentiform", "Noyau Lenticulaire (-1)", "Lentiform nucleus (-1)", "Núcleo lenticular (-1)", 1),
                toggle("ic", "Capsule Interne (-1)", "Internal capsule (-1)", "Cápsula interna (-1)", 1),
                toggle("insula", "Ruban Insulaire (-1)", "Insular ribbon (-1)", "Ínsula (-1)", 1),
                toggle("m1", "Territoire M1 (-1)", "M1 cortex (-1)", "Corteza M1 (-1)", 1),
                toggle("m2", "Territoire M2 (-1)", "M2 cortex (-1)", "Corteza M2 (-1)", 1),
                toggle("m3", "Territoire M3 (-1)", "M3 cortex (-1)", "Corteza M3 (-1)", 1),
                toggle("m4", "Territoire M4 (-1)", "M4 cortex (-1)", "Corteza M4 (-1)", 1),
                toggle("m5", "Territoire M5 (-1)", "M5 cortex (-1)", "Corteza M5 (-1)", 1),
                toggle("m6", "Territoire M6 (-1)", "M6 cortex (-1)", "Corteza M6 (-1)", 1)
            )
        ),
        mcpScore(
            scoreId = "modified_rankin",
            name = "Modified Rankin Scale (mRS)",
            specialty = neuroMeta,
            descFr = "Mesure du degré de handicap et d'autonomie fonctionnelle post-AVC (0–6).",
            descEn = "Global disability and functional independence scale post-stroke (0–6).",
            descEs = "Escala de Rankin modificada de discapacidad funcional (0–6).",
            minScore = 0.0,
            maxScore = 6.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            unitDisplay = "Grade",
            inputs = listOf(
                choice("mrs", "Niveau fonctionnel", "Functional status", "Estado funcional",
                    listOf(
                        opt(0, "0 - Aucun symptôme"),
                        opt(1, "1 - Symptômes sans incapacité"),
                        opt(2, "2 - Handicap léger, autonome"),
                        opt(3, "3 - Handicap modéré, marche sans aide"),
                        opt(4, "4 - Handicap modéré à sévère"),
                        opt(5, "5 - Handicap sévère, grabataire"),
                        opt(6, "6 - Décès")
                    ))
            )
        ),
        mcpScore(
            scoreId = "four_score",
            name = "FOUR Score (Coma)",
            specialty = neuroMeta,
            descFr = "Évaluation du coma incluant réflexes du tronc et respiration chez le patient intubé (0–16).",
            descEn = "Full Outline of UnResponsiveness coma scale including brainstem reflexes (0–16).",
            descEs = "Escala FOUR para coma incluyendo reflejos de tronco (0–16).",
            minScore = 0.0,
            maxScore = 16.0,
            severeBoundary = 8.0,
            moderateBoundary = 12.0,
            higherIsWorse = false,
            inputs = listOf(
                choice("eye", "Réponse oculaire (E)", "Eye response", "Respuesta ocular",
                    listOf(opt(4, "4 - Poursuite"), opt(3, "3 - Ouverts sans poursuite"), opt(2, "2 - Ouverture au bruit"), opt(1, "1 - Ouverture à la douleur"), opt(0, "0 - Fermés à la douleur")), 4),
                choice("motor", "Réponse motrice (M)", "Motor response", "Respuesta motora",
                    listOf(opt(4, "4 - Pouce levé / signe"), opt(3, "3 - Localise douleur"), opt(2, "2 - Flexion"), opt(1, "1 - Extension"), opt(0, "0 - Aucune / État de mal")), 4),
                choice("brainstem", "Réflexes tronc cérébral (B)", "Brainstem reflexes", "Reflejos tronco",
                    listOf(opt(4, "4 - Pupillaire + cornéen présents"), opt(3, "3 - 1 pupille dilatée fixe"), opt(2, "2 - Pupillaire ou cornéen absent"), opt(1, "1 - Les deux absents"), opt(0, "0 - Tous absents + toux absente")), 4),
                choice("resp", "Respiration (R)", "Respiration", "Respiración",
                    listOf(opt(4, "4 - Non intubé, régulière"), opt(3, "3 - Non intubé, Cheyne-Stokes"), opt(2, "2 - Non intubé, irrégulière"), opt(1, "1 - Intubé, déclenche respirateur"), opt(0, "0 - Apnée / rythme machine")), 4)
            )
        )
    )

    // 6 additional Psychiatry calculators (+ phq_9 = 7 total)
    val psychiatryExtras: List<ClinicalScoreDefinition> = listOf(
        mcpScore(
            scoreId = "gad_7",
            name = "GAD-7 (Anxiété)",
            specialty = psychMeta,
            descFr = "Dépistage et sévérité du trouble anxieux généralisé sur 7 items (0–21).",
            descEn = "Generalized Anxiety Disorder 7-item severity screening (0–21).",
            descEs = "Cuestionario GAD-7 de ansiedad generalizada (0–21).",
            minScore = 0.0,
            maxScore = 21.0,
            severeBoundary = 15.0,
            moderateBoundary = 10.0,
            supportsIntervention = false,
            inputs = listOf(
                "nervous" to "Nervosité / anxiété",
                "worry_ctrl" to "Incapacité à contrôler les inquiétudes",
                "worry_all" to "Inquiétudes excessives diverses",
                "relax" to "Difficulté à se détendre",
                "restless" to "Agitation intérieure",
                "irritable" to "Irritabilité",
                "afraid" to "Peur qu'un malheur survienne"
            ).map { (k, lbl) ->
                choice(k, lbl, lbl, lbl, listOf(
                    opt(0, "0 - Jamais"), opt(1, "1 - Plusieurs jours"),
                    opt(2, "2 - > 50% du temps"), opt(3, "3 - Presque tous les jours")
                ))
            }
        ),
        mcpScore(
            scoreId = "audit_c",
            name = "AUDIT-C (Mésusage Alcool)",
            specialty = psychMeta,
            descFr = "Repérage rapide d'une consommation d'alcool à risque en 3 questions (0–12).",
            descEn = "3-item alcohol consumption misuse screening tool (0–12).",
            descEs = "Cribado rápido de consumo de riesgo de alcohol AUDIT-C (0–12).",
            minScore = 0.0,
            maxScore = 12.0,
            severeBoundary = 8.0,
            moderateBoundary = 4.0,
            supportsIntervention = false,
            inputs = listOf(
                choice("freq", "Fréquence de consommation", "Drinking frequency", "Frecuencia",
                    listOf(opt(0, "0 - Jamais"), opt(1, "1 - ≤ 1/mois"), opt(2, "2 - 2–4/mois"), opt(3, "3 - 2–3/sem"), opt(4, "4 - ≥ 4/sem"))),
                choice("qty", "Verres par occasion", "Drinks on typical day", "Bebidas por día",
                    listOf(opt(0, "0 - 1–2"), opt(1, "1 - 3–4"), opt(2, "2 - 5–6"), opt(3, "3 - 7–9"), opt(4, "4 - ≥ 10"))),
                choice("binge", "≥ 6 verres en une occasion", "≥ 6 drinks on one occasion", "≥ 6 bebidas en una ocasión",
                    listOf(opt(0, "0 - Jamais"), opt(1, "1 - < 1/mois"), opt(2, "2 - Mensuel"), opt(3, "3 - Hebdo"), opt(4, "4 - Quotidien")))
            )
        ),
        mcpScore(
            scoreId = "cage_screen",
            name = "Questionnaire CAGE / DETA",
            specialty = psychMeta,
            descFr = "Dépistage en 4 questions d'une dépendance à l'alcool (0–4).",
            descEn = "4-item CAGE screening for alcohol use disorder (0–4).",
            descEs = "Cuestionario CAGE de detección de dependencia alcohólica (0–4).",
            minScore = 0.0,
            maxScore = 4.0,
            severeBoundary = 3.0,
            moderateBoundary = 2.0,
            supportsIntervention = false,
            inputs = listOf(
                toggle("cut", "Ressenti le besoin de diminuer (Cut down)", "Felt need to Cut down", "Necesidad de reducir", 1),
                toggle("annoyed", "Agacé par les remarques de l'entourage (Annoyed)", "Annoyed by criticism", "Molesto por críticas", 1),
                toggle("guilty", "Culpabilité liée à l'alcool (Guilty)", "Felt Guilty about drinking", "Culpa por beber", 1),
                toggle("eye", "Besoin d'un verre le matin (Eye-opener)", "Morning Eye-opener", "Copa matutina", 1)
            )
        ),
        mcpScore(
            scoreId = "ciwa_ar",
            name = "CIWA-Ar (Sevrage Alcoolique)",
            specialty = psychMeta,
            descFr = "Évaluation clinique de la sévérité du syndrome de sevrage éthylique (0–67).",
            descEn = "Clinical Institute Withdrawal Assessment for Alcohol revised scale (0–67).",
            descEs = "Escala CIWA-Ar de gravedad de abstinencia alcohólica (0–67).",
            minScore = 0.0,
            maxScore = 67.0,
            severeBoundary = 16.0,
            moderateBoundary = 9.0,
            inputs = listOf(
                slider("nausea", "Nausées / Vomissements", "Nausea/Vomiting", "Náuseas/Vómitos", 0.0, 7.0, 0.0),
                slider("tremor", "Tremblements", "Tremor", "Temblor", 0.0, 7.0, 0.0),
                slider("sweat", "Sueurs paroxystiques", "Paroxysmal sweats", "Sudoración", 0.0, 7.0, 0.0),
                slider("anxiety", "Anxiété", "Anxiety", "Ansiedad", 0.0, 7.0, 0.0),
                slider("agitation", "Agitation", "Agitation", "Agitación", 0.0, 7.0, 0.0),
                slider("tactile", "Troubles tactiles", "Tactile disturbances", "Alteraciones táctiles", 0.0, 7.0, 0.0),
                slider("auditory", "Hallucinations auditives", "Auditory disturbances", "Alucinaciones auditivas", 0.0, 7.0, 0.0),
                slider("visual", "Hallucinations visuelles", "Visual disturbances", "Alucinaciones visuales", 0.0, 7.0, 0.0),
                slider("headache", "Céphalées", "Headache", "Cefalea", 0.0, 7.0, 0.0),
                slider("orientation", "Désorientation", "Orientation clouding", "Desorientación", 0.0, 4.0, 0.0)
            )
        ),
        mcpScore(
            scoreId = "columbia_ssrs",
            name = "C-SSRS (Risque Suicidaire)",
            specialty = psychMeta,
            descFr = "Dépistage structuré de l'idéation et du passage à l'acte suicidaire (0–5).",
            descEn = "Columbia-Suicide Severity Rating Scale triage screening (0–5).",
            descEs = "Escala de evaluación de riesgo suicida C-SSRS (0–5).",
            minScore = 0.0,
            maxScore = 5.0,
            severeBoundary = 4.0,
            moderateBoundary = 2.0,
            supportsIntervention = false,
            inputs = listOf(
                toggle("wish_dead", "Souhait d'être mort(e) (+1)", "Wish to be dead (+1)", "Deseo de morir (+1)", 1),
                toggle("active_thoughts", "Idées suicidaires actives (+1)", "Active suicidal thoughts (+1)", "Ideas suicidas activas (+1)", 1),
                toggle("method", "Méthode envisagée (+1)", "Method considered (+1)", "Método considerado (+1)", 1),
                toggle("intent", "Intention d'agir (+1)", "Suicidal intent (+1)", "Intención de actuar (+1)", 1),
                toggle("plan_attempt", "Plan structuré ou tentative récente (+1)", "Specific plan or recent attempt (+1)", "Plan o intento reciente (+1)", 1)
            )
        ),
        mcpScore(
            scoreId = "cows_opioid",
            name = "COWS (Sevrage Opioïdes)",
            specialty = psychMeta,
            descFr = "Échelle clinique objective de sévérité du sevrage aux opioïdes (0–48).",
            descEn = "Clinical Opiate Withdrawal Scale (0–48).",
            descEs = "Escala clínica de abstinencia de opioides COWS (0–48).",
            minScore = 0.0,
            maxScore = 48.0,
            severeBoundary = 25.0,
            moderateBoundary = 13.0,
            inputs = listOf(
                choice("pulse", "Pouls de repos", "Resting pulse", "Pulso en reposo",
                    listOf(opt(0, "0 - ≤ 80"), opt(1, "1 - 81–100"), opt(2, "2 - 101–120"), opt(4, "4 - > 120"))),
                choice("sweating", "Sudation", "Sweating", "Sudoración",
                    listOf(opt(0, "0 - Aucune"), opt(1, "1 - Frissons/bouffées"), opt(2, "2 - Front humide"), opt(3, "3 - Gouttes visibles"), opt(4, "4 - Profuse"))),
                choice("restlessness", "Impatiences / Agitation", "Restlessness", "Inquietud",
                    listOf(opt(0, "0 - Calme"), opt(1, "1 - Légère"), opt(3, "3 - Mouvements fréquents"), opt(5, "5 - Incapable de rester assis"))),
                choice("pupil", "Diamètre pupillaire", "Pupil size", "Tamaño pupilar",
                    listOf(opt(0, "0 - Normal"), opt(1, "1 - Légère mydriase"), opt(2, "2 - Modérée"), opt(5, "5 - Mydriase maximale"))),
                choice("gi", "Troubles digestifs", "GI upset", "Síntomas GI",
                    listOf(opt(0, "0 - Aucun"), opt(1, "1 - Crampes"), opt(2, "2 - Nausées/selles molles"), opt(3, "3 - Diarrhée/vomissements"), opt(5, "5 - Multiples épisodes"))),
                choice("yawning", "Bâillements / Rhinorrhée / Piloérection", "Yawning / Lacrimation / Gooseflesh", "Bostezos / Rinorrea / Piloerección",
                    listOf(opt(0, "0 - Absent"), opt(2, "2 - Modéré"), opt(5, "5 - Marqué"), opt(10, "10 - Sévère généralisé")))
            )
        )
    )
}
