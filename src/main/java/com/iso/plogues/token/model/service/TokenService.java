package com.iso.plogues.token.model.service;

import java.util.Map;


import org.springframework.stereotype.Service;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.CustomAuthenticationException;
import com.iso.plogues.token.model.dao.TokenMapper;
import com.iso.plogues.token.model.vo.RefreshToken;
import com.iso.plogues.token.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
	private final JwtUtil tokenUtil;
	private final TokenMapper tokenMapper;

	public Map<String, String> getTokens(CustomUserDetails user) {
		
		Map<String, String> tokens = createTokens(user);
		saveToken(tokens.get("refreshToken"), user.getUsername());
		return tokens;
	}

	private Map<String, String> createTokens(CustomUserDetails user) {
		return Map.of("accessToken", tokenUtil.getAccessToken(user),
					  "refreshToken", tokenUtil.getRefreshToken(user));
	}

	private void saveToken(String token, String memberId) {
		RefreshToken refreshToken = RefreshToken.builder()
												.userId(memberId)
												.token(token)
												.expiration(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3))
												.build();
		tokenMapper.saveToken(refreshToken);
	}
	
	public Map<String, String> tokenRotation(String refreshToken){
		RefreshToken token = tokenMapper.findByToken(refreshToken);
		Claims claims = tokenUtil.parseJwt(token.getToken());
		String userId = claims.getSubject();
		String memberName = (String)claims.get("memberName");
		CustomUserDetails user = CustomUserDetails.builder().memberName(memberName).username(userId).build();
		hasRefreshToken(token);
		Map<String, String> tokens = createTokens(user);
		saveToken(tokens.get("refreshToken"), userId);
		return tokens;
	}
	
	private void hasRefreshToken(RefreshToken token) {
		if(token == null) {
			throw new CustomAuthenticationException("일치하는  토큰이 없습니다.");
		}
		if(token.getExpiration() < System.currentTimeMillis()) {
			tokenMapper.deleteToken(token.getUserId(), token.getToken());
			throw new CustomAuthenticationException("유효하지않은 토큰입니다.");
		}
	}

	public void deleteToken(CustomUserDetails user, String refreshToken) {
		tokenMapper.deleteToken(user.getUsername(), refreshToken);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
