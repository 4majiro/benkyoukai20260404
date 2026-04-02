package bkk;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "https://play.vuejs.org")
public class ApiController {

    private final OpenAiService service;

    public ApiController(OpenAiService service) {
        this.service = service;
    }

    @PostMapping("/greet")
    public String greet(@RequestBody(required =  false) String name) {
    	String answer = "こんにちは";
    	if(name != null && !name.isBlank()) {
    		answer += "、" + name + "さん"; 
    	}
    	answer += "！";    	
        return answer;
    }
    
    @PostMapping("/teachMe")
    public String teachMe(@RequestBody UserInfo userInfo) {
    	
    	String systemPrompt = """
    		    あなたは親切なパーソナルアシスタントです。
    		    提供されたユーザー情報（名前、性別、誕生日）をもとに、以下の3点を含むフレンドリーなメッセージを作成してください。
    		    1. 挨拶：
    		    ユーザーの名前を呼び、性別に合わせた自然な挨拶をしてください。
    		    （例：こんにちは、〇〇さん！）
    		    2. 星座とアドバイス：
    		    誕生日から正確な「星座」を特定し、その星座の今日の運勢や特徴を1文添えてください。
    		    3. 誕生日のトリビア：
    		    その「誕生日（月日）」が歴史的にどのような日か、あるいは「今日は何の日」的な記念日や出来事を1つ紹介してください。
    		    【出力ルール】
    		    - 各項目ごとに適宜改行を入れ、読みやすく構成してください。
    		    - 装飾（マークダウンの太字など）は最小限にしてください。
    		    """;

    		String userPrompt = String.format("""
    		    以下の情報をもとにメッセージを作成してください。

    		    名前: %s 
    		    性別: %s (M:男性, F:女性, O:その他)
    		    誕生日: %s (mmdd)
    		    """, userInfo.getName(), userInfo.getGender(), userInfo.getBirthday());
    	
        return service.ask(systemPrompt, userPrompt);
    }
    
    
    
    
    
}
