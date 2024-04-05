package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.dto.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalsisRepository;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImp implements AnalysisService{
    private final AnalsisRepository analsisRepository;
    private final QuestionService questionService;

    @Transactional
    @Override //        추가적으로 사용자 정보가 있어야 함.
    public AnalysisCreateResponseDto createAnalysis() {

        List<Question> questions = questionService.createQuestions();
        Analysis analysis = Analysis.builder()
                .questions(questions)
                .build();

        questions.stream().forEach(question -> question.connectAnalysis(analysis)); // 연관관계 편의 메서드

        analsisRepository.save(analysis);

        return AnalysisCreateResponseDto.builder()
                .analysisId(analysis.getId())
                .build();
    }

    @Override
    public Analysis getAnalysis() {
        return null;
    }

    @Override
    public List<Analysis> getAnalysisList() {
        return null;
    }
}
