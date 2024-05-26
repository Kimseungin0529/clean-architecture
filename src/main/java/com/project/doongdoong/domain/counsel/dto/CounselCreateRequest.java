package com.project.doongdoong.domain.counsel.dto;

import com.project.doongdoong.domain.counsel.model.CounselType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CounselCreateRequest {
    private Double feellingScore;

    @NotNull(message = "상담 카테코리는 필수입니다.")
    private CounselType counselType;
}
