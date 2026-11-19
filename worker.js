export default {
  async fetch(request, env) {
    // Vérifier que la méthode est POST
    if (request.method !== "POST") {
      return new Response("Méthode non autorisée", { status: 405 });
    }

    try {
      // Récupérer le corps de la requête envoyé par l'application mobile
      const requestBody = await request.json();

      // URL de l'API Hugging Face (compatible OpenAI)
      const HF_API_URL = "https://router.huggingface.co/v1/chat/completions";

      // Appeler Hugging Face avec la clé stockée en secret (env.HF_API_KEY)
      const response = await fetch(HF_API_URL, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${env.HF_API_KEY}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
      });

      // Récupérer et renvoyer la réponse
      const data = await response.text();
      return new Response(data, {
        status: response.status,
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
      });
    } catch (error) {
      return new Response(JSON.stringify({ error: error.message }), {
        status: 500,
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
        },
      });
    }
  },
};
