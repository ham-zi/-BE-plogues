package com.iso.plogues.request.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.service.JoinService;
import com.iso.plogues.request.model.dao.RequestMapper;
import com.iso.plogues.request.model.dto.RequestDto;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class RequestService {

	private final RequestMapper requestMapper;
	private final JoinService joinService;
	
	@Transactional
	public void saveRequest(RequestDto requestDto) {
		validateJoinNo(requestDto.getJoinNo());
		isDuplicateRequest(requestDto);
		requestMapper.saveRequest(requestDto);
	}
	
	@Transactional
	public void requestAccept(CustomUserDetails user, Long requestNo) {
		validateAcceptRequest(user.getUsername(), requestNo);
		requestMapper.requestAccept(requestNo);
	}
	
	@Transactional
	public void requestDenied(CustomUserDetails user, Long requestNo) {
		validateDeniedRequest(user.getUsername(), requestNo);
		requestMapper.requestDenied(requestNo);
	}
	
	@Transactional(readOnly=true)
	public BoardResponse<RequestDto> findAll(String userId, int page, String status) {
		PageInfo pi = PageInfo.of(requestMapper.countByUserIdStatus(userId, status), page, 10, 5);
		List<RequestDto> requests = requestMapper.findAll(pi.getOffset(),pi.getBoardLimit(),userId, status);
		BoardResponse<RequestDto> boardResponse = new BoardResponse<RequestDto>();
		boardResponse.setPage(pi);
		boardResponse.setBoard(requests);
		return boardResponse;
	}
	
	private void isDuplicateRequest(RequestDto requestDto) {
		if(requestMapper.countByUserIdJoinNo(requestDto) > 0) {
			throw new InValidJoinRequestException("이미 신청한 요청입니다.");
		}
	}
	
	private void validateAcceptRequest(String userId, Long requestNo) {
		RequestDto request = requestMapper.findByRequestNo(requestNo);
		validateRequestNo(requestNo);
		checkAccepted(request.getStatus());
		validateJoinNo(request.getJoinNo());
		validateHost(userId, request.getHost());
		
	}
	private void validateDeniedRequest(String userId, Long requestNo) {
		RequestDto request = requestMapper.findByRequestNo(requestNo);
		validateRequestNo(requestNo);
		checkDenied(request.getStatus());
		validateHost(userId, request.getHost());
		
	}
	
	private void validateRequestNo(Long requestNo) {
		if(requestMapper.findByRequestNo(requestNo) == null) {
			throw new InValidJoinRequestException("존재하지 않는 요청입니다.");
		}
	}
	
	private void checkAccepted(String status) {
		if("ACCEPTED".equals(status)) {			
			throw new InValidJoinRequestException("이미 승인 처리된 요청입니다.");
		}
	}
	
	private void checkDenied(String status) {
		if("DENIED".equals(status)) {			
			throw new InValidJoinRequestException("이미 거절 처리된 요청입니다.");
		}
	}

	private void validateHost(String userId, String host) {
		if(!host.equals(userId)) {			
			throw new InValidJoinRequestException("요청에 대한 승인 권한이 없습니다.");
		}
	}
	
	private void validateJoinNo(Long joinNo) {
		JoinDto joinDto = joinService.findByJoinNo(joinNo);
		validateParticipants(joinDto.getParticipants(), joinNo);
	}
	
	private void validateParticipants(int participants, Long joinNo) {
		if(participants <= requestMapper.countAcceptByJoinNo(joinNo)) {			
			throw new InValidJoinRequestException("모집이 완료된 모임입니다.");
		}
	}

}
