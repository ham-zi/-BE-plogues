package com.iso.plogues.join.request.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.join.common.JoinBoardValidate;
import com.iso.plogues.join.model.dao.JoinMapper;
import com.iso.plogues.join.model.dto.DetailJoinDto;
import com.iso.plogues.join.request.model.dao.RequestMapper;
import com.iso.plogues.join.request.model.dto.RequestDto;

/**
 * Mapper 잠금을 무효화한 상태의 정원 초과를 재현하는 단위 테스트.
 * 실제 RequestService / JoinBoardValidate를 실행하고 Mapper만 대체한다.
 * Spring 트랜잭션, 실제 SQL, Oracle 잠금/격리 수준을 검증하는 통합 테스트는 아니다.
 * 재현 테스트의 성공은 버그가 재현됐다는 의미이며, 동시성 안전성을 뜻하지 않는다.
 */
@Timeout(20)
class RequestServiceConcurrencyTest {
    private static final long JOIN_NO = 1L;
    private static final int CAPACITY = 2;
    private static final int HOST_COUNT = 1;

    private final RequestMapper requestMapper = mock(RequestMapper.class);
    // void인 pessimisticLocking 호출은 이 Mock에서 아무 동작도 하지 않는다.
    private final JoinMapper joinMapper = mock(JoinMapper.class);
    private final Set<Long> acceptedRequests = ConcurrentHashMap.newKeySet();
    private final CustomUserDetails host = CustomUserDetails.builder().username("host").build();
    private RequestService requestService;

    @BeforeEach
    void setUp() {
        requestService = new RequestService(requestMapper, new JoinBoardValidate(joinMapper));
        when(requestMapper.findByRequestNo(anyLong())).thenAnswer(invocation -> {
            Long requestNo = invocation.getArgument(0);
            RequestDto request = new RequestDto();
            request.setJoinRequestNo(requestNo);
            request.setJoinNo(JOIN_NO);
            request.setHost("host");
            request.setUserId("applicant-" + requestNo);
            request.setStatus(acceptedRequests.contains(requestNo) ? "ACCEPTED" : "WAITING");
            return request;
        });
        doAnswer(invocation -> {
            acceptedRequests.add(invocation.getArgument(0));
            return null;
        }).when(requestMapper).requestAccept(anyLong());
    }

    @Test
    @DisplayName("순차 승인에서는 마지막 한 명만 승인하고 다음 요청은 거절한다")
    void sequentialAccept_respectsCapacity() {
        when(joinMapper.findByJoinNo(JOIN_NO)).thenAnswer(invocation -> snapshot());

        requestService.requestAccept(host, 101L);

        assertThrows(InValidJoinRequestException.class,
                () -> requestService.requestAccept(host, 102L));
        assertEquals(Set.of(101L), acceptedRequests);
        assertEquals(CAPACITY, currentCount());
    }

    @Test
    @DisplayName("잠금 없는 Mock 재현: 마지막 한 자리를 동시에 조회하면 정원을 초과한다")
    void concurrentAccept_withoutControl_reproducesOverbooking() throws Exception {
        CyclicBarrier bothHaveRead = new CyclicBarrier(2);
        when(joinMapper.findByJoinNo(JOIN_NO)).thenAnswer(invocation -> {
            // 두 스레드 모두 승인 전 인원을 읽은 다음 검증/승인을 진행한다.
            // sleep에 의존하지 않고 문제가 발생하는 실행 순서를 재현한다.
            DetailJoinDto board = snapshot();
            bothHaveRead.await(5, TimeUnit.SECONDS);
            return board;
        });

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<?> first = executor.submit(() -> requestService.requestAccept(host, 101L));
            Future<?> second = executor.submit(() -> requestService.requestAccept(host, 102L));
            // 작업 스레드에서 발생한 예외도 테스트 실패로 전달한다.
            first.get(10, TimeUnit.SECONDS);
            second.get(10, TimeUnit.SECONDS);

            assertEquals(Set.of(101L, 102L), acceptedRequests);
            assertEquals(3, currentCount(), "정원 2명인데 호스트 포함 3명이 승인된다");
            assertTrue(currentCount() > CAPACITY, "동시성 제어 전 정원 초과 재현");
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "작업 스레드 종료 실패");
        }
    }

    private DetailJoinDto snapshot() {
        DetailJoinDto board = new DetailJoinDto();
        board.setJoinNo(JOIN_NO);
        board.setParticipants(CAPACITY);
        board.setCurrentCount(currentCount());
        return board;
    }

    private int currentCount() {
        return HOST_COUNT + acceptedRequests.size();
    }
}