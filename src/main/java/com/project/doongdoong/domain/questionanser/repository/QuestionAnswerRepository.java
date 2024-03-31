package com.project.doongdoong.domain.questionanser.repository;

import com.project.doongdoong.domain.questionanser.model.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
}
