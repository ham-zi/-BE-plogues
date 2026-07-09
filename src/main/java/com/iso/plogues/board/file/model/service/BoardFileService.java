package com.iso.plogues.board.file.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.board.file.model.dao.BoardFileMapper;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.file.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardFileService {
    private final BoardFileMapper fileMapper;
    private final FileService fileService;

    @Transactional
    public void saveFile(MultipartFile file, Long refBno) {
        File fileEntity = File.of(refBno, file.getOriginalFilename(), "board");
        int result = fileMapper.saveFile(fileEntity);
        throwFileInsertException(result);
        fileService.fileTransferTo(file, fileEntity.getChangeName(), "board");
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
    public void deleteFileByNo(Long fileNo) {
        fileMapper.deleteFileByNo(fileNo);
    }

    private void hardDeleteFile(Long refBno) {
        int result = fileMapper.hardDeleteFile(refBno);
        throwFailedDeleteException(result);
    }

	
}
