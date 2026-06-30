package com.iso.plogues.join.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iso.plogues.auth.model.vo.CustomUserDetails;
import com.iso.plogues.exception.FailedInsertException;
import com.iso.plogues.join.file.model.service.JoinFileService;
import com.iso.plogues.join.model.dao.JoinMapper;
import com.iso.plogues.join.model.dto.JoinDto;
import com.iso.plogues.join.model.vo.Join;
import com.iso.plogues.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {
	private final JoinMapper joinMapper;
	private final JoinFileService fileService;
	
	@Transactional
	public void saveJoin(CustomUserDetails user, JoinDto join, MultipartFile file) {
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
		if(result != 1) {
			throw new FailedInsertException("게시글 작성 실패");
		}
		if(file == null || file.isEmpty()) {
			return;
		}
		fileService.saveFile(file, joinEntity.getJoinNo(), "join");
	}
	
	private PageInfo newPageInfo(int listCount, int page) {
		return PageInfo.of(listCount, page, 10, 5);
	}
	
	@Transactional
	public List<JoinDto> findAllPlant(int page) {
		return joinMapper.findAllPlant(newPageInfo(joinMapper.listCount(), page));
	}
	
	@Transactional
	public List<JoinDto> findAllPlog(int page) {
		return joinMapper.findAllPlog(newPageInfo(joinMapper.listCount(), page));
	}

}
