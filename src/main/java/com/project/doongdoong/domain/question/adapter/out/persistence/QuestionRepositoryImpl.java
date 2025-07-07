package com.project.doongdoong.domain.question.adapter.out.persistence;

import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public void deleteQuestionsById(Long analysisId) {
        questionJpaRepository.deleteById(analysisId);
    }
}
