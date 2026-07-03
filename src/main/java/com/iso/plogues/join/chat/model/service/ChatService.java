package com.iso.plogues.join.chat.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedFindAllException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.join.chat.model.dao.ChatMapper;
import com.iso.plogues.join.chat.model.dto.ChatDto;
import com.iso.plogues.join.chat.model.vo.Chat;
import com.iso.plogues.request.model.service.RequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatMapper chatMapper;
	private final RequestService requestService;
	
	@Transactional
	public void saveChat(CustomUserDetails user, ChatDto chat) {
		validUser(user.getUsername(), chat.getJoinNo());
		Chat chatEntity = Chat.builder()
							  .userId(user.getUsername())
							  .joinNo(chat.getJoinNo())
							  .content(chat.getContent())
							  .build();
		int result = chatMapper.saveChat(chatEntity);
		throwFailedInsertException(result);
	}

	@Transactional
	public List<ChatDto> findAll(CustomUserDetails user, Long joinNo) {
		validUser(user.getUsername(), joinNo);
		List<ChatDto> list = chatMapper.findAll(joinNo);
		throwFindAllException(list);
		return list;
	}
	
	private void validUser(String userId, Long joinNo) {
		requestService.findByUserIdJoin(userId, joinNo);
	}
	
	private void throwFailedInsertException(int result) {
		if(result != 1) {
			throw new FailedInsertException("채팅 작성에 실패했습니다.");
		}
	}
	
	private <T> void throwFindAllException(List<T> list) {
		if(list == null || list.isEmpty()) {
			throw new FailedFindAllException("작성된 채팅이 없습니다.");
		}
	}

}
