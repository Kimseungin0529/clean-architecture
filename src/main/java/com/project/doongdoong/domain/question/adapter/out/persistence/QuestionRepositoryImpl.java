package com.project.doongdoong.domain.question.adapter.out.persistence;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.AnalysisEntityMapper;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.answer.adapter.out.persistence.repository.AnswerEntityMapper;
import com.project.doongdoong.domain.question.application.port.dto.AnalysisQuestionsAnswersDto;
import com.project.doongdoong.domain.question.application.port.dto.QuestionAnswer;
import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionJpaRepository questionJpaRepository;

    private final AnalysisEntityMapper analysisEntityMapper;
    private final QuestionEntityMapper questionEntityMapper;
    private final AnswerEntityMapper answerEntityMapper;

    @Override
    public List<Long> saveAll(List<Question> questions, Long analysisId) {

        List<QuestionEntity> questionEntities = questions.stream()
                .map(question -> questionEntityMapper.fromModel(question, analysisEntityMapper.fromId(analysisId), null))
                .toList();

        return questionJpaRepository.saveAll(questionEntities).stream()
                .map(QuestionEntity::getId)
                .toList();
    }

    @Override
    public List<Question> findQuestionsFrom(Long analysisId) {
        List<QuestionEntity> questionEntities = questionJpaRepository.findAllByAnalysis(analysisEntityMapper.fromId(analysisId));

        return questionEntities.stream()
                .map(questionEntityMapper::toModel)
                .toList();
    }

    @Override
    public List<QuestionAnswer> findQuestionsByAnalysisIdWithAnswer(Long analysisId) {
        List<QuestionEntity> questionEntities = questionJpaRepository.findQuestionsByAnalysisIdWithAnswer(analysisId);

        return questionEntities.stream()
                .map(questionEntity -> QuestionAnswer.of(
                                questionEntityMapper.toModel(questionEntity),
                                answerEntityMapper.toModel(questionEntity.getAnswer())
                        )
                ).toList();
    }

    @Override
    public AnalysisQuestionsAnswersDto findAnalysisWithQuestionAndAnswerByAnalysisId(Long analysisId) {
        List<QuestionEntity> questionEntities = questionJpaRepository.findAnalysisWithQuestionAndAnswerByAnalysisId(analysisId);
        Analysis analysis = analysisEntityMapper.toModel(questionEntities.get(0).getAnalysis());

        return AnalysisQuestionsAnswersDto.of(analysis,
                questionEntities.stream().map(questionEntityMapper::toModel).toList(),
                questionEntities.stream().map(questionEntity -> answerEntityMapper.toModel(questionEntity.getAnswer())).toList()
        );

    }

    @Override
    public List<Question> findQuestionsByAnalysisIdsIn(List<Long> analysisIds) {
        return questionJpaRepository.findQuestionsByAnalysisIdsIn(analysisIds)
                .stream()
                .map(questionEntityMapper::toModel)
                .toList();
    }
}
