package com.iso.plogues.request.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.request.model.dto.RequestDto;

@Mapper
public interface RequestMapper {
	int countByRequestNo(Long reqeustNo);
	int countByUserIdJoinNo(RequestDto requestDto);
	RequestDto findByRequestNo(Long RequestNo);
	void requestJoin(RequestDto requestDto);
	void requestAccept(Long requestNo);
}
