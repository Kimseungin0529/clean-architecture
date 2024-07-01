package com.project.doongdoong.global.dto.response;

import lombok.Getter;

@Getter
public class CounselAiResponse {
    private String answer;
    private String imageUrl;

    public CounselAiResponse(String answer, String imageUrl) {
        this.answer = answer;
        this.imageUrl = imageUrl;
    }
}
