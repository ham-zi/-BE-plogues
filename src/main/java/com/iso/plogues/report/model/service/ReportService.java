package com.iso.plogues.report.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.report.model.dao.ReportMapper;
import com.iso.plogues.report.model.dto.ReportDto;
import com.iso.plogues.report.model.vo.Report;

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
		
		int result = reportMapper.saveReport(reportEntity);
		throwFailedInsertException(result);
	}
	
	private void throwFailedInsertException(int result) {
		if(result != 1) {
			throw new FailedInsertException("게시글 작성 실패");
		}
	}

}
