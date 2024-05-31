package com.project.doongdoong.domain.counsel.dto;

import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.model.CounselType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CounselCreateRequest {

    private Long analysisId;
    @NotBlank(message = "카테코리는 필수입니다.")
    private String counselType;
    @NotBlank(message = "상담 질문은 필수입니다.")
    private String question;

}
