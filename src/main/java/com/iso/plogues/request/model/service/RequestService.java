package com.iso.plogues.request.model.service;

import org.springframework.stereotype.Service;
import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.join.model.service.JoinService;
import com.iso.plogues.request.model.dao.RequestMapper;
import com.iso.plogues.request.model.dto.RequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class RequestService {

	private final RequestMapper requestMapper;
	private final JoinService joinService;
	
	public void requestJoin(RequestDto requestDto) {
		isValidJoinNo(requestDto.getJoinNo());
		isValidRequest(requestDto);
		requestMapper.requestJoin(requestDto);
	}
	
	private void isValidRequest(RequestDto requestDto) {
		if(requestMapper.countByUserIdJoinNo(requestDto) > 0) {
			throw new InValidJoinRequestException("이미 신청한 요청입니다.");
		}
	}
	private void isValidJoinNo(Long joinNo) {
		joinService.findByJoinNo(joinNo);
	}

}
