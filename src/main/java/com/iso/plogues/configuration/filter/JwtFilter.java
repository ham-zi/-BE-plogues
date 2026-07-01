package com.iso.plogues.configuration.filter;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.token.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailService;

	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    
	    String uri = request.getRequestURI();
	    if (uri.equals("/api/auth/login") || uri.equals("/api/auth/refresh")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
	    log.info("요청 URI: {}, 인증 헤더: {}", uri, authorization); // <--- 이 로그를 꼭 추가하세요!

	    if(authorization == null || !authorization.startsWith("Bearer ")) {
	        log.info("인증 헤더 없음 또는 형식 오류"); // <--- 이 로그도 추가
	        filterChain.doFilter(request, response);
	        return;
	    }
		String token = authorization.substring(7);
		try {
			Claims claims = jwtUtil.parseJwt(token);
			String username = claims.getSubject();
			
			CustomUserDetails user = (CustomUserDetails)userDetailService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication
				= new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch(ExpiredJwtException e) {
			
			log.info("토큰의 유효기간 만료");
			response.setStatus(401);
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write("토큰만료");
			return;
			
		} catch(JwtException e) {
			
			log.info("이 서버의 서비스키로 만든 토큰이 아님");
			response.setStatus(401);
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write("유효하지않은토큰");
			return;
			
		}
		
		// filterChain.doFilter(request, response); 밑에삭제하
		
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
		    log.info("인증 성공! 사용자: {}", SecurityContextHolder.getContext().getAuthentication().getName());
		} else {
		    log.info("인증 실패! SecurityContext가 비어있습니다.");
		}
		filterChain.doFilter(request, response);
	}
	

}
