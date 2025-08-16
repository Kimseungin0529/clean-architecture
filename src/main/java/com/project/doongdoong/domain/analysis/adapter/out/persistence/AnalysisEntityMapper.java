package com.project.doongdoong.domain.analysis.adapter.out.persistence;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component // 매퍼 전용 어노테이션 구현 필
public class AnalysisEntityMapper {

    public AnalysisEntity fromId(Long analysisId) {

        return AnalysisEntity.builder()
                .id(analysisId)
                .build();
    }

    public AnalysisEntity fromModel(Analysis analysis, UserEntity userEntity) {

        return AnalysisEntity.builder()
                .id(analysis.getId())
                .isUsed(analysis.isUsed())
                .user(userEntity)
                .build();
    }

    public Analysis toModel(AnalysisEntity analysisEntity) {

        return Analysis.builder()
                .id(analysisEntity.getId())
                .feelingState(analysisEntity.getFeelingState())
                .analyzedDate(analysisEntity.getAnalyzeTime())
                .isUsed(analysisEntity.isUsed())
                .build();
    }
}
