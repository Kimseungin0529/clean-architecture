package com.project.doongdoong.domain.analysis.application.port;

import com.project.doongdoong.domain.analysis.adapter.in.dto.*;
import com.project.doongdoong.domain.analysis.application.port.in.AnalysisService;
import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.exception.AllAnswersNotFoundException;
import com.project.doongdoong.domain.analysis.exception.AlreadyAnalyzedException;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.question.application.port.in.QuestionProvidable;
import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.user.application.port.out.UserRepository;
import com.project.doongdoong.domain.user.domain.SocialIdentifier;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.domain.voice.application.port.in.VoiceService;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.global.exception.servererror.ExternalApiCallException;
import com.project.doongdoong.global.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        User user = findUserBy(uniqueValue);
        List<Question> questions = questionProvider.createRandomQuestions();

        Analysis analysis = Analysis.of(user, questions);

        Map<QuestionContent, Voice> voicesMap = generateVoicesMapFor(questions);

        List<Long> questionIds = getQuestionIdsFrom(questions);
        List<QuestionContent> questionTexts = getQuestionTextsFrom(questions);
        List<String> accessUrls = extractAccessUrlsFrom(questions, voicesMap);

        analysisRepository.save(analysis);

        return AnalysisCreateResponseDto.of(analysis.getId(), questionIds, questionTexts, accessUrls);
    }

    private static List<QuestionContent> getQuestionTextsFrom(List<Question> questions) {
        return questions.stream()
                .map(Question::getQuestionContent)
                .toList();
    }

    private static List<Long> getQuestionIdsFrom(List<Question> questions) {
        return questions.stream()
                .map(Question::getId)
                .toList();
    }

    private List<String> extractAccessUrlsFrom(List<Question> questions, Map<QuestionContent, Voice> voicesMap) {
        return questions.stream()
                .map(question -> voicesMap.get(question.getQuestionContent()))
                .map(Voice::getAccessUrl)
                .toList();
    }

    @Override
    public AnalysisDetailResponse getAnalysis(Long analysisId) {
        Analysis findAnalysis = analysisRepository.searchAnalysisWithVoiceOfAnswer(analysisId).orElseThrow(AnalysisNotFoundException::new);

        List<Question> questions = findAnalysis.getQuestions();
        List<QuestionContent> questionContents = extractQuestionContentsBy(questions);

        List<String> questionTexts = extractQuestionTextBy(questionContents);
        List<Long> questionIds = extractQuestionIdBy(questions);

        List<Voice> findVoice = voiceRepository.findVoiceAllByQuestionContentIn(questionContents);
        Map<QuestionContent, String> voiceMap = mapVoiceToQuestionInformation(findVoice);
        List<String> questionVoiceAccessUrls = findUrlsByMatchingQuestionContentsWithVoice(questionContents, voiceMap);

        List<String> answerContents = findAnswerContentsByMatchingQuestionWithAnswer(questions);

        return AnalysisDetailResponse.of(analysisId, findAnalysis.getFeelingState(), findAnalysis.getAnalyzedDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)),
                questionTexts, questionIds, questionVoiceAccessUrls, answerContents);
    }

    @Override
    public AnalysisListResponseDto getAnalysisList(String uniqueValue, int pageNumber) {
        User user = findUserBy(uniqueValue);

        PageRequest pageable = PageRequest.of(pageNumber, ANALYSIS_PAGE_SIZE);
        Page<Analysis> analysisPages = analysisRepository.findAllByUserOrderByCreatedTime(user, pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);


        return AnalysisListResponseDto.of(analysisPages, formatter);
    }

    @Override
    public FeelingStateResponseListDto getAnalysisListGroupByDay(String uniqueValue) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        User user = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);


        Optional<Analysis> analysis = analysisRepository.findFirstByUserOrderByAnalyzeTimeDesc(user);
        List<FeelingStateResponseDto> result = null;
        if (analysis.isPresent()) {
            Analysis findAnalysis = analysis.get();
            LocalDate endTime = findAnalysis.getAnalyzedDate().plusDays(1);
            LocalDate startTime = endTime.minusDays(6);
            result = analysisRepository.findAllByDateBetween(user, startTime, endTime);
        }

        return FeelingStateResponseListDto.builder()
                .feelingStateResponsesDto(result)
                .build();
    }


    @Transactional
    @Override
    public FellingStateCreateResponse analyzeEmotion(Long analysisId, String uniqueValue) {

        Analysis findAnalysis = analysisRepository.searchFullAnalysisBy(analysisId).orElseThrow(AnalysisNotFoundException::new);
        boolean allAnswerPresent = findAnalysis.getQuestions()
                .stream().anyMatch(question -> question.getAnswer() != null);

        if (allAnswerPresent) { //만약 모든 질문에 대한 답변이 없는 경우, 답변이 부족하다는 예외 발생
            throw new AllAnswersNotFoundException();
        }

        if (findAnalysis.isAlreadyAnalyzed()) { // 분석은 1번만 가능
            throw new AlreadyAnalyzedException();
        }

        List<Voice> voices = getVoiceListFrom(findAnalysis);
        List<FellingStateCreateResponse> responseByText = webClientUtil.callAnalyzeEmotion(voices); // 2. 파일을 request 값으로 외부 lambda API 비동기 처리(동일한 외부 API 4번 호출)
        List<FellingStateCreateResponse> responseByVoice = webClientUtil.callAnalyzeEmotionVoice(voices);

        List<Answer> answers = getAnswersFrom(findAnalysis);
        updateContentWithTranscribedTextBy(answers, responseByText);

        double resultByText = calculateFellingStatusAverage(responseByText);
        double resultByVoice = calculateFellingStatusAverage(responseByVoice);
        double result = calculateTotalEmotionScoreFrom(resultByText, resultByVoice);

        findAnalysis.changeFeelingStateAndAnalyzeTime(result, LocalDate.now());

        return FellingStateCreateResponse.builder()
                .feelingState(result)
                .build();
    }

    @Transactional
    @Override
    public void removeAnalysis(Long analysisId) {
        Analysis findAnalysis = analysisRepository.searchFullAnalysisBy(analysisId)
                .orElseThrow(AnalysisNotFoundException::new);

        answerRepository.detachVoiceFromAnswersBy(analysisId);

        voiceService.deleteVoices(getVoiceListFrom(findAnalysis));
        questionRepository.deleteQuestionsById(analysisId);
        answerRepository.deleteAnswersById(analysisId);
        analysisRepository.deleteAnalysis(analysisId);
    }


    private Map<QuestionContent, Voice> generateVoicesMapFor(List<Question> questions) {
        List<QuestionContent> questionContents = questions.stream()
                .map(Question::getQuestionContent)
                .collect(Collectors.toList());

        return voiceRepository.findVoiceAllByQuestionContentIn(questionContents)
                .stream()
                .collect(Collectors.toMap(Voice::getQuestionContent, voice -> voice));
    }

    private User findUserBy(String uniqueValue) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        return userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);

    }

    private String[] parseUniqueValue(String uniqueValue) {
        return uniqueValue.split("_");
    }

    private List<String> findAnswerContentsByMatchingQuestionWithAnswer(List<Question> questions) {
        return questions.stream()
                .map(question ->
                        Optional.ofNullable(question.getAnswer())
                                .map(Answer::getContent)
                                .orElse(DEFAULT_NO_ANSWER_MESSAGE))
                .collect(Collectors.toList());
    }

    private List<String> findUrlsByMatchingQuestionContentsWithVoice(List<QuestionContent> questionContents, Map<QuestionContent, String> voiceMap) {
        return questionContents.stream()
                .map(voiceMap::get)
                .collect(Collectors.toList());
    }

    private List<Long> extractQuestionIdBy(List<Question> questions) {
        return questions.stream()
                .map(Question::getId).collect(Collectors.toList());
    }

    private Map<QuestionContent, String> mapVoiceToQuestionInformation(List<Voice> findVoices) {
        return findVoices.stream()
                .collect(Collectors.toMap(
                        Voice::getQuestionContent,
                        Voice::getAccessUrl,
                        (existing, replacement) -> existing
                ));
    }

    private List<String> extractQuestionTextBy(List<QuestionContent> questionContents) {
        return questionContents.stream()
                .map(QuestionContent::getText)
                .collect(Collectors.toList());
    }

    private List<QuestionContent> extractQuestionContentsBy(List<Question> questionEntities) {
        return questionEntities.stream()
                .map(Question::getQuestionContent)
                .collect(Collectors.toList());
    }


    private List<Answer> getAnswersFrom(Analysis findAnalysis) {
        return findAnalysis.getQuestions()
                .stream()
                .map(Question::getAnswer)
                .toList();
    }

    private void updateContentWithTranscribedTextBy(List<Answer> answers, List<FellingStateCreateResponse> responseByText) {
        if (answers.size() != responseByText.size()) {
            throw new ExternalApiCallException();
        }
        for (int i = 0; i < responseByText.size(); i++) {
            answers.get(i).changeContent(responseByText.get(i).getTranscribedText());
        }
    }

    private double calculateTotalEmotionScoreFrom(double resultByText, double resultByVoice) {
        return ANALYSIS_TEXT_RATE * resultByText + ANALYSIS_VOICE_RATE * resultByVoice;
    }


    private User findUserWithAnalysisBy(String uniqueValue) {
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


    private List<Voice> getVoiceListFrom(Analysis findAnalysis) {
        return findAnalysis.getQuestions().stream()
                .map(Question::getAnswer)
                .map(Answer::getVoice)
                .toList();
    }


}
