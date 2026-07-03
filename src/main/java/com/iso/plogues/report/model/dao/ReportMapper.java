package com.iso.plogues.report.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.report.model.dto.ReportDto;
import com.iso.plogues.report.model.vo.Report;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface ReportMapper {
	
	int saveReport(Report report);
	int listCount();
	List<ReportDto> findAll(PageInfo page);
	int existsReport(Report report);

}
