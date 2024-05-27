package com.project.doongdoong.domain.counsel.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CounselResultResponse {

    private String counselResult;

    @Builder
    public CounselResultResponse(String counselResult) {
        this.counselResult = counselResult;
    }
}
