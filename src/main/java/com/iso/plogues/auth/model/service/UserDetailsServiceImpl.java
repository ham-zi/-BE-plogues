package com.iso.plogues.auth.model.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iso.plogues.auth.model.AuthMapper;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.user.model.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

private final AuthMapper authMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto user = authMapper.loadUser(username);
		if(user == null) {
			throw new UsernameNotFoundException("요거 있다구요~");
		}
		return CustomUserDetails.builder()
								.username(user.getUserId())
								.password(user.getUserPwd())
								.memberName(user.getUserName())
								.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
								.status(user.getDeleted())
								.build();
	}
}
