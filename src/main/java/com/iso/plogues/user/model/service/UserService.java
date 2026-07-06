package com.iso.plogues.user.model.service;


import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.DuplicateUserIdException;
import com.iso.plogues.exception.user.InvalidUserPwdException;

import com.iso.plogues.user.model.dao.UserMapper;
import com.iso.plogues.user.model.dto.MyInfoDto;
import com.iso.plogues.user.model.dto.UserDto;
import com.iso.plogues.user.model.vo.User;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final FileService fileService;

	

	@Transactional(readOnly=true)
	public MyInfoDto selectMyInfo(CustomUserDetails user) {
		log.info("{}",user.getUsername());
		return userMapper.selectMyInfo(user);
	}
	
	
	@Transactional
	public void signUp(UserDto user) {
		
		int count = userMapper.countByUserId(user.getUserId());
		
		if(count > 0) {
			throw new DuplicateUserIdException("이미 있는 아이디입니다.");
		}
		
		User userEntity = User.builder()
							  .userId(user.getUserId())
							  .userPwd(passwordEncoder.encode(user.getUserPwd()))
							  .userName(user.getUserName())
							  .email(user.getEmail())
							  .address(user.getAddress())
							  .phone(user.getPhone())
							  .info(user.getInfo())
							  .role("ROLE_USER")
							  .build();

		userMapper.signUp(userEntity);
	}
	
	@Transactional
	public void patchMyInfo(CustomUserDetails user, UserDto userInfo, MultipartFile file) {
		
		userInfo.setUserId(user.getUsername());
		int result = userMapper.patchMyInfo(userInfo);
		//파일이 있다면?
		if(result > 0 && file != null) {
			File userFile = File.of(user.getUsername(), file.getOriginalFilename(), "user");
			
			fileService.fileTransferTo(file, userFile.getChangeName(), userFile.getBoardType());
		}
		
	}

	@Transactional
	public void deleteAccount(CustomUserDetails user, Map<String,String>body) {
		matchesPassword(user, body.get("userPwd") );
		userMapper.deleteAccount(user.getUsername());
	}
	
	private void matchesPassword(CustomUserDetails user, String userPwd) {
		if(!passwordEncoder.matches(userPwd, user.getPassword())) {
			throw new InvalidUserPwdException("비밀번호가 틀렸습니다.");
		}
	}





}
