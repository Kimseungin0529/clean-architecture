package com.project.doongdoong.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AnaylsisResponseDto {
    private Long anaylisId;
    private String time;
    private double feelingState;
    private List<String> questionContent;
    private List<String> questionContentVoiceUrls;
    private List<String> answerContent;

    @Builder
    public AnaylsisResponseDto(Long anaylisId, double feelingState, String time, List<String> questionContent
            , List<String> questionContentVoiceUrls, List<String> answerContent) {
        this.anaylisId = anaylisId;
        this.time = time;
        this.feelingState = feelingState;
        this.questionContent = questionContent;
        this.questionContentVoiceUrls = questionContentVoiceUrls;
        this.answerContent = answerContent;
    }


}
