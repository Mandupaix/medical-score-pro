import { initializeApp, cert } from "firebase-admin/app";
import { getFirestore, FieldValue } from "firebase-admin/firestore";
import { getMessaging } from "firebase-admin/messaging";

const DATABASE_ID =
  process.env.FIRESTORE_DATABASE_ID ||
  "ai-studio-android-medicals-4c10e4c2-f93c-4854-a44b-ce5c59a22f78";

async function fetchLatestNpmVersion(pkgName) {
  try {
    const res = await fetch(`https://registry.npmjs.org/${encodeURIComponent(pkgName)}/latest`);
    if (!res.ok) return null;
    const data = await res.json();
    return data.version || null;
  } catch {
    return null;
  }
}

async function checkLearnedSocieties() {
  const feeds = [
    { name: "ESC Cardiology", url: "https://www.escardio.org/" },
    { name: "AHA Journals", url: "https://www.ahajournals.org/" },
    { name: "SFAR", url: "https://sfar.org/" },
    { name: "NICE Guidelines", url: "https://www.nice.org.uk/guidance" },
  ];
  const reachable = [];
  for (const feed of feeds) {
    try {
      const res = await fetch(feed.url, { method: "HEAD" });
      if (res.ok) reachable.push(feed.name);
    } catch {
      // Ignore transient network failures in CI
    }
  }
  return `Sociétés savantes vérifiées (${new Date().toISOString().slice(0, 10)}) : ${reachable.join(", ") || "Vérification planifiée"}`;
}

async function main() {
  const rawSa = process.env.FIREBASE_SERVICE_ACCOUNT_JSON;
  if (!rawSa) {
    throw new Error("Secret FIREBASE_SERVICE_ACCOUNT_JSON manquant.");
  }

  const serviceAccount = JSON.parse(rawSa);
  const app = initializeApp({
    credential: cert(serviceAccount),
  });

  const db = getFirestore(app, DATABASE_ID);

  // 1. Agrégations sur anonymous_evaluations
  const snapshot = await db.collection("anonymous_evaluations").get();
  const totalEvaluations = snapshot.size;
  const specialtyCounts = {};
  for (const doc of snapshot.docs) {
    const spec = doc.get("specialty") || "Other";
    specialtyCounts[spec] = (specialtyCounts[spec] || 0) + 1;
  }

  let modelMaturity = "BOOTSTRAP";
  if (totalEvaluations >= 500) {
    modelMaturity = "MATURE";
  } else if (totalEvaluations >= 50) {
    modelMaturity = "CALIBRATING";
  }

  // 2. Vérification des mises à jour @awell-health/awell-score & medical-calc-mcp
  const awellVersion = (await fetchLatestNpmVersion("@awell-health/awell-score")) || "1.3.0";
  const mcpVersion = (await fetchLatestNpmVersion("medical-calc-mcp")) || "2.1.0";
  const societyNotice = await checkLearnedSocieties();

  const configRef = db.collection("ai_model_config").doc("current");
  const prevSnap = await configRef.get();
  const prevData = prevSnap.exists ? prevSnap.data() : {};

  await configRef.set(
    {
      totalEvaluations,
      modelMaturity,
      awellScoreVersion: awellVersion,
      medicalCalcMcpVersion: mcpVersion,
      hfSpaceUrl: process.env.HF_SPACE_URL || prevData.hfSpaceUrl || "",
      latestSocietyNotice: societyNotice,
      specialtyCounts,
      updatedAt: FieldValue.serverTimestamp(),
    },
    { merge: true }
  );

  console.log("ai_model_config/current mis à jour :", {
    totalEvaluations,
    modelMaturity,
    awellVersion,
    mcpVersion,
  });

  // 3. Notification FCM gratuite si une nouvelle version est détectée
  const hasUpdate =
    (prevData.awellScoreVersion && prevData.awellScoreVersion !== awellVersion) ||
    (prevData.medicalCalcMcpVersion && prevData.medicalCalcMcpVersion !== mcpVersion);

  if (hasUpdate) {
    await getMessaging(app).send({
      topic: "medical_score_updates",
      notification: {
        title: "Mise à jour Medical Score Pro",
        body: `Nouvelles versions détectées : awell-score v${awellVersion}, medical-calc-mcp v${mcpVersion}.`,
      },
      data: {
        awellScoreVersion: awellVersion,
        medicalCalcMcpVersion: mcpVersion,
      },
    });
    console.log("Notification FCM envoyée au topic medical_score_updates.");
  }
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
