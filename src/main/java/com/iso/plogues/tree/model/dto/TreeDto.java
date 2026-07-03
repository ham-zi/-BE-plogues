package com.iso.plogues.tree.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TreeDto {
	private Long environmentNo;
	private String manager;
	@NotBlank(message="센서의 구역을 입력해주세요.")
	private String zoneName;
	private Date measureTime;
	@NotNull(message="온도를 입력해주세요.")
	private double temperature;
	@NotNull(message="습도를 입력해주세요.")
	private double humidity;
	@NotNull(message="토양습도를 입력해주세요.")
	private double soilMoisture;
}
