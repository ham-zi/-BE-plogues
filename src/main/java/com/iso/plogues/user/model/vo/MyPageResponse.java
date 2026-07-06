package com.iso.plogues.user.model.vo;

import java.util.List;
import com.iso.plogues.user.model.dto.MyInfoDto;
import com.iso.plogues.util.page.PageInfo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MyPageResponse<T>{

	private PageInfo pageInfo;
	private List<T> list;
	private MyInfoDto myInfo;
}
