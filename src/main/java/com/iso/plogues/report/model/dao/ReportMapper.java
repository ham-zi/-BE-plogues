package com.iso.plogues.report.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.report.model.vo.Report;

@Mapper
public interface ReportMapper {
	
	int saveReport(Report report);

}
