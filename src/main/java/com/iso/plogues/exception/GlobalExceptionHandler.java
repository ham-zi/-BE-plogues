package com.iso.plogues.exception;

import org.springframework.http.ResponseEntity;

import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.exception.CustomAuthenticationException;
import com.iso.plogues.exception.user.InvalidUserPwdException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DuplicateUserIdException.class)
	public ResponseEntity<ApiResponse> handlerDuplicateId(DuplicateUserIdException e){
		ApiResponse ar = new ApiResponse(400, e.getMessage(), null);
		return ResponseEntity.badRequest().body(ar);
	}
	
	@ExceptionHandler(CustomAuthenticationException.class)
	public ResponseEntity<ApiResponse> handlerAuthenticationError(CustomAuthenticationException e){
		return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
	}
	
	
	@ExceptionHandler(FileUploadException.class)
	public ResponseEntity<ApiResponse> handlerFileUpload(FileUploadException e){
		return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> handlerIllegalArgument(IllegalArgumentException e){
		return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
	}

	@ExceptionHandler(InvalidUserPwdException.class)
	public ResponseEntity<ApiResponse> handlerInvalidUserPwd(InvalidUserPwdException e){
		return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage(), null));
	}
	

	

}
