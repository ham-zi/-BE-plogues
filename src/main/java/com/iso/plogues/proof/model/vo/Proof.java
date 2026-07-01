package com.iso.plogues.proof.model.vo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Proof {

    private Long proofNo;
    private String userId;
    private Long joinNo;
    private String title;
    private String category;
    private Double quantity;
    private String content;
    private LocalDateTime createDate;
    private String deleted;
    private String updated;
}


