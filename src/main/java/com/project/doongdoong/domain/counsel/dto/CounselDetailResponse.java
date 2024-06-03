package com.project.doongdoong.domain.counsel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CounselDetailResponse {

    private String date;
    private Long counselId;
    private String question;
    private String answer;
    private String counselType;

    @Builder
    public CounselDetailResponse(String data, Long counselId, String question, String answer, String counselType) {
        this.date = data;
        this.counselId = counselId;
        this.question = question;
        this.answer = answer;
        this.counselType = counselType;
    }

}
