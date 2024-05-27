package com.project.doongdoong.domain.counsel.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CounselResultResponse {
    private Long counselId;
    private String counselResult;

    @Builder
    public CounselResultResponse(String counselResult, Long counselId) {
        this.counselResult = counselResult;
        this.counselId = counselId;
    }
}
