package com.iso.plogues.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.iso.plogues.configuration.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		return http.formLogin(AbstractHttpConfigurer::disable)
				   .csrf(AbstractHttpConfigurer::disable)
				   .cors(Customizer.withDefaults())
				   .authorizeHttpRequests(requests -> { 
					   requests.requestMatchers(HttpMethod.GET, "/api/notices/**", "/api/joins/**", "/api/home", "/api/boards/**", "/api/tree/**").permitAll();
					   requests.requestMatchers(HttpMethod.POST, "/api/users", "/api/auth/login", "/api/tree", "/api/auth/refresh").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/api/notices/**", "/api/joins/**", "/api/home", "/api/boards/**","/api/proof/**").permitAll();
					   requests.requestMatchers(HttpMethod.POST, "/api/users", "/api/auth/login", "api/tree", "/api/auth/refresh").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/api/users/**", "/api/chats", "/api/proof/**", "api/report").authenticated();
					   requests.requestMatchers(HttpMethod.POST, "/api/auth/logout", "/api/joins/**", "/api/question", "/api/request/**", "/api/chats" , "/api/boards", "api/notices", "/api/report", "/api/boards/*/comments", "/api/proof/**").authenticated();
					   requests.requestMatchers(HttpMethod.PATCH, "/api/users/**", "/api/request/**","/api/joins/**", "/api/chats/**","/api/boards/**", "/api/boards/*/comments/**", "/api/proof/**", "/api/report/**").authenticated();					   
					   requests.requestMatchers(HttpMethod.DELETE, "/api/users/**", "/api/joins/**", "/api/request/**", "/api/chats/**","/api/boards/**", "/api/boards/*/comments/**").authenticated();
					   requests.requestMatchers(HttpMethod.GET, "/api/report").hasRole("ADMIN");
					   requests.requestMatchers(HttpMethod.POST, "/api/notices").hasRole("ADMIN");
					   requests.requestMatchers(HttpMethod.PATCH, "/api/notices/**").hasRole("ADMIN");
					   requests.requestMatchers(HttpMethod.DELETE, "/api/notices/**").hasRole("ADMIN");
					   requests.requestMatchers(HttpMethod.POST, "/api/auth/logout", "/api/joins", "/api/question").authenticated();
					   requests.requestMatchers(HttpMethod.GET, "/api/question/**").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/api/question/**").authenticated();
					   requests.requestMatchers(HttpMethod.PATCH, "/api/question/**").authenticated();
					   requests.requestMatchers(HttpMethod.POST, "/api/question/**").authenticated();
					   requests.requestMatchers("/uploads/**").permitAll();
					   
					   
				   }).sessionManagement(manager -> 
				   						manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				   .build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174"));
		configuration.setAllowedMethods(Arrays.asList("POST", "PATCH", "DELETE", "GET", "PUT", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	
	
	
	
	

}
