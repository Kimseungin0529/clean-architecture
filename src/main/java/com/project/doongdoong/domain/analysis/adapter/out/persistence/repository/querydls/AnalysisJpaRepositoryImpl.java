package com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.querydls;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.voice.domain.QVoiceEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import static com.project.doongdoong.domain.analysis.domain.QAnalysisEntity.analysisEntity;
import static com.project.doongdoong.domain.answer.domain.QAnswerEntity.*;
import static com.project.doongdoong.domain.counsel.domain.QCounselEntity.*;
import static com.project.doongdoong.domain.voice.domain.QVoiceEntity.*;

public class AnalysisJpaRepositoryImpl implements AnalysisJpaRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public AnalysisJpaRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<AnalysisEntity> searchFullAnalysisBy(Long analysisId) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(analysisEntity)
                        .leftJoin(analysisEntity.counsel, counselEntity).fetchJoin()
                        .leftJoin(analysisEntity.answers, answerEntity).fetchJoin()
                        .leftJoin(answerEntity.voice, voiceEntity).fetchJoin()
                        .where(analysisEntity.id.eq(analysisId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<AnalysisEntity> searchAnalysisWithVoiceOfAnswer(Long analysisId) {

        return Optional.ofNullable(
                queryFactory.selectFrom(analysisEntity)
                        .leftJoin(analysisEntity.counsel, counselEntity).fetchJoin()
                        .where(analysisEntity.id.eq(analysisId))
                        .fetchOne()
        );
    }
}
