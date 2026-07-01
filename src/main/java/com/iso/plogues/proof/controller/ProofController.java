package com.iso.plogues.proof.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.board.model.dto.BoardDto;
import com.iso.plogues.board.model.service.BoardService;
import com.iso.plogues.proof.model.dto.ProofDto;
import com.iso.plogues.proof.model.service.ProofService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/proof")
@RequiredArgsConstructor
public class ProofController {
	
	
	private final ProofService proofService;
	

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> save(@Valid@ModelAttribute ProofDto proof,
	@RequestParam(name="file", required=false)  List<MultipartFile> files,
	@AuthenticationPrincipal CustomUserDetails user){
	// 제약조건valid 리스트로 이미지파일 2개 받고, 유저검증하기
		System.out.println("user = " + user);
		
		proofService.save(proof, files, user);
	
	
	// 복수로 받으니 files
	return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(null));
	}

}



