package com.iso.plogues.join.request.model.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.join.common.JoinBoardValidate;
import com.iso.plogues.join.request.model.dao.RequestMapper;
import com.iso.plogues.join.request.model.dto.RequestDto;
import com.iso.plogues.join.request.model.vo.Request;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
	@Mock
	RequestMapper requestMapper;
	@Mock
	JoinBoardValidate joinBoardValidate;
	@InjectMocks
	RequestService requestService;
	
	
	private RequestDto createRequestDto() {
		RequestDto requestDto = new RequestDto();
		requestDto.setJoinNo((long) 3);
		requestDto.setUserId("user03");
		requestDto.setAspiration("포부");
		requestDto.setStatus("WAITING");
		requestDto.setJoinRequestNo((long)3);
		requestDto.setHost("user01");
		return requestDto;
	}
	
	private CustomUserDetails createCustomUserDetails() {
		CustomUserDetails user = CustomUserDetails.builder().username("user01").build();
		return user;
	}
	
	
	@Test
	@DisplayName("요청 신청 시 Mapper에 값들이 제대로 들어가야함")
	void saveRequest_success() {
		
		// given -> 상황 만들기
		RequestDto requestDto = new RequestDto();
		requestDto.setJoinNo((long) 3);
		requestDto.setUserId("user03");
		requestDto.setAspiration("포부");
		requestDto.setStatus("WAITING");
		
		
		// when -> 실행
		requestService.saveRequest(requestDto);
		
		// then -> 검증
		ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
		verify(requestMapper).saveRequest(captor.capture());
		
		Request saved = captor.getValue();
		assertEquals("user03",saved.getUserId());
		assertEquals((long)3,saved.getJoinNo());
	
	}
	
	
	@Test
	@DisplayName("요청 수락 시 Mapper가 실행되어야 함")
	void requestAccept_success() {
		
		//given -> 상황 만들기
		CustomUserDetails user = createCustomUserDetails();
		Long requestNo = (long) 3;
		RequestDto request = createRequestDto();
		
		
		//when -> 실행
		when(requestMapper.findByRequestNo(requestNo)).thenReturn(request);
		
		assertDoesNotThrow(() -> requestService.requestAccept(user, requestNo));
		
		//then
		verify(requestMapper).requestAccept(requestNo);
		
	}
	
	@Test
	@DisplayName("요청 거절 시 Mapper가 실행되어야 함")
	void requestDenied_success() {
		//given -> 상황 만들기
		CustomUserDetails user = createCustomUserDetails();
		Long requestNo = (long) 3;
		RequestDto request = createRequestDto();
		
		
		//when -> 실행
		when(requestMapper.findByRequestNo(requestNo)).thenReturn(request);
		
		assertDoesNotThrow(() -> requestService.requestAccept(user, requestNo));
		
		//then
		verify(requestMapper).requestAccept(requestNo);
	}
	
	@Test
	@DisplayName("내 요청 취소 시 Mapper가 실행되어야 함")
	void requestCanceled_success() {
		
		//given
		CustomUserDetails user = CustomUserDetails.builder().username("user05").build();
		Long requestNo = (long) 3;
		RequestDto request = createRequestDto();
		request.setUserId("user05");
		
		//when
		when(requestMapper.findByRequestNo(requestNo)).thenReturn(request);
		
		assertDoesNotThrow(() -> requestService.requestCanceled(user, requestNo));
		
		//then
		verify(requestMapper).requestCanceled(requestNo);
		
	}
	
	
	@Test
	@DisplayName("이미 신청한 모집글에 다시 신청하면 예외가 발생한다.")
	void saveRequest_duplicate() {
		
		//given
		RequestDto dto = new RequestDto();
		dto.setJoinNo(1L);
		dto.setUserId("user01");
		
		when(requestMapper.countByUserIdJoinNo(dto)).thenReturn(1);
		
		//when
		assertThrows(InValidJoinRequestException.class, () -> requestService.saveRequest(dto));
		
		//verify는 Mockito에서 Mock객체의 특정 메서드가 실제로 호출됐는지 확인할 때쓴다.
		//반대로 never()는 단 한번도 호출되면 안된다.
		verify(requestMapper, never()).saveRequest(any(Request.class));
		
		
	}
	
	@Test
	@DisplayName("요청 수락을 누를 때 존재하지 않는 요청일 경우 예외가 발생한다.")
	void requestAccept_validateRequestNo() {
		
		//given
		CustomUserDetails user = CustomUserDetails.builder().username("user01").build();
		Long requestNo = (long)3;
		
		//when
		when(requestMapper.findByRequestNo(requestNo)).thenReturn(null);
		
		assertThrows(InValidJoinRequestException.class, () -> requestService.requestAccept(user, requestNo));
		
		//then
		verify(requestMapper, never()).requestAccept(requestNo);
		
	}
	
	@Test
	@DisplayName("이미 승인 처리된 요청일 경우 예외가 발생한다.")
	void requestAccept_checkAccepted() {
		
		//given
		CustomUserDetails user = CustomUserDetails.builder().username("user01").build();
		Long requestNo = (long)3;
		RequestDto request = new RequestDto();
		request.setStatus("ACCEPTED");
		
		//when
		when(requestMapper.findByRequestNo(requestNo)).thenReturn(request);
		
		assertThrows(InValidJoinRequestException.class, () -> requestService.requestAccept(user, requestNo));
		
		//then
		verify(requestMapper, never()).requestAccept(requestNo);
	}
	

	@Test
	@DisplayName("요청에 대한 승인 권한이 없을 때 예외가 발생한다.")
	void requestAccept_validateHost() {
		
		//given
		CustomUserDetails user = createCustomUserDetails();
		Long requestNo = (long) 3;
		RequestDto request = createRequestDto();
		request.setHost("isNotHost");
		
		//when
		when(requestMapper.findByRequestNo(requestNo)).thenReturn(request);
		
		assertThrows(InValidJoinRequestException.class, () -> requestService.requestAccept(user, requestNo));
		
		//then
		verify(requestMapper, never()).requestAccept(requestNo);
		
	}
	
	@Test
	@DisplayName("본인의 요청을 취소할때 본인이 아니면 예외가 발생한다.")
	void requestAccept_validateUser() {
		
		//given
		CustomUserDetails user = createCustomUserDetails();
		Long requestNo = (long) 3;
		RequestDto request = createRequestDto();
		request.setUserId("isNotUser");
		
		//when
		assertThrows(InValidJoinRequestException.class, () -> requestService.requestCanceled(user,requestNo));
		
		//then
		verify(requestMapper, never()).requestAccept(requestNo);
		
	}
	
	
	
	
	
}
