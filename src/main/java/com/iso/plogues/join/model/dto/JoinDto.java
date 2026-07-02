package com.iso.plogues.join.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.iso.plogues.util.file.FileDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {
	private Long joinNo;
	private String userId;
	@NotBlank
	private String category;
	@NotBlank
	@Size(max=20, message="참여인원은 20까지 가능합니다")
	private int participants;
	@NotBlank
	@Pattern(regexp="^[a-zA-Z가-힣]*$")
	@Size(min=2, max=50, message="지역은 2글자 이상 50글자까지 작성가능합니다")
	private String region;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDate;
	@NotBlank
	@Pattern(regexp="^[a-zA-Z0-9가-힣]*$")
	@Size(min=2, max=20, message="제목은 2글자 이상 20글자까지 작성가능합니다")
	private String title;
	@NotBlank
	@Pattern(regexp="^[a-zA-Z0-9가-힣]*$")
	@Size(min=2, max=2000, message="내용은 2글자 이상 2000글자까지 작성가능합니다")
	private String content;
	private LocalDateTime createDate;
	private List<FileDto> files;

}