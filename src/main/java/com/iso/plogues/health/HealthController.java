package com.iso.plogues.health;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/health")
public class HealthController {
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.application.name}")
	private String name;
	
	@GetMapping
	public ResponseEntity<Map<String,Object>> healthCheck() {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("status","UP");
		res.put("timestamp",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss")));
		res.put("app",name);
		
		try (Connection conn = dataSource.getConnection()) {
			res.put("database", "UP");
		} catch (Exception e) {
			res.put("database", "DOWN");
			
		}
		
		return ResponseEntity.ok(res);
	}
	
	
	
}
