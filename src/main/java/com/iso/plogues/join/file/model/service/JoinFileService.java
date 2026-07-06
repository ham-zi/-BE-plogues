package com.iso.plogues.join.file.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.join.file.model.dao.JoinFileMapper;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinFileService {
	private final JoinFileMapper fileMapper;
	private final FileService fileService;
	
	@Transactional
	public void saveFile(MultipartFile file, Long refBno, String boardType) {
		File fileEntity = File.of(refBno, file.getOriginalFilename(), boardType);
		int result = fileMapper.saveFile(fileEntity);
		throwFileInsertException(result);
		fileService.fileTransferTo(file, fileEntity.getChangeName(), boardType);
	}
	
	@Transactional
	public List<FileDto> findByBno(Long refBno) {
		return fileMapper.findByBno(refBno);
	}
	
	@Transactional
	public void deleteFile(Long refBno) {
		if(findByBno(refBno).isEmpty()) {
			return;
		}
		int result = fileMapper.deleteFile(refBno);
		throwFailedDeleteException(result);
	}
	
	@Transactional
	public void updateFile(MultipartFile file, Long refBno, String boardType) {
		if(!findByBno(refBno).isEmpty()) {
			hardDeleteFile(refBno);
		}
		saveFile(file, refBno, boardType);
	}
	
	private void throwFileInsertException(int result) {
		if(result != 1) {
			throw new FailedInsertException("파일 추가에 실패했습니다.");
		}
	}
	
	private void throwFailedDeleteException(int result) {
		if(result != 1) {
			throw new FailedDeleteException("파일 삭제에 실패했습니다.");
		}
	}
	
	private void hardDeleteFile(Long refBno) {
		int result = fileMapper.hardDeleteFile(refBno);
		throwFailedDeleteException(result);
	}

}
