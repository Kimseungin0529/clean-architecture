package com.project.doongdoong.domain.analysis.application.port.in;

import com.project.doongdoong.domain.analysis.adapter.in.dto.*;

public interface AnalysisService {

    public AnalysisCreateResponseDto createAnalysis(String uniqueValue);

    public AnalysisDetailResponse getAnalysis(Long analysisId);

    public AnalysisListResponseDto getAnalysisList(String uniqueValue, int pageNumber);

    public FeelingStateResponseListDto getAnalysisListGroupByDay(String uniqueValue);

    public FellingStateCreateResponse analyzeEmotion(Long analysisId, String uniqueValue);

    public void removeAnalysis(Long analysisId);
}
