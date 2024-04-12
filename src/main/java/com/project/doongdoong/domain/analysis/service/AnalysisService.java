package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.dto.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.analysis.dto.AnaylsisListResponseDto;
import com.project.doongdoong.domain.analysis.dto.AnaylsisResponseDto;

import java.util.List;

public interface AnalysisService {

    public AnalysisCreateResponseDto createAnalysis(String uniqueValue);

    public AnaylsisResponseDto getAnalysis(Long analysisId);

    public AnaylsisListResponseDto getAnalysisList(String uniqueValue, int pageNumber);

    public AnaylsisListResponseDto getAnalysisListGroupByDay(String uniqueValue);

}
