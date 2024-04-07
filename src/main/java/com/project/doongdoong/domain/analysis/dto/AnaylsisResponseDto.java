package com.project.doongdoong.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AnaylsisResponseDto {
    private Long anaylisId;
    private long feelingState;
    private List<String> questionContent;
    private List<String> answerContent;

    @Builder
    public AnaylsisResponseDto(Long anaylisId, long feelingState, List<String> questionContent, List<String> answerContent) {
        this.anaylisId = anaylisId;
        this.feelingState = feelingState;
        this.questionContent = questionContent;
        this.answerContent = answerContent;
    }


}
