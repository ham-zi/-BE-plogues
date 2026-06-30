package com.iso.plogues.join.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.vo.Join;
import com.iso.plogues.template.page.PageInfo;

@Mapper
public interface JoinMapper {
	int saveJoin(Join join);
	int listCount();
	List<JoinDto> findAllPlant(PageInfo page);
	List<JoinDto> findAllPlog(PageInfo page);


}