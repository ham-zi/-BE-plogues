package com.iso.plogues.join.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.join.model.dto.DetailJoinDto;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.vo.Join;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface JoinMapper {
	int saveJoin(Join join);
	int listCount(String keyword);
	int hostListCount(@Param("userId") String userId);
	int listCountByKeyword(String keyword);
	int countMyJoins(@Param(value="userId")String userId, @Param(value="status")String status);
	List<JoinDto> findAllPlant(@Param(value="pi")PageInfo pi,@Param(value="keyword")String keyword);
	List<JoinDto> findAllPlog(@Param(value="pi")PageInfo pi,@Param(value="keyword")String keyword);
	List<JoinDto> findAllByHost(@Param(value="userId")String userId,@Param(value="pi")PageInfo pi);
	DetailJoinDto findByJoinNo(Long joinNo);
	int deleteJoin(@Param(value="userId") String userId, @Param(value="joinNo") Long joinNo);
	int updateJoin(Join join);
}