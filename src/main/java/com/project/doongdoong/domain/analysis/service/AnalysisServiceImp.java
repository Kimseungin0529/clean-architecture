package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.dto.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.analysis.dto.AnaylsisListResponseDto;
import com.project.doongdoong.domain.analysis.dto.AnaylsisResponseDto;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.repository.QuestionRepository;
import com.project.doongdoong.domain.question.service.QuestionService;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImp implements AnalysisService{
    private final UserRepository userRepository;
    private final AnalysisRepository analsisRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    private final static int ANALYSIS_PAGE_SIZE = 10;

    @Transactional
    @Override //        추가적으로 사용자 정보가 있어야 함.
    public AnalysisCreateResponseDto createAnalysis(String uniqueValue) {
        String[] values = parseUniqueValue(uniqueValue);
        User user = userRepository.findBySocialTypeAndSocialId(SocialType.customValueOf(values[1]), values[0])
                .orElseThrow(() -> new UserNotFoundException());

        List<Question> questions = questionService.createQuestions();
        Analysis analysis = Analysis.builder()
                .user(user)
                .questions(questions)
                .build();

        for(int i=0; i<questions.size(); i++){
            Question question = questions.get(i);
            question.connectAnalysis(analysis);
        } // ConcurrentModificationException 으로 인해 for문 사용

        analsisRepository.save(analysis);

        return AnalysisCreateResponseDto.builder()
                .analysisId(analysis.getId())
                .build();
    }

    private static String[] parseUniqueValue(String uniqueValue) {
        String[] values = uniqueValue.split("_"); // 사용자 찾기
        return values;
    }

    @Override
    public AnaylsisResponseDto getAnalysis(Long analysisId) {
        Analysis findAnalysis = analsisRepository.findById(analysisId).orElseThrow(() -> new AnalysisNotFoundException());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return AnaylsisResponseDto.builder()
                .anaylisId(findAnalysis.getId())
                .time(findAnalysis.getCreatedTime().format(formatter))
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
    public AnaylsisListResponseDto getAnalysisList(String uniqueKey, int pageNumber) {
        String[] values = parseUniqueValue(uniqueKey);
        User user = userRepository.findBySocialTypeAndSocialId(SocialType.customValueOf(values[1]), values[0])
                .orElseThrow(() -> new UserNotFoundException());

        PageRequest pageable = PageRequest.of(pageNumber, ANALYSIS_PAGE_SIZE);
        Page<Analysis> analysisPages = analsisRepository.findAllByUserOrderByCreatedTime(user, pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        return AnaylsisListResponseDto.builder()
                .pageNumber(analysisPages.getNumber() + 1)
                .totalPage(analysisPages.getTotalPages())
                .anaylsisResponseDtoList(analysisPages.getContent().stream()
                        .map(analysis -> AnaylsisResponseDto.builder()
                                .anaylisId(analysis.getId())
                                .time(analysis.getCreatedTime().format(formatter))
                                .feelingState(analysis.getFeelingState())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public AnaylsisListResponseDto getAnalysisListGroupByDay(String uniqueValue) {
        return null;
    }


}
