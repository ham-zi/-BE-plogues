package com.iso.plogues.ai.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
	private final ChatClient chatClient;
	
	@GetMapping("/ask")
	public String ask() {
		return chatClient.prompt()
	  			  .system("""
	  			  			You are a Korean literature professor with 5 years of teaching experience.
	  			  			
	  			  			Always respond in Korean.

							Format your response using numbered Markdown headings in the following style:
							
							## 1.
							## 2.
							## 3.
							
							Continue the numbering as needed.
							
							Never use profanity, vulgar language, or offensive expressions under any circumstances.
	  			  		""").user(u -> u.text("""
	  			  				Summarize the content below in exactly three concise lines.
	  			  				---
	  			  				{board}
	  			  				---
	  			  				""").param("board", "오늘 지역 주민들과 함께 플로깅 활동을 진행했습니다. 활동을 시작하기 전 간단한 안전 수칙을 안내하고 구역을 나누어 쓰레기를 수거했습니다. 산책로와 공원 주변에는 담배꽁초와 플라스틱 컵, 비닐 등이 예상보다 많이 버려져 있었습니다. 참여자 모두가 적극적으로 활동에 참여해 깨끗한 환경을 만드는 데 힘을 보탰습니다. 활동을 마친 후에는 수거한 쓰레기를 종류별로 분리배출하며 마무리했습니다. 짧은 시간이었지만 눈에 띄게 주변이 깨끗해져 큰 보람을 느낄 수 있었습니다. 함께한 참여자들과 활동 소감을 나누며 다음 플로깅 일정도 이야기했습니다. 앞으로도 꾸준히 환경 보호 활동에 참여해 더 많은 사람들과 의미 있는 시간을 보내고 싶습니다.")).call().content()
	  			  				+ "\n\n"
	  			  				+ "작은 실천이 모여 큰 변화를 만들 수 있다는 것을 다시 한번 느낀 하루였습니다. 다음 활동에서도 많은 분들과 함께 깨끗한 우리 동네를 만들어 가면 좋겠습니다.";
	}
	
	@PostMapping("/title")
	public String title(@RequestBody Map<String, String> content) {
		return chatClient.prompt().options(OllamaChatOptions.builder().temperature(1.3))
								  .system("""
								  			Always respond in Korean.
										
											Return exactly three titles separated by the "|" character.
											Do not use Markdown, numbering, bullet points, or any additional explanations.
											
											Example:
											첫 번째 제목 | 두 번째 제목 | 세 번째 제목
											Continue the numbering as needed.
											
											Never use profanity, vulgar language, or offensive expressions under any circumstances.
								  		""")
								  .user(u -> u.text("""
								  		Based on the content below, recommend three appropriate titles.
								  		---
								  		{title}
								  		---
								  		""").param("title", content.get("content")))
								  .call().content();
	}
	
}
