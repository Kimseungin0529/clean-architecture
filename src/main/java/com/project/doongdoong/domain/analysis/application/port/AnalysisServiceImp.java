package com.project.doongdoong.domain.analysis.application.port;

import com.project.doongdoong.domain.analysis.adapter.in.dto.*;
import com.project.doongdoong.domain.analysis.application.port.in.AnalysisService;
import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.exception.AllAnswersNotFoundException;
import com.project.doongdoong.domain.analysis.exception.AlreadyAnalyzedException;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.question.application.port.in.QuestionProvidable;
import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.user.application.port.out.UserRepository;
import com.project.doongdoong.domain.user.domain.SocialIdentifier;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.domain.voice.application.port.in.VoiceService;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import com.project.doongdoong.domain.voice.exception.VoiceNotFoundException;
import com.project.doongdoong.global.exception.servererror.ExternalApiCallException;
import com.project.doongdoong.global.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImp implements AnalysisService {

    private final VoiceRepository voiceRepository;
    private final UserRepository userRepository;
    private final AnalysisRepository analysisRepository;
    private final QuestionProvidable questionProvider;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final VoiceService voiceService;
    private final WebClientUtil webClientUtil;

    private final static int ANALYSIS_PAGE_SIZE = 10;
    private final static double ANALYSIS_VOICE_RATE = 0.65;
    private final static double ANALYSIS_TEXT_RATE = 0.35;
    private final static String DEFAULT_NO_ANSWER_MESSAGE = "질문에 대한 답변이 없습니다.";
    private final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";


    @Transactional
    @Override
    public AnalysisCreateResponseDto createAnalysis(String uniqueValue) {
        UserEntity userEntity = findUserBy(uniqueValue);
        List<QuestionEntity> questionEntities = questionProvider.createRandomQuestions();

        AnalysisEntity analysisEntity = AnalysisEntity.of(userEntity, questionEntities);

        Map<QuestionContent, VoiceEntity> voicesMap = generateVoicesMapFor(questionEntities);

        analysisRepository.save(analysisEntity);

        List<String> accessUrls = new ArrayList<>();
        List<String> questionTexts = new ArrayList<>();
        List<Long> questionIds = new ArrayList<>();
        linkAnalysisWith(analysisEntity, questionEntities, voicesMap, questionTexts, accessUrls, questionIds);

        return AnalysisCreateResponseDto.of(analysisEntity.getId(), questionIds, questionTexts, accessUrls);
    }

    @Override
    public AnalysisDetailResponse getAnalysis(Long analysisId) {
        AnalysisEntity findAnalysisEntity = analysisRepository.searchAnalysisWithVoiceOfAnswer(analysisId).orElseThrow(AnalysisNotFoundException::new);

        List<QuestionEntity> questionEntities = findAnalysisEntity.getQuestions();
        List<QuestionContent> questionContents = extractQuestionContentsBy(questionEntities);

        List<String> questionTexts = extractQuestionTextBy(questionContents);
        List<Long> questionIds = extractQuestionIdBy(questionEntities);

        List<VoiceEntity> findVoiceEntities = voiceRepository.findVoiceAllByQuestionContentIn(questionContents);
        Map<QuestionContent, String> voiceMap = mapVoiceToQuestionInformation(findVoiceEntities);
        List<String> questionVoiceAccessUrls = findUrlsByMatchingQuestionContentsWithVoice(questionContents, voiceMap);

        List<String> answerContents = findAnswerContentsByMatchingQuestionWithAnswer(questionEntities);

        return AnalysisDetailResponse.of(analysisId, findAnalysisEntity.getFeelingState(), findAnalysisEntity.getCreatedTime().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)),
                questionTexts, questionIds, questionVoiceAccessUrls, answerContents);
    }

    @Override
    public AnalysisListResponseDto getAnalysisList(String uniqueValue, int pageNumber) {
        UserEntity userEntity = findUserBy(uniqueValue);

        PageRequest pageable = PageRequest.of(pageNumber, ANALYSIS_PAGE_SIZE);
        Page<AnalysisEntity> analysisPages = analysisRepository.findAllByUserOrderByCreatedTime(userEntity, pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);


        return AnalysisListResponseDto.of(analysisPages, formatter);
    }

    @Override
    public FeelingStateResponseListDto getAnalysisListGroupByDay(String uniqueValue) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        UserEntity userEntity = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);


        Optional<AnalysisEntity> analysis = analysisRepository.findFirstByUserOrderByAnalyzeTimeDesc(userEntity);
        List<FeelingStateResponseDto> result = null;
        if (analysis.isPresent()) {
            AnalysisEntity findAnalysisEntity = analysis.get();
            LocalDateTime endTime = findAnalysisEntity.getCreatedTime().plusDays(1).truncatedTo(ChronoUnit.DAYS);
            LocalDateTime startTime = endTime.minusDays(6).truncatedTo(ChronoUnit.DAYS);
            result = analysisRepository.findAllByDateBetween(userEntity, startTime.toLocalDate(), endTime.toLocalDate());
        }

        return FeelingStateResponseListDto.builder()
                .feelingStateResponsesDto(result)
                .build();
    }


    @Transactional
    @Override
    public FellingStateCreateResponse analyzeEmotion(Long analysisId, String uniqueValue) {
        // 사용자 찾기
        UserEntity userEntity = findUserWithAnalysisBy(uniqueValue);
        // 오늘
        if (checkToAnalyzeTodayFirstBy(userEntity)) {
            userEntity.growUp();
        }

        AnalysisEntity findAnalysisEntity = analysisRepository.searchFullAnalysisBy(analysisId).orElseThrow(AnalysisNotFoundException::new);
        if (findAnalysisEntity.isMissingAnswers()) { //만약 모든 질문에 대한 답변이 없는 경우, 답변이 부족하다는 예외 발생
            throw new AllAnswersNotFoundException();
        }

        if (findAnalysisEntity.isAlreadyAnalyzed()) { // 분석은 1번만 가능
            throw new AlreadyAnalyzedException();
        }

        List<VoiceEntity> voiceEntities = getVoiceListFrom(findAnalysisEntity);
        List<FellingStateCreateResponse> responseByText = webClientUtil.callAnalyzeEmotion(voiceEntities); // 2. 파일을 request 값으로 외부 lambda API 비동기 처리(동일한 외부 API 4번 호출)
        List<FellingStateCreateResponse> responseByVoice = webClientUtil.callAnalyzeEmotionVoice(voiceEntities);

        List<AnswerEntity> answerEntities = getAnswersFrom(findAnalysisEntity);
        updateContentWithTranscribedTextBy(answerEntities, responseByText);

        double resultByText = calculateFellingStatusAverage(responseByText);
        double resultByVoice = calculateFellingStatusAverage(responseByVoice);
        double result = calculateTotalEmotionScoreFrom(resultByText, resultByVoice);

        findAnalysisEntity.changeFeelingStateAndAnalyzeTime(result, LocalDate.now());

        return FellingStateCreateResponse.builder()
                .feelingState(result)
                .build();
    }

    @Transactional
    @Override
    public void removeAnalysis(Long analysisId) {
        AnalysisEntity findAnalysisEntity = analysisRepository.searchFullAnalysisBy(analysisId)
                .orElseThrow(AnalysisNotFoundException::new);

        answerRepository.detachVoiceFromAnswersBy(analysisId);

        voiceService.deleteVoices(getVoiceListFrom(findAnalysisEntity));
        questionRepository.deleteQuestionsById(analysisId);
        answerRepository.deleteAnswersById(analysisId);
        analysisRepository.deleteAnalysis(analysisId);
    }

    private void linkAnalysisWith(AnalysisEntity analysisEntity, List<QuestionEntity> questionEntities, Map<QuestionContent, VoiceEntity> voicesMap, List<String> questionTexts, List<String> accessUrls, List<Long> questionIds) {
        questionEntities.forEach(question -> {
            question.connectAnalysis(analysisEntity);
            VoiceEntity voiceEntity = Optional.ofNullable(voicesMap.get(question.getQuestionContent()))
                    .orElseThrow(VoiceNotFoundException::new);

            questionTexts.add(question.getQuestionContent().getText());
            questionIds.add(question.getId());
            accessUrls.add(voiceEntity.getAccessUrl());

        });
    }

    private Map<QuestionContent, VoiceEntity> generateVoicesMapFor(List<QuestionEntity> questionEntities) {
        List<QuestionContent> questionContents = questionEntities.stream()
                .map(QuestionEntity::getQuestionContent)
                .collect(Collectors.toList());

        return voiceRepository.findVoiceAllByQuestionContentIn(questionContents)
                .stream()
                .collect(Collectors.toMap(VoiceEntity::getQuestionContent, voice -> voice));
    }

    private UserEntity findUserBy(String uniqueValue) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        return userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);

    }

    private String[] parseUniqueValue(String uniqueValue) {
        return uniqueValue.split("_");
    }

    private List<String> findAnswerContentsByMatchingQuestionWithAnswer(List<QuestionEntity> questionEntities) {
        return questionEntities.stream()
                .map(question ->
                        Optional.ofNullable(question.getAnswer())
                                .map(AnswerEntity::getContent)
                                .orElse(DEFAULT_NO_ANSWER_MESSAGE))
                .collect(Collectors.toList());
    }

    private List<String> findUrlsByMatchingQuestionContentsWithVoice(List<QuestionContent> questionContents, Map<QuestionContent, String> voiceMap) {
        return questionContents.stream()
                .map(voiceMap::get)
                .collect(Collectors.toList());
    }

    private List<Long> extractQuestionIdBy(List<QuestionEntity> questionEntities) {
        return questionEntities.stream()
                .map(QuestionEntity::getId).collect(Collectors.toList());
    }

    private Map<QuestionContent, String> mapVoiceToQuestionInformation(List<VoiceEntity> findVoiceEntities) {
        return findVoiceEntities.stream()
                .collect(Collectors.toMap(
                        VoiceEntity::getQuestionContent,
                        VoiceEntity::getAccessUrl,
                        (existing, replacement) -> existing
                ));
    }

    private List<String> extractQuestionTextBy(List<QuestionContent> questionContents) {
        return questionContents.stream()
                .map(QuestionContent::getText)
                .collect(Collectors.toList());
    }

    private List<QuestionContent> extractQuestionContentsBy(List<QuestionEntity> questionEntities) {
        return questionEntities.stream()
                .map(QuestionEntity::getQuestionContent)
                .collect(Collectors.toList());
    }


    private List<AnswerEntity> getAnswersFrom(AnalysisEntity findAnalysisEntity) {
        return findAnalysisEntity.getAnswers();
    }

    private void updateContentWithTranscribedTextBy(List<AnswerEntity> answerEntities, List<FellingStateCreateResponse> responseByText) {
        if (answerEntities.size() != responseByText.size()) {
            throw new ExternalApiCallException();
        }
        for (int i = 0; i < responseByText.size(); i++) {
            answerEntities.get(i).changeContent(responseByText.get(i).getTranscribedText());
        }
    }

    private double calculateTotalEmotionScoreFrom(double resultByText, double resultByVoice) {
        return ANALYSIS_TEXT_RATE * resultByText + ANALYSIS_VOICE_RATE * resultByVoice;
    }


    private UserEntity findUserWithAnalysisBy(String uniqueValue) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        return userRepository.findUserWithAnalysisBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);
    }

    private double calculateFellingStatusAverage(List<FellingStateCreateResponse> responseByText) {
        return responseByText.stream()
                .mapToDouble(FellingStateCreateResponse::getFeelingState)
                .average()
                .orElseThrow(ExternalApiCallException::new);
    }

    private boolean checkToAnalyzeTodayFirstBy(UserEntity userEntity) {
        List<AnalysisEntity> list = userEntity.getAnalysisList().stream()
                .filter(analysis -> analysis.equalsAnalyzeTimeTo(LocalDate.now()))
                .toList();
        return list.isEmpty();
    }


    private List<VoiceEntity> getVoiceListFrom(AnalysisEntity findAnalysisEntity) {
        return findAnalysisEntity.getAnswers().stream()
                .map(AnswerEntity::getVoice).toList();
    }


}
