package com.iso.plogues.request.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.request.model.dto.RequestDto;

@Mapper
public interface RequestMapper {
	int countByRequestNo(Long reqeustNo);
	int countByUserIdJoinNo(RequestDto requestDto);
	RequestDto findByRequestNo(Long RequestNo);
	void requestJoin(RequestDto requestDto);
	void requestAccept(Long requestNo);
	void requestDenied(Long requestNo);
	RequestDto findByUserIdJoin(@Param(value = "userId") String userId, @Param(value = "joinNo") Long joinNo);
}
