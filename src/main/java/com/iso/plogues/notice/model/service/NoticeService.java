package com.iso.plogues.notice.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.CustomAuthenticationException;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.FailedUpdateException;
import com.iso.plogues.notice.file.model.service.NoticeFileService;
import com.iso.plogues.notice.model.dao.NoticeMapper;
import com.iso.plogues.notice.model.dto.NoticeDto;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeMapper noticeMapper;
	private final NoticeFileService noticeFileService;
	private static final String ADMIN_ID = "admin";
	
	@Transactional(readOnly = true)
	public BoardResponse<NoticeDto> selectNoticeList(String category, int currentPage) {
	    int listCount = noticeMapper.countNoticeList(category);
	    PageInfo page = PageInfo.of(listCount, currentPage, 10, 5);
	    List<NoticeDto> noticeList = noticeMapper.selectNoticeList(category, page);
	    BoardResponse<NoticeDto> response = new BoardResponse<>();
	    response.setPage(page);
	    response.setBoard(noticeList);
	    return response;
	}

	@Transactional(readOnly = true)
	public NoticeDto selectNoticeDetail(Long noticeNo) {
	    NoticeDto notice = noticeMapper.selectNoticeDetail(noticeNo);
	    if (notice == null) {
			throw new FailedFindByNoException("존재하지 않는 게시글입니다.");
		}
	    List<FileDto> files = noticeMapper.selectFileList(noticeNo);
	    notice.setFileList(files);
	    return notice;
	}

	

	@Transactional
	public void updateNotice(CustomUserDetails user, Long noticeNo, NoticeDto noticeDto, List<MultipartFile> files) {
	    if (!ADMIN_ID.equals(user.getUsername())) {
	        throw new CustomAuthenticationException("관리자만 수정할 수 있습니다.");
	    }
	    noticeDto.setNoticeNo(noticeNo);
	    int result = noticeMapper.updateNotice(noticeDto);
	    if (result != 1) {
	        throw new FailedUpdateException("공지사항 수정에 실패했습니다.");
	    }
	    if (files != null && !files.isEmpty()) {
	        for (MultipartFile file : files) {
	            noticeFileService.updateFile(file, noticeNo);
	        }
	    }
	}

	@Transactional
	public void deleteNotice(CustomUserDetails user, Long noticeNo) {
	    if (!ADMIN_ID.equals(user.getUsername())) {
	        throw new CustomAuthenticationException("관리자만 삭제할 수 있습니다.");
	    }
	    int result = noticeMapper.deleteNotice(noticeNo);
	    if (result != 1) {
	        throw new FailedDeleteException("공지사항 삭제에 실패했습니다.");
	    }
	}
	
	@Transactional
	public void insertNotice(CustomUserDetails user, NoticeDto noticeDto, List<MultipartFile> files) {
	    if (!ADMIN_ID.equals(user.getUsername())) {
	        throw new CustomAuthenticationException("관리자만 작성할 수 있습니다.");
	    }
	    noticeDto.setUserId(user.getUsername());
	    int result = noticeMapper.insertNotice(noticeDto);
	    if (result != 1) {
	        throw new FailedInsertException("공지사항 작성에 실패했습니다.");
	    }
	    if (files != null && !files.isEmpty()) {
	        for (MultipartFile file : files) {
	            noticeFileService.saveFile(file, noticeDto.getNoticeNo());
	        }
	    }
	}
}
