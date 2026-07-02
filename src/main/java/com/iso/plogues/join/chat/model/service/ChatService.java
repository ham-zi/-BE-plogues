package com.iso.plogues.join.chat.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.join.chat.model.dao.ChatMapper;
import com.iso.plogues.join.chat.model.dto.ChatDto;
import com.iso.plogues.join.chat.model.vo.Chat;
import com.iso.plogues.request.model.dao.RequestMapper;
import com.iso.plogues.request.model.dto.RequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatMapper chatMapper;
	private final RequestMapper requestMapper;
	
	private Long validUser(CustomUserDetails user, Long joinNo) {
		RequestDto request = requestMapper.findByUserIdJoin(user.getUsername(), joinNo);
		throwFindByException(request);
		return request.getJoinRequestNo();
	}
	
	private void throwFindByException(RequestDto request) {
		if(request == null) {
			throw new FailedFindByNoException("참여요청이 수락된 후 이용할 수 있습니다.");
		}
	}
	
	@Transactional
	public void saveChat(CustomUserDetails user, ChatDto chat) {
		Long refJrn = validUser(user, chat.getJoinNo());
		Chat chatEntity = Chat.builder()
							  .requestNo(refJrn)
							  .content(chat.getContent())
							  .build();
		int result = chatMapper.saveChat(chatEntity);
		throwFailedInsertException(result);
	}
	
	private void throwFailedInsertException(int result) {
		if(result != 1) {
			throw new FailedInsertException("채팅 작성에 실패했습니다.");
		}
	}

}
