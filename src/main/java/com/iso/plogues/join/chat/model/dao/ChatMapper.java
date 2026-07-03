package com.iso.plogues.join.chat.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.iso.plogues.join.chat.model.dto.ChatDto;
import com.iso.plogues.join.chat.model.vo.Chat;

@Mapper
public interface ChatMapper {
	int saveChat(Chat chat);
	List<ChatDto> findAll(Long joinNo);

}
