package com.iso.plogues.exception;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.exception.report.DuplicateReportException;
import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.exception.user.InvalidUserPwdException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
	
	@ExceptionHandler(InValidJoinRequestException.class)
	public ResponseEntity<ApiResponse> handlerInValidJoinRequest(InValidJoinRequestException e){
		return ResponseEntity.badRequest().body(ApiResponse.conplict(e.getMessage(), null));
	}
	
	@ExceptionHandler(FailedInsertException.class)
	public ResponseEntity<ApiResponse> handlerFailedInsert(FailedInsertException e){
		return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage(), null));
	}
	
	@ExceptionHandler(FailedUpdateException.class)
	public ResponseEntity<ApiResponse> handlerFailedUpdate(FailedUpdateException e){
		return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage(), null));
	}
	
	@ExceptionHandler(FailedDeleteException.class)
	public ResponseEntity<ApiResponse> handlerFailedDelete(FailedDeleteException e){
		return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage(), null));
	}

	@ExceptionHandler(FailedFindByNoException.class)
	public ResponseEntity<ApiResponse> handlerFailedFindByNo(FailedFindByNoException e){
		return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage(), null));
	}
	
	@ExceptionHandler(FailedFindAllException.class)
	public ResponseEntity<ApiResponse> handlerFailedFindAll(FailedFindAllException e){
		return ResponseEntity.badRequest().body(ApiResponse.badRequest(e.getMessage(), null));
	}

	@ExceptionHandler(DuplicateReportException.class)
	public ResponseEntity<ApiResponse> handlerDuplicateReport(DuplicateReportException e){
		return ResponseEntity.badRequest().body(ApiResponse.conplict(e.getMessage(), null));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException e) {
	    List<String> messages = e.getConstraintViolations()
	            .stream()
	            .map(ConstraintViolation::getMessage)
	            .toList();

	    return ResponseEntity
	            .badRequest()
	            .body(ApiResponse.badRequest("입력값 검증에 실패했습니다.", messages));
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiResponse<Void>> handleMaxUploadSize(MaxUploadSizeExceededException e) {
	    return ResponseEntity
	            .badRequest()
	            .body(ApiResponse.badRequest("사진 용량은 100MB 이하만 가능합니다.", null));
	}

}
