package com.iso.plogues.proof.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FileUploadException;
import com.iso.plogues.proof.file.model.service.ProofFileService;
import com.iso.plogues.proof.model.dao.ProofMapper;
import com.iso.plogues.proof.model.dto.ProofDto;
import com.iso.plogues.proof.model.vo.Proof;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProofService {
	
	private final ProofMapper proofMapper;
	private final ProofFileService proofFileService;
	
	@Transactional
    public void save(ProofDto proof, List<MultipartFile> files, CustomUserDetails user) {
        
       
		Proof p = Proof.builder()
		        .title(proof.getTitle())
		        .content(proof.getContent())
		        .userId(user.getUsername())
		        .category(proof.getCategory())
		        .joinNo(proof.getJoinNo())
		        .quantity(proof.getQuantity()) 
		        .build();

        // 2. 인증 게시글 저장                  
        int result = proofMapper.save(p);
       
        
        if (result == 0) {
            throw new FileUploadException("게시글 작성에 실패하였습니다. 잠시 후에 다시 시도해주세요.");
        }	
       
        if (files != null && !files.isEmpty()) {
            proofFileService.saveProofFiles(files, p.getProofNo());
        }
    }
	
	
	
}
