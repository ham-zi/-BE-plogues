package com.iso.plogues.tree.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TreeResponse {
	private Long environmentNo;
	private String manager;
	private String zoneName;
	private String measureTime;
	private double temperature;
	private double humidity;
	private double soilMoisture;
}

