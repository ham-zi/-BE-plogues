package com.iso.plogues.home.model.service;

import org.springframework.stereotype.Service;

import com.iso.plogues.home.model.vo.HomeResponse;
import com.iso.plogues.notice.model.dto.NoticeDto;
import com.iso.plogues.notice.model.service.NoticeService;
import com.iso.plogues.proof.model.service.ProofService;
import com.iso.plogues.tree.model.service.TreeService;
import com.iso.plogues.util.dto.BoardResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeService {
	private final NoticeService noticeService;
	private final TreeService treeService;
	private final ProofService proofService;
	
	public HomeResponse getWelcomInfo() {
		BoardResponse<NoticeDto> events = noticeService.selectNoticeList("EVENT", 1);
		BoardResponse<NoticeDto> notices = noticeService.selectNoticeList("NOTICE", 1);
		proofService.getCarbonReduction();
		return HomeResponse.builder()
				           .notices(notices.getBoard())
				           .events(events.getBoard())
				           //.environments(treeService.findDataByWeek())
				           .build();
	}

	
}
