package service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GPTService {

    @Value("${gpt.api.url}")
    private String apiUrl;

    @Value("${gpt.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GPTService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateInsight(String userPreference) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // Prepare the request body according to the OpenAI API requirements
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4"); // Ensure you're specifying the correct model
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", "You are a financial assistant."),
                    Map.of("role", "user", "content", "Generate investment insights for the Indian market based on: " + userPreference)
            ));
            requestBody.put("max_tokens", 100);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                Map<String, Object> firstChoice = (Map<String, Object>) ((List<Object>) responseBody.get("choices")).get(0);
                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                return (String) message.get("content");
            }

            return "No insights available";
        } catch (HttpStatusCodeException e) {
            return "Error from GPT-4 API: " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}
