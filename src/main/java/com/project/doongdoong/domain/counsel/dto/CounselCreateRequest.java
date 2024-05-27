package com.project.doongdoong.domain.counsel.dto;

import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.model.CounselType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CounselCreateRequest {
    private Long analysisId;

    @NotNull(message = "상담 카테코리는 필수입니다.")
    private CounselType counselType;

    @NotBlank(message = "상담 내용이 존재하지 않습니다.")
    private String question;


}
