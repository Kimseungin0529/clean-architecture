package com.project.doongdoong.domain.answer.adapter.out.persistence.repository;

import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from AnswerEntity answer where answer.analysis.id = :analysisId")
    void deleteAnswersById(@Param("analysisId") Long analysisId);

    @Modifying
    @Query("update AnswerEntity a set a.voice = null where a.analysis.id = :analysisId")
    void detachVoiceFromAnswersBy(@Param("analysisId") Long analysisId);

}
