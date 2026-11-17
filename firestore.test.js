const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_clinician_1";
const BOB_UID = "bob_clinician_2";
const ADMIN_EMAIL = "mandupaix@gmail.com";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated users are denied across all collections", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("evaluations").get()
  );
  await assertFails(
    unauthDb.collection("anonymous_evaluations").doc("eval_1").set({
      evaluationId: "eval_1",
      evaluatorUid: ALICE_UID,
    })
  );
  await assertFails(
    unauthDb.collection("medgemma_ai_scores").doc("eval_1").get()
  );
  await assertFails(
    unauthDb.collection("ai_model_config").doc("current").get()
  );
  await assertFails(
    unauthDb.collection("score_catalog").where("active", "==", true).get()
  );
});

test("UserEvaluation: owner create, read, partial update, and cross-user isolation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date(Date.now() - 1000);

  const validEval = {
    userId: ALICE_UID,
    scoreId: "glasgow",
    scoreName: "Glasgow Coma Scale (GCS)",
    specialty: "Neurology",
    clinicalScoreValue: 13,
    clinicalScoreDisplay: "13 / 15",
    severityLabel: "Moderate",
    diagnosis: "Traumatisme crânien léger à modéré",
    interventionType: "Surveillance neurologique",
    breakdownSummary: "Yeux: 4 | Verbal: 4 | Moteur: 5",
    encryptedClinicalNotesCiphertext: "ZW5jcnlwdGVk",
    encryptedClinicalNotesIv: "aXYxMjM0NTY=",
    language: "fr",
    createdAt: now,
    updatedAt: now,
  };

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("evaluations")
      .doc("eval_gcs_1")
      .set(validEval)
  );

  // List own evaluations succeeds
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("evaluations").get()
  );

  // Partial update of Gemini recommendation succeeds
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("evaluations")
      .doc("eval_gcs_1")
      .update({
        geminiRecommendation: "Surveillance horaire du score de Glasgow.",
        updatedAt: new Date(Date.now() - 500),
      })
  );

  // Reject immutable createdAt modification
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("evaluations")
      .doc("eval_gcs_1")
      .update({
        createdAt: new Date(Date.now() - 500),
        updatedAt: new Date(Date.now() - 500),
      })
  );

  // Reject ghost field / @DocumentId 'id' leakage
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("evaluations")
      .doc("eval_gcs_2")
      .set({ ...validEval, id: "eval_gcs_2" })
  );

  // Cross-user isolation: Bob cannot read or write Alice's evaluations
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("evaluations")
      .doc("eval_gcs_1")
      .get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("evaluations").get()
  );
});

test("anonymous_evaluations: authenticated create succeeds, all reads/updates/deletes fail", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date(Date.now() - 1000);

  await assertSucceeds(
    aliceDb.collection("anonymous_evaluations").doc("eval_anon_1").set({
      evaluationId: "eval_anon_1",
      evaluatorUid: ALICE_UID,
      scoreId: "glasgow",
      specialty: "Neurology",
      clinicalScoreValue: 14,
      severityLabel: "Mild",
      diagnosis: "Observation post-chute",
      inputs: { eye: 4, verbal: 4, motor: 6 },
      language: "fr",
      createdAt: now,
    })
  );

  // Reject PII / unexpected fields like nom, prenom, etablissement
  await assertFails(
    aliceDb.collection("anonymous_evaluations").doc("eval_anon_pii").set({
      evaluationId: "eval_anon_pii",
      evaluatorUid: ALICE_UID,
      scoreId: "glasgow",
      specialty: "Neurology",
      clinicalScoreValue: 14,
      severityLabel: "Mild",
      diagnosis: "Observation",
      nom: "Dupont",
      inputs: { eye: 4, verbal: 4, motor: 6 },
      language: "fr",
      createdAt: now,
    })
  );

  // Reads forbidden for clients
  await assertFails(
    aliceDb.collection("anonymous_evaluations").doc("eval_anon_1").get()
  );
  await assertFails(aliceDb.collection("anonymous_evaluations").get());
  await assertFails(
    aliceDb
      .collection("anonymous_evaluations")
      .doc("eval_anon_1")
      .update({ diagnosis: "Modified" })
  );
});

