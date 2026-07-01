package com.iso.plogues.proof.file.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.proof.file.model.dao.ProofFileMapper;
import com.iso.plogues.util.file.File;
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
}