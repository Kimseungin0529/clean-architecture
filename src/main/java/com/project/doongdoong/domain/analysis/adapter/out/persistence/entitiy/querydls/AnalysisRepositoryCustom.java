package com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.querydls;

import com.project.doongdoong.domain.analysis.domain.Analysis;

import java.util.Optional;

public interface AnalysisRepositoryCustom {
    Optional<Analysis> searchFullAnalysisBy(Long analysisId);

    Optional<Analysis> searchAnalysisWithVoiceOfAnswer(Long analysisId);
}
