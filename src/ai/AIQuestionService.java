package ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.apache.http.HttpException;


public class AIQuestionService {

    private static final String API_KEY = "your api key";

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
                            "gemini-3-flash-preview", // ✅ Valid SDK model
                            prompt,
                            null
                    );

            return response.text(); // ✅ Clean AI output

        } catch (Exception e) {
            throw new RuntimeException("AI generation failed", e);
        }
    }
}
