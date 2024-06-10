package com.project.doongdoong.domain.counsel.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CounselResultResponse {
    private Long counselId;
    private String counselContent;
    private String imageUrl;

    @Builder
    public CounselResultResponse(Long counselId, String counselContent, String imageUrl) {
        this.counselId = counselId;
        this.counselContent = counselContent;
        this.imageUrl = imageUrl;
    }
}
