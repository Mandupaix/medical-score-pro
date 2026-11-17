---
title: Medical Score Pro - MedGemma Scorer
emoji: 🩺
colorFrom: blue
colorTo: green
sdk: docker
app_port: 7860
pinned: false
license: mit
---

# Medical Score Pro — Serveur d'Inférence MedGemma 1.5 4B (4-bit)

Serveur FastAPI gratuit hébergé sur **Hugging Face Spaces (CPU Basic)** remplaçant les Cloud Functions Firebase.

## Variables / Secrets à configurer dans Space Settings
- `FIREBASE_SERVICE_ACCOUNT_JSON` : JSON complet du compte de service `hf-medgemma-scorer`
- `FIRESTORE_DATABASE_ID` : `ai-studio-android-medicals-4c10e4c2-f93c-4854-a44b-ce5c59a22f78`
- `HF_TOKEN` : Token Hugging Face autorisé pour `google/medgemma-4b-it`
- `MEDGEMMA_MODEL_ID` : `google/medgemma-4b-it` (ou modèle MedGemma 1.5 4B quantifié)
