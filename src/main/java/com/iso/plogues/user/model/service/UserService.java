package com.iso.plogues.user.model.service;


import java.io.IOException;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.DuplicateUserIdException;
import com.iso.plogues.exception.FileUploadException;
import com.iso.plogues.exception.user.InvalidUserPwdException;
import com.iso.plogues.user.file.FileMapper;
import com.iso.plogues.user.model.dao.UserMapper;
import com.iso.plogues.user.model.dto.MyInfoDto;
import com.iso.plogues.user.model.dto.UserDto;
import com.iso.plogues.user.model.vo.User;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserMapper userMapper;
	private final FileMapper fileMapper;
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
		log.info("{}, {}",file, result);
		if(result == 1 && file != null) {
			changeUserFile(user, file);
		}
	}
	
	private void changeUserFile(CustomUserDetails user, MultipartFile file) {
		File userFile = File.of(user.getUsername(), file.getOriginalFilename(), "user");
		log.info("usernameuansemr~!@~!@{}",userFile);
		fileMapper.deleteFile(user.getUsername());
		int saveResult = fileMapper.saveFile(user.getUsername(),userFile);
		invalidSaveFile(saveResult);
		try {
			fileService.deleteFile(userFile);
		} catch (IOException e) {
			throw new FileUploadException("파일 업로드에 실패했습니다.");
		}
		fileService.fileTransferTo(file, userFile.getChangeName(), userFile.getBoardType());
	}
	
	private void invalidSaveFile(int result) {
		if(result != 1) {
			throw new FileUploadException("파일 업로드에 실패했습니다.");
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
