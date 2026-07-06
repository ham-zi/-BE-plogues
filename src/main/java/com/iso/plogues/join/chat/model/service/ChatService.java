package com.iso.plogues.join.chat.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedFindAllException;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.FailedUpdateException;
import com.iso.plogues.join.chat.model.dao.ChatMapper;
import com.iso.plogues.join.chat.model.dto.ChatDto;
import com.iso.plogues.join.chat.model.vo.Chat;
import com.iso.plogues.join.request.model.service.RequestService;

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
	
	@Transactional
	public void updateChat(CustomUserDetails user, ChatDto chat) {
		validUser(user.getUsername(), chat.getJoinNo());
		validChat(chat.getChatNo());
		Chat chatEntity = Chat.builder()
				  .chatNo(chat.getChatNo())
				  .content(chat.getContent())
				  .build();
		int result = chatMapper.updateChat(chatEntity);
		throwFailedUpdateException(result);
	}
	
	@Transactional
	public void deleteChat(CustomUserDetails user, Long joinNo, Long chatNo) {
		validUser(user.getUsername(), joinNo);
		validChat(chatNo);
		int result = chatMapper.deleteChat(chatNo);
		throwFailedDeleteException(result);
	}

	private void validUser(String userId, Long joinNo) {
		requestService.findByUserIdJoin(userId, joinNo);
	}
	
	private void validChat(Long chatNo) {
		ChatDto chatDto = chatMapper.findByChatNo(chatNo);
		if(chatDto == null) {
			throw new FailedFindByNoException("해당하는 채팅이 없습니다.");
		}
	}
	
	private <T> void throwFindAllException(List<T> list) {
		if(list == null || list.isEmpty()) {
			throw new FailedFindAllException("작성된 채팅이 없습니다.");
		}
	}
	
	private void throwFailedInsertException(int result) {
		if(result != 1) {
			throw new FailedInsertException("채팅 작성에 실패했습니다.");
		}
	}
	
	private void throwFailedUpdateException(int result) {
		if(result != 1) {
			throw new FailedUpdateException("채팅 수정에 실패했습니다.");
		}
	}
	
	private void throwFailedDeleteException(int result) {
		if(result != 1) {
			throw new FailedDeleteException("채팅 삭제에 실패했습니다.");
		}
	}

}
