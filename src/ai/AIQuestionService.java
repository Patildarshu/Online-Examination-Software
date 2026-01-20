package ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class AIQuestionService {

    // ✅ Best practice: use environment variable
    private static final String API_KEY = "AIzaSyD5LQV3C7xPLddArZM-oaFoxkXH3dJk5Ao";


    public String generateMCQs(String topic, int count) {

        try {
            Client client = Client.builder()
                    .apiKey(API_KEY)
                    .build();

            String prompt =
                    "Generate " + count + " multiple choice questions on the topic: " + topic + ". " +
                            "Each question must have exactly 4 options and one correct answer. " +
                            "Return ONLY valid JSON in this format:\n" +
                            "[{\"question\":\"\",\"options\":[\"\",\"\",\"\",\"\"],\"answer\":1}]";

            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-3-flash-preview",
                            prompt,
                            null
                    );

            return response.text(); // ✅ returns JSON array

        } catch (Exception e) {
            throw new RuntimeException("AI generation failed", e);
        }
    }
}
