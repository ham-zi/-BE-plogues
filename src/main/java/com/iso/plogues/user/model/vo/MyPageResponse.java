package com.iso.plogues.user.model.vo;

import java.util.List;

import com.iso.plogues.request.model.dto.RequestDto;
import com.iso.plogues.user.model.dto.MyInfoDto;
import com.iso.plogues.util.page.PageInfo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MyPageResponse {

	private PageInfo pageInfo;
	private List<RequestDto> requestDto;
	private MyInfoDto myInfo;
}
