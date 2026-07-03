package com.iso.plogues.home.model.vo;

import java.util.List;

import com.iso.plogues.notice.model.dto.NoticeDto;
import com.iso.plogues.tree.model.dto.TreeDto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HomeResponse {
	private List<NoticeDto> events;
	private List<NoticeDto> notices;
	private List<?> absolration;
	private List<TreeDto>environments;
}
