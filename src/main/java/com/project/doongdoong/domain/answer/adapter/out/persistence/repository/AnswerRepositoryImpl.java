package com.project.doongdoong.domain.answer.adapter.out.persistence.repository;

import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {
    private final AnswerJpaRepository answerJpaRepository;

    @Override
    public Answer save(Answer answer) {
        return answerJpaRepository.save(AnswerEntity.fromModel(answer)).toModel();
    }

    @Override
    public void deleteAnswersById(Long analysisId) {
        answerJpaRepository.deleteById(analysisId);
    }

    @Override
    public void detachVoiceFromAnswersBy(Long analysisId) {
        answerJpaRepository.detachVoiceFromAnswersBy(analysisId);
    }
}
