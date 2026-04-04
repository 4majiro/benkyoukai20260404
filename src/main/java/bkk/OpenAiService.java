package bkk;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAiService {

	@Value("${openai.api.key}")
	private String apiKey;

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper mapper = new ObjectMapper();

	public record ChatRequest(String model, List<Message> messages) {
	}

	public record Message(String role, String content) {
	}

	public String ask(String systemPrompt, String userPrompt) {
		String url = "https://api.openai.com/v1/chat/completions";

		try {
			ChatRequest request = new ChatRequest("gpt-5.4-nano", List.of(
					new Message("system", systemPrompt),
					new Message("user", userPrompt)));

			String jsonBody = mapper.writeValueAsString(request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(apiKey);

			HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

			String responseJson = restTemplate.postForObject(url, entity, String.class);
			return extractText(responseJson);

		} catch (Exception e) {
			return "エラー発生: " + e.getMessage();
		}
	}

	private String extractText(String json) {
		try {
			JsonNode root = mapper.readTree(json);
			return root.path("choices").get(0).path("message").path("content").asText();
		} catch (Exception e) {
			return "解析エラー";
		}
	}
}
