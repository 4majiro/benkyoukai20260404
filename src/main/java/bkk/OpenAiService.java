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

	// OpenAIの形式に合わせた内部クラス（recordが楽です）
	public record ChatRequest(String model, List<Message> messages) {
	}

	public record Message(String role, String content) {
	}

	public String ask(String systemPrompt, String userPrompt) {
		// 1. URLを正しいものに修正（重要！）
		String url = "https://api.openai.com/v1/chat/completions";

		// 2. プロンプト作成（Java内での改行は自由）
		//String systemPrompt = "あなたは親切なアシスタントです。\n挨拶、星座、誕生日のトリビアを、適宜改行を入れて回答してください。";
		//String userPrompt = String.format("名前:%s\n性別:%s\n誕生日:%s", name, gender, birthday);

		try {
			// 3. DTOを使ってデータを組み立てる（これでエスケープが自動化されます）
			ChatRequest request = new ChatRequest("gpt-4o-mini", List.of(
					new Message("system", systemPrompt),
					new Message("user", userPrompt)));

			// 4. ObjectMapperでJSON文字列に変換
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
			// 修正後のパス
			return root.path("choices").get(0).path("message").path("content").asText();
		} catch (Exception e) {
			return "解析エラー";
		}
	}
}
