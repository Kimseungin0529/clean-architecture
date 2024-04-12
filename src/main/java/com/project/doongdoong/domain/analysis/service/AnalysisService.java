package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.dto.*;

import java.util.List;

public interface AnalysisService {

    public AnalysisCreateResponseDto createAnalysis(String uniqueValue);

    public AnaylsisResponseDto getAnalysis(Long analysisId);

    public AnaylsisListResponseDto getAnalysisList(String uniqueValue, int pageNumber);

    public FeelingStateResponseListDto getAnalysisListGroupByDay(String uniqueValue);

}
