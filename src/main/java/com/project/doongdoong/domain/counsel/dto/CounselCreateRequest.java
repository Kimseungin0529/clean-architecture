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

    private CounselType counselType;

    private String question;

    public CounselCreateRequest() { // 기본 설정
        this.counselType = CounselType.ETC;
    }
}
