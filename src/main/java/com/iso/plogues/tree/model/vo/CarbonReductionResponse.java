package com.iso.plogues.tree.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CarbonReductionResponse {
    private int year;
    private int totalTreeCount;
    private double yearlyReduction;
    private double cumulativeReduction;
}