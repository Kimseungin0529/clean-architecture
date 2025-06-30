package com.project.doongdoong.domain.question.application.port.out;

import com.project.doongdoong.domain.question.domain.Question;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Question question where question.analysis.id = :analysisId")
    void deleteQuestionsById(@Param("analysisId") Long analysisId);
}
