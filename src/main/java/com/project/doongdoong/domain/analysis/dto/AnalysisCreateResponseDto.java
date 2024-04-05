package com.project.doongdoong.domain.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnalysisCreateResponseDto {

    private Long analysisId;

    @Builder
    public AnalysisCreateResponseDto(Long analysisId){
        this.analysisId = analysisId;

    }
}
