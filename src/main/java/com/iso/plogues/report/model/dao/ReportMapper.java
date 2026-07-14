package com.iso.plogues.report.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.report.model.dto.ReportDto;
import com.iso.plogues.report.model.vo.Report;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface ReportMapper {
	
	int saveReport(Report report);
	int listCount(ReportDto report);
	List<ReportDto> findAll(@Param(value="pi")PageInfo pi,@Param(value="report")ReportDto report);
	void completeReport(Long reportNo);
	int existsReport(Report report);
	int checkTarget(Long targetNo);

}
