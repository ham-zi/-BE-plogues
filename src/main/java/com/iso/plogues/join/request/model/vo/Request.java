package com.iso.plogues.join.request.model.vo;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Request {
	private final Long joinNo;
	private final String userId;
	private final String aspiration;
	private final String status;

}
