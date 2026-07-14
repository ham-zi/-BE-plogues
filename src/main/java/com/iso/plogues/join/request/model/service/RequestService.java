package com.iso.plogues.join.request.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.join.common.JoinBoardValidate;
import com.iso.plogues.join.model.dto.DetailJoinDto;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.service.JoinService;
import com.iso.plogues.join.request.model.dao.RequestMapper;
import com.iso.plogues.join.request.model.dto.RequestDto;
import com.iso.plogues.join.request.model.vo.Request;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly=true)
public class RequestService {

	private final RequestMapper requestMapper;
	private final JoinBoardValidate joinBoardValidate;
	
	@Transactional
	public void saveRequest(RequestDto requestDto) {
		isDuplicateRequest(requestDto);
		joinBoardValidate.ValidateJoinBoard(requestDto.getJoinNo());
		Request requestEntity = Request.builder()
									   .joinNo(requestDto.getJoinNo())
									   .userId(requestDto.getUserId())
									   .aspiration(requestDto.getAspiration())
									   .status(requestDto.getStatus())
									   .build();
		requestMapper.saveRequest(requestEntity);
		
	}
	
	@Transactional
	public void saveRequestByHost(String userId,Long joinNo) {
			Request requestEntity = Request.builder()
										   .joinNo(joinNo)
										   .userId(userId)
										   .aspiration("host")
										   .status("ACCEPTED")
										   .build();
			requestMapper.saveRequest(requestEntity);
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
	
	@Transactional
	public void requestCanceled(CustomUserDetails user, Long requestNo) {
		validateCanceledRequest(user.getUsername(), requestNo);
		requestMapper.requestCanceled(requestNo);
	}
	
	public BoardResponse<RequestDto> findAllMyJoins(String userId, int page, String status, String category) { //???
		PageInfo pi = PageInfo.of(requestMapper.countMyJoins(userId, status, category), page, 10, 5);
		List<RequestDto> requests = requestMapper.findAllMyJoins(pi.getOffset(),pi.getBoardLimit(),userId, status, category);
		BoardResponse<RequestDto> boardResponse = new BoardResponse<RequestDto>(pi, requests);
		return boardResponse;
	}

	public BoardResponse<RequestDto> findAllRequest(CustomUserDetails user, int page, String status, String category) {
		PageInfo pi = PageInfo.of(requestMapper.countByHostStatus(user.getUsername(), status, category), page, 10, 5);
		List<RequestDto> requests = requestMapper.findAllRequest(pi.getOffset(),pi.getBoardLimit(),user.getUsername(), status, category);
		BoardResponse<RequestDto> boardResponse = new BoardResponse<RequestDto>(pi, requests);
		return boardResponse;
	}
	
	@Transactional
	public void findByUserIdJoin(String userId, Long joinNo) {
		RequestDto request = requestMapper.findByUserIdJoin(userId, joinNo);
		validateRequest(request);
	}
	//
	
	private void validateRequest(RequestDto request) {
		if(request == null) {
			throw new FailedFindByNoException("참여요청이 수락된 후 이용할 수 있습니다.");
		}
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
		joinBoardValidate.ValidateJoinBoard(request.getJoinNo());
		validateHost(userId, request.getHost());
		
	}
	private void validateDeniedRequest(String userId, Long requestNo) {
		RequestDto request = requestMapper.findByRequestNo(requestNo);
		validateRequestNo(requestNo);
		validateHost(userId, request.getHost());
		
	}
	
	private void validateCanceledRequest(String userId, Long requestNo) {
	    RequestDto request = requestMapper.findByRequestNo(requestNo);
	    validateRequestNo(requestNo);
	    validateUser(userId, request.getUserId());
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

	private void validateHost(String userId, String host) {
		if(!host.equals(userId)) {			
			throw new InValidJoinRequestException("요청에 대한 승인 권한이 없습니다.");
		}
	}
	
	private void validateUser(String userId, String requestUserId) {
	    if(!requestUserId.equals(userId)) {
	        throw new InValidJoinRequestException("본인의 신청만 취소할 수 있습니다.");
	    }
	}


}
