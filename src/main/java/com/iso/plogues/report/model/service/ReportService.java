package com.iso.plogues.report.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.report.model.dao.ReportMapper;
import com.iso.plogues.report.model.dto.ReportDto;
import com.iso.plogues.report.model.vo.Report;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final ReportMapper reportMapper;
	
	
	
	@Transactional
	public void saveReport(CustomUserDetails user, ReportDto report) {
		Report reportEntity = Report.builder()
									.userId(user.getUsername())
									.reportCategory(report.getReportCategory())
									.boardType(report.getBoardType())
									.content(report.getContent())
									.targetNo(report.getTargetNo())
									.build();
		
		if (reportMapper.existsReport(reportEntity) > 0) {
		    throw new IllegalArgumentException("이미 신고한 게시글입니다.");
		}
		
		int result = reportMapper.saveReport(reportEntity);
		throwFailedInsertException(result);
	}
	
	private void throwFailedInsertException(int result) {
		if(result != 1) {
			throw new FailedInsertException("게시글 작성 실패");
		}
	}
	
	private PageInfo newPageInfo(int listCount, int page) {
		return PageInfo.of(listCount, page, 10, 5);
	}
	
	@Transactional
	public BoardResponse<ReportDto> findAll(int page) {
		PageInfo pageInfo = newPageInfo(reportMapper.listCount(), page);
		List<ReportDto> list = reportMapper.findAll(pageInfo);
		BoardResponse<ReportDto> br = new BoardResponse<>(pageInfo, list);
		
		return br;
	}

}
