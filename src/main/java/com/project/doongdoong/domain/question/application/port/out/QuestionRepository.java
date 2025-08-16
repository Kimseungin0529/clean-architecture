package com.project.doongdoong.domain.question.application.port.out;

import com.project.doongdoong.domain.question.application.port.dto.AnalysisQuestionsAnswersDto;
import com.project.doongdoong.domain.question.application.port.dto.QuestionAnswer;
import com.project.doongdoong.domain.question.domain.Question;

import java.util.List;

public interface QuestionRepository {

    List<Long> saveAll(List<Question> questions, Long id);

    List<Question> findQuestionsFrom(Long analysisId);

    List<QuestionAnswer> findQuestionsByAnalysisIdWithAnswer(Long analysisId);

    AnalysisQuestionsAnswersDto findAnalysisWithQuestionAndAnswerByAnalysisId(Long analysisId);

    List<Question> findQuestionsByAnalysisIdsIn(List<Long> analysisIds);
}
