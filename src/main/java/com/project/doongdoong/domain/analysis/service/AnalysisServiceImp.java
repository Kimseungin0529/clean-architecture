package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.dto.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.analysis.dto.AnaylsisResponseDto;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.repository.QuestionRepository;
import com.project.doongdoong.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImp implements AnalysisService{
    private final AnalysisRepository analsisRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    @Transactional
    @Override //        추가적으로 사용자 정보가 있어야 함.
    public AnalysisCreateResponseDto createAnalysis() {
        // 사용자 찾기 로직 필요
        List<Question> questions = questionService.createQuestions();
        Analysis analysis = Analysis.builder()
                //.user(null)
                .questions(questions)
                .build();

        for(int i=0; i<questions.size(); i++){
            Question question = questions.get(i);
            question.connectAnalysis(analysis);
        } // ConcurrentModificationException 으로 인해 for문 사용 /

        analsisRepository.save(analysis);
        questionRepository.saveAll(questions);

        return AnalysisCreateResponseDto.builder()
                .analysisId(analysis.getId())
                .build();
    }

    @Override
    public AnaylsisResponseDto getAnalysis(Long analysisId) {
        Analysis findAnalysis = analsisRepository.findById(analysisId).orElseThrow(() -> new AnalysisNotFoundException());


        for(Question question : findAnalysis.getQuestions()){
            log.info("question.getQuestionContent().getText() = {}", question.getQuestionContent().getText());
        }

        return AnaylsisResponseDto.builder()
                .anaylisId(findAnalysis.getId())
                .feelingState(findAnalysis.getFeelingState())
                .questionContent(findAnalysis.getQuestions().stream()
                        .map(question -> question.getQuestionContent().getText())
                        .collect(Collectors.toList()))
                .answerContent(findAnalysis.getAnswers().stream()
                        .map(answer -> answer.getContent())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<AnaylsisResponseDto> getAnalysisList() {
        return null;
    }


}
