package com.iso.plogues.join.request.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Request {
	private Long joinRequestNo;
	private Long joinNo;
	private String userId;
	private String aspiration;
	private String status;

}
