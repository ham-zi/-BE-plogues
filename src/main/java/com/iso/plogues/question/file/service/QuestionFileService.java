package com.iso.plogues.question.file.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.question.file.dao.QuestionFileMapper;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionFileService {
    private final QuestionFileMapper fileMapper;
    private final FileService fileService;

    @Transactional
    public void saveFile(MultipartFile file, Long refBno) {
        File fileEntity = File.of(refBno, file.getOriginalFilename());
        int result = fileMapper.saveFile(fileEntity);
        throwFileInsertException(result);
        fileService.fileSave(file, fileEntity.getChangeName());
    }

    private void throwFileInsertException(int result) {
        if(result != 1) {
            throw new FailedInsertException("파일 추가에 실패했습니다.");
        }
    }

    @Transactional
    public List<FileDto> findByBno(Long refBno) {
        return fileMapper.findByBno(refBno);
    }

    @Transactional (readOnly = true)
    public void deleteFile(Long refBno) {
        if(findByBno(refBno).isEmpty()) {
            return;
        }
        int result = fileMapper.deleteFile(refBno);
        throwFailedDeleteException(result);
    }

    private void throwFailedDeleteException(int result) {
        if(result != 1) {
            throw new FailedDeleteException("파일 삭제에 실패했습니다.");
        }
    }

    @Transactional
    public void updateFile(MultipartFile file, Long refBno) {
    	List<FileDto> files = findByBno(refBno);
		if(!findByBno(refBno).isEmpty()) {
			hardDeleteFile(refBno);
			for(FileDto f : files) {			
				fileService.deleteFile(f.getFilePath());
			}
		}
		saveFile(file, refBno);
    }

    private void hardDeleteFile(Long refBno) {
        int result = fileMapper.hardDeleteFile(refBno);
        throwFailedDeleteException(result);
    }

	
}