test("medgemma_ai_scores: client writes denied, owner can get/list (including non-existent doc listener)", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();

  // Client cannot write medgemma_ai_scores directly
  await assertFails(
    aliceDb.collection("medgemma_ai_scores").doc("eval_1").set({
      evaluationId: "eval_1",
      evaluatorUid: ALICE_UID,
      scoreId: "glasgow",
      improvementScore: 88,
      confidence: 0.91,
      clinicalInsights: "Fake client write",
      modelVersion: "medgemma-1.5-4b-4bit",
      computedAt: new Date(Date.now() - 1000),
    })
  );

  // Listening to a not-yet-created score doc succeeds (resource == null)
  await assertSucceeds(
    aliceDb.collection("medgemma_ai_scores").doc("eval_pending").get()
  );

  // Seed via admin rules disabled (simulating HF Space service account)
  await testEnv.withSecurityRulesDisabled(async (ctx) => {
    await ctx.firestore().collection("medgemma_ai_scores").doc("eval_1").set({
      evaluationId: "eval_1",
      evaluatorUid: ALICE_UID,
      scoreId: "glasgow",
      improvementScore: 85,
      confidence: 0.89,
      clinicalInsights: "Stable neurological trajectory",
      modelVersion: "medgemma-1.5-4b-4bit",
      computedAt: new Date(Date.now() - 1000),
    });
  });

  await assertSucceeds(
    aliceDb.collection("medgemma_ai_scores").doc("eval_1").get()
  );
  await assertSucceeds(
    aliceDb
      .collection("medgemma_ai_scores")
      .where("evaluatorUid", "==", ALICE_UID)
      .get()
  );

  // Bob cannot read Alice's MedGemma score and unfiltered list is denied
  await assertFails(
    bobDb.collection("medgemma_ai_scores").doc("eval_1").get()
  );
  await assertFails(aliceDb.collection("medgemma_ai_scores").get());
});

test("Admin security: email spoofing without email_verified fails; verified admin manages score_catalog and ai_model_config", async () => {
  const spoofedAdminDb = testEnv
    .authenticatedContext("spoof_uid", {
      email: ADMIN_EMAIL,
      email_verified: false,
    })
    .firestore();
  const realAdminDb = testEnv
    .authenticatedContext("real_admin_uid", {
      email: ADMIN_EMAIL,
      email_verified: true,
    })
    .firestore();
  const claimAdminDb = testEnv
    .authenticatedContext("claim_admin_uid", {
      admin: true,
    })
    .firestore();
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date(Date.now() - 1000);

  const scoreItem = {
    scoreId: "custom_sofa_2",
    name: "Custom SOFA Ext",
    specialty: "Soins Intensifs",
    description: "Score personnalisé administrateur",
    license: "MIT",
    source: "Medical Calculator MCP",
    version: "1.0.0",
    active: true,
    minScore: 0,
    maxScore: 24,
    updatedAt: now,
  };

  // Spoofed email without verification must fail
  await assertFails(
    spoofedAdminDb.collection("score_catalog").doc("custom_sofa_2").set(scoreItem)
  );
  // Non-admin clinician cannot write score_catalog
  await assertFails(
    aliceDb.collection("score_catalog").doc("custom_sofa_2").set(scoreItem)
  );

  // Verified admin email & custom claim admin succeed
  await assertSucceeds(
    realAdminDb.collection("score_catalog").doc("custom_sofa_2").set(scoreItem)
  );
  await assertSucceeds(
    claimAdminDb.collection("ai_model_config").doc("current").set({
      totalEvaluations: 42,
      modelMaturity: "CALIBRATING",
      awellScoreVersion: "1.3.0",
      medicalCalcMcpVersion: "2.1.0",
      hfSpaceUrl: "https://huggingface.co/spaces/medical-score-pro/medgemma",
      latestSocietyNotice: "ESC 2026 Guidelines checked",
      updatedAt: now,
    })
  );

  // Clinician can read active score_catalog and ai_model_config/current
  await assertSucceeds(
    aliceDb.collection("score_catalog").where("active", "==", true).get()
  );
  await assertSucceeds(
    aliceDb.collection("ai_model_config").doc("current").get()
  );
  // Unfiltered score_catalog query fails for non-admin
  await assertFails(aliceDb.collection("score_catalog").get());
});
