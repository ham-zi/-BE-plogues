package com.iso.plogues.join.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.join.model.vo.Join;

@Mapper
public interface JoinMapper {
	public int saveJoin(Join join);

}
