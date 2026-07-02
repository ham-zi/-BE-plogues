package com.iso.plogues.proof.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.FileUploadException;
import com.iso.plogues.proof.file.model.service.ProofFileService;
import com.iso.plogues.proof.model.dao.ProofMapper;
import com.iso.plogues.proof.model.dto.ProofDto;
import com.iso.plogues.proof.model.vo.Proof;
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

	@Transactional
	public void save(ProofDto proof, List<MultipartFile> files, CustomUserDetails user) {

		// 사진 2장 필수
		if (files == null || files.size() != 2) {
			throw new FileUploadException("인증 사진은 2장을 등록해야 합니다.");
		}

		Proof p = Proof.builder().title(proof.getTitle()).content(proof.getContent()).userId(user.getUsername())
				.category(proof.getCategory()).joinNo(proof.getJoinNo()).quantity(proof.getQuantity()).build();

		// 게시글 저장
		int result = proofMapper.save(p);

		if (result == 0) {
			throw new FileUploadException("게시글 작성에 실패하였습니다.");
		}

		// 사진 2개 각각 저장
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
		return PageInfo.of(listCount, page, 10, 5);
	}

	@Transactional
	public BoardResponse<ProofDto> findAll(int page) {

		PageInfo pageInfo = newPageInfo(proofMapper.listCount(), page);

		List<ProofDto> list = proofMapper.findAll(pageInfo);

		BoardResponse<ProofDto> br = new BoardResponse<>();

		br.setPage(pageInfo);
		br.setBoard(list);

		return br;
	}

}