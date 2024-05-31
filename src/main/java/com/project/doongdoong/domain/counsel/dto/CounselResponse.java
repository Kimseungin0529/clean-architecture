package com.project.doongdoong.domain.counsel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CounselResponse {

    private Long counselId;
    private boolean isAnalysisUsed;
    private String counselType;

    @Builder
    public CounselResponse(Long counselId, boolean isAnalysisUsed, String counselType) {
        this.counselId = counselId;
        this.isAnalysisUsed = isAnalysisUsed;
        this.counselType = counselType;
    }
}
