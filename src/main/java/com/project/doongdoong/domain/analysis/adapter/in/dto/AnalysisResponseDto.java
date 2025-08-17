package com.project.doongdoong.domain.analysis.adapter.in.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AnalysisResponseDto {
    private Long analysisId;
    private String time;
    private double feelingState;
    private List<String> questionContent;

    @Builder
    public AnalysisResponseDto(Long analysisId, double feelingState, String time, List<String> questionContent) {
        this.analysisId = analysisId;
        this.time = time;
        this.feelingState = feelingState;
        this.questionContent = questionContent;
    }


}
