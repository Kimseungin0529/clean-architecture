package com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.querydls;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;

import java.util.Optional;

public interface AnalysisJpaRepositoryCustom {
    Optional<AnalysisEntity> searchFullAnalysisBy(Long analysisId);

    Optional<AnalysisEntity> searchAnalysisWithVoiceOfAnswer(Long analysisId);
}
