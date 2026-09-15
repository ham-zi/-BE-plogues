package com.iso.plogues.join.request.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.request.InValidJoinRequestException;
import com.iso.plogues.join.common.JoinBoardValidate;
import com.iso.plogues.join.model.dao.JoinMapper;
import com.iso.plogues.join.model.dto.DetailJoinDto;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.request.model.dao.RequestMapper;
import com.iso.plogues.join.request.model.dto.RequestDto;

/**
 * H2 Oracle 모드에서 운영 Mapper XML과 실제 서비스 트랜잭션을 검증한다.
 * 테스트마다 독립 메모리 DB를 사용한다. Oracle 자체의 잠금 동작 검증은 아니다.
 * Mapper를 mock하지 않으며 각 스레드가 별도 연결/트랜잭션을 사용한다.
 */
@Timeout(30)
class RequestServiceLockIntegrationTest {
    private static final int WORKERS = 8;
    private JdbcTemplate jdbc;
    private RequestService service;
    private final CustomUserDetails host = CustomUserDetails.builder().username("host").build();

    @BeforeEach
    void setUp() throws Exception {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=Oracle;DB_CLOSE_DELAY=30;LOCK_TIMEOUT=10000", "sa", "");
        jdbc = new JdbcTemplate(dataSource);
        jdbc.execute("CREATE TABLE PLOGUES_USER (USER_ID VARCHAR(50) PRIMARY KEY, USER_NAME VARCHAR(50))");
        jdbc.execute("""
                CREATE TABLE JOIN_BOARD (
                    JOIN_NO BIGINT PRIMARY KEY, USER_ID VARCHAR(50), CATEGORY VARCHAR(50),
                    PARTICIPANTS INT, REGION VARCHAR(50), START_DATE TIMESTAMP, END_DATE TIMESTAMP,
                    TITLE VARCHAR(100), CONTENT VARCHAR(1000), CREATE_DATE TIMESTAMP, DELETED CHAR(1))
                """);
        jdbc.execute("""
                CREATE TABLE JOIN_REQUEST (
                    JOIN_REQUEST_NO BIGINT PRIMARY KEY, ASPIRATION VARCHAR(100),
                    STATUS VARCHAR(20), JOIN_NO BIGINT, USER_ID VARCHAR(50))
                """);
        jdbc.update("INSERT INTO PLOGUES_USER VALUES ('host', 'Host')");
        jdbc.update("INSERT INTO JOIN_BOARD (JOIN_NO, USER_ID, PARTICIPANTS, DELETED) VALUES (1, 'host', 2, 'N')");
        jdbc.update("INSERT INTO JOIN_REQUEST VALUES (1, 'host', 'ACCEPTED', 1, 'host')");
        for (int i = 0; i < WORKERS; i++) {
            jdbc.update("INSERT INTO JOIN_REQUEST VALUES (?, 'test', 'WAITING', 1, ?)", 101L + i, "user-" + i);
        }

        Configuration config = new Configuration();
        config.setMapUnderscoreToCamelCase(true);
        config.getTypeAliasRegistry().registerAlias("ParticipantDto", com.iso.plogues.join.model.dto.ParticipantDto.class);
        config.getTypeAliasRegistry().registerAlias("JoinDto", JoinDto.class);
        config.getTypeAliasRegistry().registerAlias("DetailJoinDto", DetailJoinDto.class);
        config.getTypeAliasRegistry().registerAlias("RequestDto", RequestDto.class);
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setConfiguration(config);
        factory.setMapperLocations(new ClassPathResource("mapper/join-mapper.xml"),
                new ClassPathResource("mapper/request-mapper.xml"));
        SqlSessionTemplate session = new SqlSessionTemplate(factory.getObject());
        RequestService target = new RequestService(session.getMapper(RequestMapper.class),
                new JoinBoardValidate(session.getMapper(JoinMapper.class)));
        TransactionInterceptor transactions = new TransactionInterceptor();
        transactions.setTransactionManager(new DataSourceTransactionManager(dataSource));
        transactions.setTransactionAttributeSource(new AnnotationTransactionAttributeSource());
        ProxyFactory proxy = new ProxyFactory(target);
        proxy.setProxyTargetClass(true);
        proxy.addAdvice(transactions);
        service = (RequestService) proxy.getProxy();
    }

    @Test
    @DisplayName("실제 SQL 잠금: 한 자리 남은 모임에 8건 동시 승인 시 1건만 성공한다")
    void differentRequests_respectCapacity() throws Exception {
        assertEquals(1, acceptConcurrently(false));
        assertEquals(2, countStatus("ACCEPTED"));
        assertEquals(7, countStatus("WAITING"));
    }

    @Test
    @DisplayName("실제 SQL 잠금: 같은 신청을 8번 동시에 승인해도 1번만 성공한다")
    void sameRequest_isAcceptedOnce() throws Exception {
        // 정원 거절이 아닌, 잠금 후 상태 재조회로 중복 승인을 막는지 확인한다.
        jdbc.update("UPDATE JOIN_BOARD SET PARTICIPANTS = ? WHERE JOIN_NO = 1", WORKERS + 1);
        assertEquals(1, acceptConcurrently(true));
        assertEquals(2, countStatus("ACCEPTED"));
        assertEquals("ACCEPTED", jdbc.queryForObject(
                "SELECT STATUS FROM JOIN_REQUEST WHERE JOIN_REQUEST_NO = 101", String.class));
    }

    private int acceptConcurrently(boolean sameRequest) throws Exception {
        CountDownLatch ready = new CountDownLatch(WORKERS);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(WORKERS);
        List<Future<Boolean>> results = new ArrayList<>();
        try {
            for (int i = 0; i < WORKERS; i++) {
                long requestNo = sameRequest ? 101L : 101L + i;
                results.add(executor.submit(() -> {
                    ready.countDown();
                    if (!start.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("동시 시작 대기 시간 초과");
                    }
                    try {
                        service.requestAccept(host, requestNo);
                        return true;
                    } catch (InValidJoinRequestException expected) {
                        assertEquals(sameRequest ? "이미 승인 처리된 요청입니다." : "모집이 완료된 모임입니다.", expected.getMessage());
                        return false;
                    }
                }));
            }
            assertTrue(ready.await(5, TimeUnit.SECONDS), "모든 작업 준비 완료");
            start.countDown();
            int successes = 0;
            for (Future<Boolean> result : results) {
                // SQL 오류나 잠금 시간 초과 등 예상하지 않은 예외는 실패로 전파한다.
                if (result.get(15, TimeUnit.SECONDS)) successes++;
            }
            return successes;
        } finally {
            start.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    private int countStatus(String status) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM JOIN_REQUEST WHERE JOIN_NO = 1 AND STATUS = ?",
                Integer.class, status);
    }
}