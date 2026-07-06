package com.iso.plogues.proof.file.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.FileUploadException;
import com.iso.plogues.proof.file.model.dao.ProofFileMapper;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProofFileService {

	private final ProofFileMapper proofFileMapper;
	private final FileService fileService;

	@Transactional
	public void saveProofFiles(List<MultipartFile> files, Long proofNo) {

		String boardType = "proof";

		for (MultipartFile file : files) {

			File fileEntity = File.of(proofNo, file.getOriginalFilename(), boardType);

			int result = proofFileMapper.saveFile(fileEntity);

			if (result != 1) {
				throw new FailedInsertException("파일 저장에 실패했습니다.");
			}

			fileService.fileTransferTo(file, fileEntity.getChangeName(), boardType);
		}
	}

	@Transactional
	public List<FileDto> findByBno(Long proofNo) {
		return proofFileMapper.findByBno(proofNo);
	}

	@Transactional
	public void deleteFile(Long proofNo) {

		int result = proofFileMapper.deleteFile(proofNo);

		if (result < 1) {
			throw new FailedDeleteException("파일 삭제에 실패했습니다.");
		}
	}

	@Transactional
	public void updateFile(List<MultipartFile> files, Long proofNo, String boardType) {
		if (files == null || files.size() != 2) {
			throw new FileUploadException("인증 사진은 2장을 등록해야 합니다.");
		}

		deleteFile(proofNo);

		saveProofFiles(files, proofNo);

	}

}