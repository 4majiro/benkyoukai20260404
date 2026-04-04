package bkk;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String greet(@RequestBody(required = false) String name) {
		String answer = "こんにちは";
		if (name != null && !name.isBlank()) {
			answer += "、" + name + "さん";
		}
		answer += "！";
		return answer;
	}

	@PostMapping("/speak")
	public String speak(@RequestBody UserInfo userInfo) {
		
		if (userInfo.isAllEmptyOrBlank()) {
			return "こんにちは！";
		}
		if (userInfo.isOnlyNamePresent()) {
			return "こんにちは、" + userInfo.getName() + "さん！";
		}
		
		String systemPrompt = """
				あなたは親切なパーソナルアシスタントです。
				提供されたユーザー情報
				（名前、性別、血液型、身長、体重、BMI、好きな食べ物(複数可)、生年月日(YYYYMMDD)）
				をもとに、メッセージを作成してください。
				【抽出ルール】
				・値が入力されている項目のみを情報のソースとして使用してください。
				・未入力（null、ブランク）の項目は、この世に存在しないものとして扱い、一切触れないでください。
				・足りない情報を補填するような提案や、入力を促すアドバイスは厳禁です。
				・どのような回答ルールで回答しているかを伝えるのは厳禁です。
				【回答ルール】
				・最初にユーザーの名前を呼び、自然な挨拶をしてください。
				（例：こんにちは、〇〇さん！）
				・適宜改行コードを入れてください。
				・好きな食べ物のなかからランダムでどれかひとつに共感して返して下さい。
				 (具体的にどういうところがいいのかも添えて)
				・生年月日をもとに、以下のいずれかを行ってください。
				　①星座を特定し運勢を伝える
				　②生まれた日に起こったニュースを紹介する
				・その他特徴的な情報があれば、それを優先してコメントしてください。
				・いい加減な入力も可能なので、
				(例：生年月日が99999999、BMIが1000など)
				そういう場合は上記ルールを無視して冗談ぽいメッセージを返してください。
				""";

		String userPrompt = String.format("""
				以下の情報をもとにメッセージを作成してください。

				名前: %s
				性別: %s (M:男性, F:女性, O:その他)
				血液型: %s
				身長: %s
				体重: %s
				BMI: %s
				好きな食べ物: %s
				生年月日: %s (yyyymmdd)
				""", 
				userInfo.getName(), 
				userInfo.getGender(), 
				userInfo.getBloodType(), 
				userInfo.getHeight(), 
				userInfo.getWeight(), 
				userInfo.getBmi(), 
				String.join(",", userInfo.getFavoriteFood()), 
				userInfo.getDateOfBirth());

		return service.ask(systemPrompt, userPrompt);
	}

	@GetMapping(value="/foods", produces = "application/json")
	public String foods() {
		return """
				{
				  "1": "カレーライス",
				  "2": "ハンバーグ",
				  "3": "オムライス",
				  "4": "とんかつ",
				  "5": "唐揚げ",
				  "6": "ステーキ",
				  "7": "焼肉",
				  "8": "すき焼き",
				  "9": "しゃぶしゃぶ",
				  "10": "ローストビーフ",
				  "11": "照り焼きチキン",
				  "12": "餃子",
				  "13": "麻婆豆腐",
				  "14": "エビチリ",
				  "15": "生姜焼き",
				  "16": "ラーメン",
				  "17": "うどん",
				  "18": "そば",
				  "19": "カルボナーラ",
				  "20": "ミートソースパスタ",
				  "21": "ナポリタン",
				  "22": "焼きそば",
				  "23": "冷やし中華",
				  "24": "担々麺",
				  "25": "ほうとう",
				  "26": "寿司",
				  "27": "刺身",
				  "28": "天ぷら",
				  "29": "焼き鮭",
				  "30": "鯖の味噌煮",
				  "31": "親子丼",
				  "32": "牛丼",
				  "33": "天丼",
				  "34": "お好み焼き",
				  "35": "たこ焼き",
				  "36": "サンドイッチ",
				  "37": "ハンバーガー",
				  "38": "ピザ",
				  "39": "グラタン",
				  "40": "ドリア",
				  "41": "フレンチトースト",
				  "42": "いちご",
				  "43": "メロン",
				  "44": "もも",
				  "45": "チョコレート",
				  "46": "アイスクリーム",
				  "47": "プリン",
				  "48": "ショートケーキ",
				  "49": "パンケーキ",
				  "50": "チーズ"
				}
				""";
	}

}
