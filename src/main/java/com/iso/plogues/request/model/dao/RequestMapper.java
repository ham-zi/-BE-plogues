package com.iso.plogues.request.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.request.model.dto.RequestDto;

@Mapper
public interface RequestMapper {
	int countByUserIdJoinNo(RequestDto requestDto);
	void requestJoin(RequestDto requestDto);
}
