package com.iso.plogues.join.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedDeleteException;
import com.iso.plogues.exception.FailedFindByNoException;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.exception.FailedUpdateException;
import com.iso.plogues.join.file.model.service.JoinFileService;
import com.iso.plogues.join.model.dao.JoinMapper;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.vo.Join;
import com.iso.plogues.util.dto.BoardResponse;
import com.iso.plogues.util.file.FileDto;
import com.iso.plogues.util.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {
	private final JoinMapper joinMapper;
	private final JoinFileService fileService;
	
	@Transactional
	public Long saveJoin(CustomUserDetails user, JoinDto join, MultipartFile file) {
		Join joinEntity = Join.builder()
							  .userId(user.getUsername())
							  .category(join.getCategory())
							  .participants(join.getParticipants())
							  .region(join.getRegion())
							  .startDate(join.getStartDate())
							  .endDate(join.getEndDate())
							  .title(join.getTitle())
							  .content(join.getContent())
							  .build();
		int result = joinMapper.saveJoin(joinEntity);
		throwFailedInsertException(result);
		
		if(file != null && !file.isEmpty()) {
			fileService.saveFile(file, joinEntity.getJoinNo(), "join");
		}
		
		return joinEntity.getJoinNo();
	}

	@Transactional
	public BoardResponse<JoinDto> findAllPlant(int page) {
		PageInfo pageInfo = newPageInfo(joinMapper.listCount(), page);
		List<JoinDto> list = joinMapper.findAllPlant(pageInfo);
		return new BoardResponse<JoinDto>(pageInfo, list);
	}
	
	@Transactional
	public BoardResponse<JoinDto> findAllPlog(int page) {
		PageInfo pageInfo = newPageInfo(joinMapper.listCount(), page);
		List<JoinDto> list = joinMapper.findAllPlog(pageInfo);
		return new BoardResponse<JoinDto>(pageInfo, list);
	}
	
	@Transactional
	public BoardResponse<JoinDto> findAllByHost(CustomUserDetails user, int page) {
		PageInfo pageInfo = newPageInfo(joinMapper.listCount(), page);
		List<JoinDto> list = joinMapper.findAllByHost(user.getUsername(),pageInfo);
		return new BoardResponse<JoinDto>(pageInfo, list);
	}
	
	@Transactional
	public JoinDto findByJoinNo(Long joinNo) {
		JoinDto join = joinMapper.findByJoinNo(joinNo);
		throwFindByException(join);
		List<FileDto> file = fileService.findByBno(joinNo);
		join.setFiles(file);
		return join;
	}
	
	@Transactional
	public void deleteJoin(CustomUserDetails user, Long joinNo) {
		findByJoinNo(joinNo);
		int result = joinMapper.deleteJoin(user.getUsername(), joinNo);
		throwDeleteException(result);
		fileService.deleteFile(joinNo);
	}
	
	@Transactional
	public void updateJoin(CustomUserDetails user, Long joinNo, JoinDto join, MultipartFile file) {
		findByJoinNo(join.getJoinNo());
		Join joinEntity = Join.builder()
				.joinNo(joinNo)
				.userId(user.getUsername())
				.participants(join.getParticipants())
				.region(join.getRegion())
				.startDate(join.getStartDate())
				.endDate(join.getEndDate())
				.title(join.getTitle())
				.content(join.getContent())
				.build();
		int result = joinMapper.updateJoin(joinEntity);
		throwUpdateException(result);
		fileService.updateFile(file, join.getJoinNo(), "join");
	}
	
	private void throwFailedInsertException(int result) {
		if(result != 1) {
			throw new FailedInsertException("게시글 작성 실패");
		}
	}
	
	private PageInfo newPageInfo(int listCount, int page) {
		return PageInfo.of(listCount, page, 10, 5);
	}
	
	private void throwFindByException(JoinDto join) {
		if(join == null) {
			throw new FailedFindByNoException("해당 게시글을 찾지 못했습니다.");
		}
	}
	
	private void throwDeleteException(int result) {
		if(result != 1) {
			throw new FailedDeleteException("게시글 삭제에 실패했습니다.");
		}
	}
	
	private void throwUpdateException(int result) {
		if(result != 1) {
			throw new FailedUpdateException("게시글 수정에 실패했습니다.");
		}
	}


}
