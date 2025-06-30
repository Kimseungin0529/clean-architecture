package com.project.doongdoong.domain.question.application.port.out;

import com.project.doongdoong.domain.question.domain.QuestionEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from QuestionEntity question where question.analysis.id = :analysisId")
    void deleteQuestionsById(@Param("analysisId") Long analysisId);
}
