package com.iso.plogues.proof.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.CustomAuthenticationException;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedUpdateException;
import com.iso.plogues.exception.FileUploadException;
import com.iso.plogues.join.model.dto.DetailJoinDto;
import com.iso.plogues.join.model.service.JoinService;
import com.iso.plogues.proof.file.model.service.ProofFileService;
import com.iso.plogues.proof.model.dao.ProofMapper;
import com.iso.plogues.proof.model.dto.ProofDto;
import com.iso.plogues.proof.model.vo.Proof;
import com.iso.plogues.tree.model.dto.TreeCountDto;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProofService {

	private final ProofMapper proofMapper;
	private final ProofFileService proofFileService;
	private final JoinService joinService;

	@Transactional
	public void save(ProofDto proof, List<MultipartFile> files, CustomUserDetails user) {
		
		if (!"PLANT".equals(proof.getCategory()) 
	            && !"PLOG".equals(proof.getCategory())) {
	        throw new IllegalArgumentException("잘못된 카테고리입니다.");
	    }

		if (files == null || files.size() != 2) {
			throw new FileUploadException("인증 사진은 2장을 등록해야 합니다.");
		}
		
		if (proof.getQuantity() == null || proof.getQuantity() <= 0) {
		    throw new IllegalArgumentException("수거량은 0보다 커야 합니다.");
		}
		
		DetailJoinDto join = joinService.findByJoinNo(proof.getJoinNo());

		if (!join.getUserId().equals(user.getUsername())) {
		    throw new CustomAuthenticationException("모집장만 인증글을 작성할 수 있습니다.");
		}	
		
		// 해당 활동 인증글 중복 작성 방지
		int count = proofMapper.countByJoinNo(proof.getJoinNo());
		
		if (count > 0) {
		    throw new FileUploadException("이미 인증글을 작성한 활동입니다.");
		}

		Proof p = Proof.builder()
					   .title(proof.getTitle())
					   .content(proof.getContent())
					   .userId(user.getUsername())
					   .category(proof.getCategory())
					   .joinNo(proof.getJoinNo())
					   .quantity(proof.getQuantity())
					   .build();

		int result = proofMapper.save(p);

		if (result == 0) {
			throw new FileUploadException("게시글 작성에 실패하였습니다.");
		}

		// 파일 저장
		proofFileService.saveProofFiles(files, p.getProofNo());

	}

	@Transactional(readOnly = true)
	public ProofDto findByProofNo(Long proofNo) {

		ProofDto proof = proofMapper.findByProofNo(proofNo);

		if (proof == null) {
			throw new FailedFindByNoException("게시글 조회에 실패했습니다.");
		}

		List<FileDto> fileList = proofFileService.findByBno(proofNo);

		proof.setFiles(fileList);

		return proof;
	}

	private PageInfo newPageInfo(int listCount, int page) {

		return PageInfo.of(listCount, page, 8, 5);

	}

	@Transactional(readOnly=true)
	public BoardResponse<ProofDto> findAll(int page, String category) {
		validCategory(category);
		
		PageInfo pageInfo = newPageInfo(proofMapper.listCount(category), page);

		List<ProofDto> list = proofMapper.findAll(pageInfo, category);

		return new BoardResponse<>(pageInfo, list);
	}
	
	private String validCategory(String category) {
		if ("PLANT".equals(category) || "PLOG".equals(category)) {
		    return category;
		}
		return "ALL";
	}

	@Transactional
	public void deleteProof(CustomUserDetails user, Long proofNo) {

		findByProofNo(proofNo);

		int result = proofMapper.deleteProof(user.getUsername(), proofNo);

		throwDeleteException(result);

		proofFileService.deleteFile(proofNo);

	}

	private void throwDeleteException(int result) {

		if (result != 1) {
			throw new FailedDeleteException("게시글 삭제에 실패했습니다.");
		}

	}

	@Transactional
	public void updateProof(CustomUserDetails user, Long proofNo, ProofDto proof, List<MultipartFile> files) {

		findByProofNo(proofNo);
		
		if (proof.getQuantity() == null || proof.getQuantity() <= 0) {
		    throw new IllegalArgumentException("수거량은 0보다 커야 합니다.");
		}

		Proof proofEntity = Proof.builder()
								 .proofNo(proofNo)
								 .userId(user.getUsername())
								 .title(proof.getTitle())

								 .category(proof.getCategory())
								 .content(proof.getContent())
								 .quantity(proof.getQuantity())
								 .build();

		int result = proofMapper.updateProof(proofEntity);

		throwUpdateException(result);

		proofFileService.updateFile(files, proofNo, "proof");

	}

	private void throwUpdateException(int result) {

		if (result != 1) {
			throw new FailedUpdateException("게시글 수정에 실패했습니다.");
		}

	}
	
	public List<TreeCountDto> getTreeCountByYear() {
		return proofMapper.treeCountByYear();
	}
	
}