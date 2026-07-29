package com.iso.plogues.proof.file.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedInsertException;
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

	    for(int i = 0; i < files.size(); i++) {
	        MultipartFile file = files.get(i);
	        File fileEntity = File.of(proofNo, file.getOriginalFilename());

	        int result = proofFileMapper.saveFile(fileEntity, i + 1);

	        if(result != 1) {
	            throw new FailedInsertException("파일 저장 실패");
	        }

	        fileService.fileSave(file, fileEntity.getChangeName());
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
		List<FileDto> list = findByBno(proofNo);
	    if(files == null || files.isEmpty()) {
	        return;
	    }
	    hardDeleteFile(proofNo);
	    for(FileDto f : list) {			
			fileService.deleteFile(f.getChangeName());
		}
	    saveProofFiles(files, proofNo);
	}

	private void hardDeleteFile(Long proofNo) {
		int result = proofFileMapper.hardDeleteFile(proofNo);
		if(result < 1) {
			throw new FailedDeleteException("파일 삭제에 실패했습니다.");
		}
	}

}