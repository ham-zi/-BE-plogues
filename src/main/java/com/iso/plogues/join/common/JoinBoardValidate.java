package com.iso.plogues.join.common;

import org.springframework.stereotype.Component;

import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.join.model.dao.JoinMapper;
import com.iso.plogues.join.model.dto.DetailJoinDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JoinBoardValidate {

	private final JoinMapper joinMapper;

	public void ValidateJoinBoard(Long joinNo) {
		DetailJoinDto joinDto = joinMapper.findByJoinNo(joinNo);
		validateParticipants(joinDto);
	}
	
	private void validateParticipants(DetailJoinDto joinDto) {
		if(joinDto.getParticipants() > 0 && joinDto.getCurrentCount() > 0 && joinDto.getParticipants() <= joinDto.getCurrentCount()) {			
			throw new InValidJoinRequestException("모집이 완료된 모임입니다.");
		}
	}
}
