package com.iso.plogues.tree.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Tree {
	private String zoneName;
	private double temperature;
	private double humidity;
	private double soilMoisture;
}
