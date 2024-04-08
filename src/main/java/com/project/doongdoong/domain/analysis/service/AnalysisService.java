package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.dto.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.analysis.dto.AnaylsisResponseDto;

import java.util.List;

public interface AnalysisService {

    public AnalysisCreateResponseDto createAnalysis(String socialId);

    public AnaylsisResponseDto getAnalysis(Long analysisId);

    public List<AnaylsisResponseDto> getAnalysisList();

}
