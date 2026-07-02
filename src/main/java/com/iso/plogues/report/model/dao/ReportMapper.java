package com.iso.plogues.report.model.dao;

import com.iso.plogues.report.model.dto.ReportDto;
import com.iso.plogues.report.model.vo.Report;

public interface ReportMapper {
	
	int saveReport(Report report);

}
