package com.project.doongdoong.domain.question.adapter.out.persistence.repository;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.AnalysisEntity;
import com.project.doongdoong.domain.question.adapter.out.persistence.entity.QuestionEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from QuestionEntity question where question.analysis.id = :analysisId")
    void deleteQuestionsById(@Param("analysisId") Long analysisId);

    List<QuestionEntity> findAllByAnalysis(AnalysisEntity analysisEntity);

    @Query("select question from QuestionEntity question " +
           "left join fetch question.answer where question.analysis.id = :analysisId")
    List<QuestionEntity> findQuestionsByAnalysisIdWithAnswer(Long analysisId);


    @Query("select question from QuestionEntity question " +
            "left join fetch question.answer " +
            "left join fetch question.analysis " +
            "where question.analysis.id = :analysisId")
    List<QuestionEntity> findAnalysisWithQuestionAndAnswerByAnalysisId(Long analysisId);

    List<QuestionEntity> findAnalysisWithAllByAnalysisId(Long analysisId);

    @Query("select question from QuestionEntity question " +
            "left join fetch question.analysis " +
            "where question.analysis.id in :analysisIds")
    List<QuestionEntity> findQuestionsByAnalysisIdsIn(List<Long> analysisIds);

}
