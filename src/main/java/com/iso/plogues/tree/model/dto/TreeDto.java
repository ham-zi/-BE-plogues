package com.iso.plogues.tree.model.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date measureTime;

	@DecimalMin(value = "-40.0", message = "온도는 -40도 이상이어야 합니다.")
	@DecimalMax(value = "80.0", message = "온도는 80도 이하여야 합니다.")
	private double temperature;

	@DecimalMin(value = "0.0", message = "습도는 0% 이상이어야 합니다.")
	@DecimalMax(value = "100.0", message = "습도는 100% 이하여야 합니다.")
	private double humidity;

	@DecimalMin(value = "0.0", message = "토양습도는 0% 이상이어야 합니다.")
	@DecimalMax(value = "100.0", message = "토양습도는 100% 이하여야 합니다.")
	private double soilMoisture;
}
