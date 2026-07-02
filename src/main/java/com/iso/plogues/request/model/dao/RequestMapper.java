package com.iso.plogues.request.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.request.model.dto.RequestDto;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface RequestMapper {
	int countByRequestNo(Long reqeustNo);
	int countByUserIdJoinNo(RequestDto requestDto);
	int countAcceptByJoinNo(Long requestNo);
	RequestDto findByRequestNo(Long RequestNo);
	void saveRequest(RequestDto requestDto);
	void requestAccept(Long requestNo);
	void requestDenied(Long requestNo);
	int countByUserIdStatus(@Param(value="userId")String userId,@Param(value="status")String status);
	RequestDto findByUserIdJoin(@Param(value = "userId") String userId, @Param(value = "joinNo") Long joinNo);
	List<RequestDto> findAllByHost(@Param(value="offset")int offset,
			                 @Param(value="boardLimit")int boardLimit,
			                 @Param(value="userId")String userId,
			                 @Param(value="status")String status);
	List<RequestDto> findAllMyRequest(@Param(value="offset")int offset,
			                 @Param(value="boardLimit")int boardLimit,
			                 @Param(value="userId")String userId,
			                 @Param(value="status")String status);
}
