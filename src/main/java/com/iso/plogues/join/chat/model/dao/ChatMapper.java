package com.iso.plogues.join.chat.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.iso.plogues.join.chat.model.vo.Chat;

@Mapper
public interface ChatMapper {
	int saveChat(Chat chat);

}
