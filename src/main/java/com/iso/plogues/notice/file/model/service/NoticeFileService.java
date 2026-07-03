package com.iso.plogues.notice.file.model.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.iso.plogues.notice.file.model.dao.NoticeFileMapper;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.util.file.File;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.file.FileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeFileService {

    private final NoticeFileMapper fileMapper;
    private final FileService fileService;

    @Transactional
    public void saveFile(MultipartFile file, Long refNoticeNo) {
        File fileEntity = File.of(refNoticeNo, file.getOriginalFilename(), "notice");
        int result = fileMapper.saveFile(fileEntity);
        throwFileInsertException(result);
        fileService.fileTransferTo(file, fileEntity.getChangeName(), "notice");
    }

    private void throwFileInsertException(int result) {
        if (result != 1) {
            throw new FailedInsertException("파일 추가에 실패했습니다.");
        }
    }

    @Transactional
    public List<FileDto> findByBno(Long refNoticeNo) {
        return fileMapper.findByBno(refNoticeNo);
    }

    @Transactional(readOnly = true)
    public void deleteFile(Long refNoticeNo) {
        if (findByBno(refNoticeNo).isEmpty()) {
            return;
        }
        int result = fileMapper.deleteFile(refNoticeNo);
        throwFailedDeleteException(result);
    }

    private void throwFailedDeleteException(int result) {
        if (result != 1) {
            throw new FailedDeleteException("파일 삭제에 실패했습니다.");
        }
    }

    @Transactional
    public void updateFile(MultipartFile file, Long refNoticeNo) {
        if (!findByBno(refNoticeNo).isEmpty()) {
            hardDeleteFile(refNoticeNo);
        }
        saveFile(file, refNoticeNo);
    }

    private void hardDeleteFile(Long refNoticeNo) {
        int result = fileMapper.hardDeleteFile(refNoticeNo);
        throwFailedDeleteException(result);
    }
}
