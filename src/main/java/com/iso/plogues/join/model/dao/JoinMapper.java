package com.iso.plogues.join.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.vo.Join;
import com.iso.plogues.util.page.PageInfo;

@Mapper
public interface JoinMapper {
	int saveJoin(Join join);
	int listCount();
	List<JoinDto> findAllPlant(PageInfo page);
	List<JoinDto> findAllByHost(@Param(value="userId")String userId,@Param(value="pi")PageInfo pi);
	List<JoinDto> findAllPlog(PageInfo page);
	JoinDto findByJoinNo(Long joinNo);
	int deleteJoin(@Param(value="userId") String userId, @Param(value="joinNo") Long joinNo);
	int updateJoin(Join join);

}