package com.iso.plogues.proof.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.iso.plogues.util.file.FileDto;
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
    private String title;
    private String category;
    private Double quantity;
    private String content;
    private LocalDateTime createDate;
    private String deleted;
    private String updated;
    private List<FileDto> files;

}













