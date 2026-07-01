package com.iso.plogues.request.model.service;

import org.springframework.stereotype.Service;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
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
		validateJoinNo(requestDto.getJoinNo());
		isDuplicateRequest(requestDto);
		requestMapper.requestJoin(requestDto);
	}
	
	public void requestAccept(CustomUserDetails user, Long requestNo) {
		validateRequest(user.getUsername(), requestNo);
		requestMapper.requestAccept(requestNo);
	}
	
	private void isDuplicateRequest(RequestDto requestDto) {
		if(requestMapper.countByUserIdJoinNo(requestDto) > 0) {
			throw new InValidJoinRequestException("이미 신청한 요청입니다.");
		}
	}
	
	private void validateRequest(String userId, Long requestNo) {
		RequestDto request = requestMapper.findByRequestNo(requestNo);
		validateRequestNo(requestNo);
		checkStatus(request.getStatus());
		validateHost(userId, request.getUserId());
		
	}
	
	private void validateRequestNo(Long requestNo) {
		if(requestMapper.findByRequestNo(requestNo) == null) {
			throw new InValidJoinRequestException("존재하지 않는 요청입니다.");
		}
	}
	
	private void checkStatus(String status) {
		if("ACCEPTED".equals(status)) {			
			throw new InValidJoinRequestException("이미 승인 처리된 요청입니다.");
		}
	}

	private void validateHost(String userId, String host) {
		if(!host.equals(userId)) {			
			throw new InValidJoinRequestException("요청에 대한 승인 권한이 없습니다.");
		}
	}
	
	private void validateJoinNo(Long joinNo) {
		joinService.findByJoinNo(joinNo);
	}
	

}
