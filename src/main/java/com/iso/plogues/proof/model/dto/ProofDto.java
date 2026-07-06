package com.iso.plogues.proof.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.iso.plogues.util.file.FileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProofDto {

    private Long proofNo;
    private String userId;
    private Long joinNo;
    @NotBlank(message="제목을 입력해 주세요")
    @Size(min=2, max=20, message="제목은 2글자 이상 20글자까지 작성가능합니다")
    private String title;
    @NotBlank(message="분류를 입력해 주세요")
    private String category;
    @NotNull(message="수량을 입력해 주세요")
    private Double quantity;
    @NotBlank(message="내용을 입력해 주세요")
    @Size(min=2, max=2000, message="내용은 2글자 이상 2000글자까지 작성가능합니다")
    private String content;
    private LocalDateTime createDate;
    private String deleted;
    private String updated;
    private List<FileDto> files;

}













