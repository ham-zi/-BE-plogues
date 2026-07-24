package com.iso.plogues.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
	@Bean
	public ChatClient chatClient(ChatClient.Builder builder) {
		return builder.defaultSystem("너는 척척박사다. 물어보는 건 뭐든 다 대답해라").build();
	}

}
