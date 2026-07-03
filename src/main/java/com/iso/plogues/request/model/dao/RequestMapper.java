package com.iso.plogues.request.model.dao;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.iso.plogues.request.model.dto.RequestDto;


@Mapper
public interface RequestMapper {
	int countByUserIdJoinNo(RequestDto requestDto); 
	int countAcceptByJoinNo(Long requestNo);
	RequestDto findByRequestNo(Long RequestNo); 
	void saveRequest(RequestDto requestDto);
	void requestAccept(Long requestNo);
	void requestDenied(Long requestNo);
	int countByHostStatus(@Param(value="userId")String userId, @Param(value="status")String status); 
	int countMyJoins(@Param(value="userId")String userId, @Param(value="status")String status);  
	RequestDto findByUserIdJoin(@Param(value = "userId") String userId, @Param(value = "joinNo") Long joinNo);
	List<RequestDto> findAllMyJoins(@Param(value="offset")int offset,
			                 @Param(value="boardLimit")int boardLimit,
			                 @Param(value="userId")String userId,
			                 @Param(value="status")String status);
	List<RequestDto> findAllRequest(@Param(value="offset")int offset,
			                 @Param(value="boardLimit")int boardLimit,
			                 @Param(value="userId")String userId,
			                 @Param(value="status")String status);
}
