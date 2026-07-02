package com.iso.plogues.notice.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iso.plogues.exception.FailedFindByNoException;
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
	    if (notice == null) throw new FailedFindByNoException("존재하지 않는 게시글입니다.");
	    List<FileDto> files = noticeMapper.selectFileList(noticeNo);
	    notice.setFileList(files);
	    return notice;
	}
}
