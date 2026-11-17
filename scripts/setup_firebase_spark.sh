#!/usr/bin/env bash
# ==============================================================================
# Medical Score Pro - Initialisation Firebase Spark (Coût Zéro) & Comptes de Service
# ==============================================================================
# Ce script automatise la création/configuration sur le plan gratuit Firebase Spark
# sans aucune Cloud Function (aucun passage au plan Blaze requis).
#
# ÉTAPES MANUELLES 2FA / INTERACTIVES REQUISES :
#   1. Exécutez `gcloud auth login` (validez la 2FA Google dans votre navigateur).
#   2. Exécutez `firebase login` (validez la 2FA Firebase dans votre navigateur).
#   3. Si vous utilisez Hugging Face pour MedGemma, acceptez la licence du modèle
#      google/medgemma-4b-it sur huggingface.co et créez un token READ.
# ==============================================================================

set -euo pipefail

PROJECT_ID="${1:-medical-score-pro-$(date +%s | tail -c 6)}"
FIRESTORE_REGION="${FIRESTORE_REGION:-europe-west1}"
DATABASE_ID="${DATABASE_ID:-ai-studio-android-medicals-4c10e4c2-f93c-4854-a44b-ce5c59a22f78}"
ANDROID_PACKAGE="com.aistudio.medicalscorepro.mqtbr"

echo "=== [1/6] Vérification des sessions CLI (2FA manuelle si non connecté) ==="
echo "Si demandé, validez votre code 2FA dans le navigateur :"
gcloud auth list --filter=status:ACTIVE --format="value(account)" | grep -q . || gcloud auth login
firebase projects:list >/dev/null 2>&1 || firebase login

echo "=== [2/6] Création / Sélection du projet Firebase (Plan Spark gratuit) ==="
if ! gcloud projects describe "$PROJECT_ID" >/dev/null 2>&1; then
  gcloud projects create "$PROJECT_ID" --name="Medical Score Pro"
fi
gcloud config set project "$PROJECT_ID"

# Ajout de Firebase au projet GCP (reste sur le plan Spark gratuit par défaut)
firebase projects:addfirebase "$PROJECT_ID" || true
firebase use "$PROJECT_ID"

echo "=== [3/6] Activation des API gratuites (Firestore, Auth, App Check, AI Logic, FCM) ==="
gcloud services enable \
  firestore.googleapis.com \
  identitytoolkit.googleapis.com \
  firebaseappcheck.googleapis.com \
  firebasevertexai.googleapis.com \
  fcm.googleapis.com \
  --project="$PROJECT_ID"

echo "=== [4/6] Création de la base Firestore Enterprise/Standard nommée (sans Blaze) ==="
gcloud firestore databases create \
  --database="$DATABASE_ID" \
  --location="$FIRESTORE_REGION" \
  --type=firestore-native \
  --project="$PROJECT_ID" || echo "Base Firestore '$DATABASE_ID' déjà existante."

echo "=== [5/6] Déploiement des règles de sécurité Firestore vérifiées ==="
firebase deploy --only firestore:rules --project="$PROJECT_ID"

echo "=== [6/6] Création des comptes de service à privilèges minimaux ==="
# 1. Rôle personnalisé minimal pour GitHub Actions (lecture anonymous_evaluations + écriture ai_model_config + envoi FCM)
ROLE_ID="MedicalScoreAggregatorRole"
cat > /tmp/aggregator-role.yaml <<EOF
title: "Medical Score Pro GitHub Aggregator"
description: "Minimal permissions for monthly Firestore aggregations, config updates, and FCM alerts"
stage: "GA"
includedPermissions:
  - datastore.entities.get
  - datastore.entities.list
  - datastore.entities.create
  - datastore.entities.update
  - cloudmessaging.messages.create
EOF

gcloud iam roles create "$ROLE_ID" --project="$PROJECT_ID" --file=/tmp/aggregator-role.yaml 2>/dev/null || \
  gcloud iam roles update "$ROLE_ID" --project="$PROJECT_ID" --file=/tmp/aggregator-role.yaml

GA_SA="github-actions-aggregator"
GA_EMAIL="${GA_SA}@${PROJECT_ID}.iam.gserviceaccount.com"
gcloud iam service-accounts create "$GA_SA" \
  --display-name="GitHub Actions Monthly Aggregator" \
  --project="$PROJECT_ID" || true

gcloud projects add-iam-policy-binding "$PROJECT_ID" \
  --member="serviceAccount:${GA_EMAIL}" \
  --role="projects/${PROJECT_ID}/roles/${ROLE_ID}"

# 2. Rôle minimal pour le serveur Hugging Face MedGemma (écriture dans medgemma_ai_scores)
HF_SA="hf-medgemma-scorer"
HF_EMAIL="${HF_SA}@${PROJECT_ID}.iam.gserviceaccount.com"
gcloud iam service-accounts create "$HF_SA" \
  --display-name="Hugging Face MedGemma Async Scorer" \
  --project="$PROJECT_ID" || true

gcloud projects add-iam-policy-binding "$PROJECT_ID" \
  --member="serviceAccount:${HF_EMAIL}" \
  --role="projects/${PROJECT_ID}/roles/${ROLE_ID}"

echo ""
echo "=== Génération des clés JSON locales (À COPIER DANS LES SECRETS PUIS SUPPRIMER) ==="
gcloud iam service-accounts keys create ./github-actions-sa.json --iam-account="$GA_EMAIL"
gcloud iam service-accounts keys create ./hf-medgemma-sa.json --iam-account="$HF_EMAIL"

echo ""
echo "=============================================================================="
echo "CONFIGURATION TERMINÉE (PLAN SPARK 100% GRATUIT)"
echo "Projet Firebase : $PROJECT_ID"
echo "Base Firestore  : $DATABASE_ID"
echo ""
echo "ACTIONS MANUELLES RESTANTES :"
echo "1. Console Firebase -> Authentication -> Sign-in method -> Activer 'Google'."
echo "2. Console Firebase -> App Check -> Enregistrer l'app Android ($ANDROID_PACKAGE) avec Play Integrity / reCAPTCHA."
echo "3. GitHub Repository -> Settings -> Secrets -> Actions :"
echo "   - FIREBASE_SERVICE_ACCOUNT_JSON = contenu de ./github-actions-sa.json"
echo "   - FIRESTORE_DATABASE_ID         = $DATABASE_ID"
echo "   - FIREBASE_PROJECT_ID           = $PROJECT_ID"
echo "4. Hugging Face Space -> Settings -> Variables and secrets :"
echo "   - FIREBASE_SERVICE_ACCOUNT_JSON = contenu de ./hf-medgemma-sa.json"
echo "   - FIRESTORE_DATABASE_ID         = $DATABASE_ID"
echo "   - HF_TOKEN                      = votre token Hugging Face (accès MedGemma)"
echo "5. Supprimez les fichiers locaux ./github-actions-sa.json et ./hf-medgemma-sa.json !"
echo "=============================================================================="
