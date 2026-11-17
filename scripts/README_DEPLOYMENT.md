# Medical Score Pro — Guide de Déploiement Serverless (Plan Spark 0 €)

## 1. Initialisation Firebase (Plan Spark uniquement, sans Cloud Functions)

Exécutez le script fourni :
```bash
chmod +x scripts/setup_firebase_spark.sh
./scripts/setup_firebase_spark.sh
```

### Validations 2FA manuelles requises :
1. `gcloud auth login` : ouvrez le lien affiché et validez votre 2FA Google.
2. `firebase login` : validez l'accès Firebase CLI dans votre navigateur.
3. Dans la Console Firebase :
   - **Authentication → Sign-in method** : activez le fournisseur **Google**.
   - **App Check** : vérifiez l'enregistrement Android pour `com.aistudio.medicalscorepro.mqtbr`.

### Attribuer le rôle Admin (sans Cloud Function) :
```bash
export GOOGLE_APPLICATION_CREDENTIALS=./github-actions-sa.json
python3 scripts/set_admin_claim.py --email mandupaix@gmail.com
```

---

## 2. Déploiement du serveur MedGemma 1.5 4B (Hugging Face Spaces - CPU Basic gratuit)

1. Créez un Space Docker sur [huggingface.co/spaces](https://huggingface.co/spaces) avec le matériel gratuit **CPU Basic (16 Go RAM)**.
2. Poussez le contenu de `backend/medgemma_hf_space/` (`app.py`, `requirements.txt`, `Dockerfile`, `README.md`).
3. Dans **Settings → Variables and secrets** du Space, ajoutez :
   - `FIREBASE_SERVICE_ACCOUNT_JSON` : contenu de `hf-medgemma-sa.json`
   - `FIRESTORE_DATABASE_ID` : `ai-studio-android-medicals-4c10e4c2-f93c-4854-a44b-ce5c59a22f78`
   - `HF_TOKEN` : votre token Hugging Face (après acceptation de la licence `google/medgemma-4b-it`)
4. Copiez l'URL publique du Space (ex: `https://votre-compte-medgemma.hf.space`) et renseignez-la dans l'écran **Admin** de l'application (`hfSpaceUrl`) ou dans `.env` (`MEDGEMMA_HF_SPACE_URL`).

---

## 3. Workflow GitHub Actions Mensuel (17 de chaque mois)

1. Dans votre dépôt GitHub → **Settings → Secrets and variables → Actions** :
   - Secret `FIREBASE_SERVICE_ACCOUNT_JSON` : contenu de `github-actions-sa.json`
   - Secret `FIRESTORE_DATABASE_ID` : `ai-studio-android-medicals-4c10e4c2-f93c-4854-a44b-ce5c59a22f78`
2. Le workflow `.github/workflows/monthly_update.yml` s'exécute automatiquement le 17 de chaque mois (`0 8 17 * *`) ou manuellement via *Run workflow*.
3. Supprimez ensuite `github-actions-sa.json` et `hf-medgemma-sa.json` de votre poste local.
