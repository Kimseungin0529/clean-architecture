package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.dto.response.*;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.answer.model.Answer;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.question.service.QuestionService;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.domain.voice.dto.response.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
import com.project.doongdoong.domain.voice.service.VoiceService;
import com.project.doongdoong.global.util.GoogleTtsProvider;
import com.project.doongdoong.global.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImp implements AnalysisService{

    private final GoogleTtsProvider googleTtsProvider;
    private final VoiceRepository voiceRepository;
    private final UserRepository userRepository;
    private final AnalysisRepository analsisRepository;
    private final QuestionService questionService;
    private final VoiceService voiceService;
    private final WebClientUtil webClientUtil;
    private final static long WEEK_TIME = 60 * 60 * 24 * 7;

    private final static int ANALYSIS_PAGE_SIZE = 10;

    @Transactional
    @Override //        추가적으로 사용자 정보가 있어야 함.
    public AnalysisCreateResponseDto createAnalysis(String uniqueValue) {
        String[] values = parseUniqueValue(uniqueValue); // 사용자 정보 찾기
        User user = userRepository.findBySocialTypeAndSocialId(SocialType.customValueOf(values[1]), values[0])
                .orElseThrow(() -> new UserNotFoundException());

        List<Question> questions = questionService.createQuestions(); // 질문 가져오기
        Analysis analysis = Analysis.builder()
                .user(user)
                .questions(questions)
                .build();

        List<String> accessUrls = new ArrayList<>(); // 음성 파일 접근 url 리스트
        List<String> questionTexts = new ArrayList<>(); // 질문에 대한 내용 텍스트 리스트
        for(int i=0; i<questions.size(); i++){
            Question question = questions.get(i);
            question.connectAnalysis(analysis); // 연관관계 편의 메서드


            Optional<Voice> voice = voiceRepository.findVoiceByQuestionContent(question.getQuestionContent());
            if(voice.isPresent()){ // voiceRepository를 통해 이미 저장된 음성 파일이라면 TTS 과정 생략하고 음성 조회하기
                accessUrls.add(voice.get().getAccessUrl());
            }else { // 새로운 음성 파일 생성이라면 TTS 과정을 통해 음성 파일 저장하고 반환하기
                byte[] bytes = googleTtsProvider.convertTextToSpeech(question.getQuestionContent().getText());// 질문 내용 -> 음성 파일 변환
                String filename = "voice-question" + String.valueOf(question.getQuestionContent().getNumber());
                VoiceDetailResponseDto voiceDto = voiceService.saveTtsVoice(bytes, filename, question.getQuestionContent());
                accessUrls.add(voiceDto.getAccessUrl());
            }
            questionTexts.add(question.getQuestionContent().getText());

        } // ConcurrentModificationException 으로 인해 for문 사용

        analsisRepository.save(analysis);

        return AnalysisCreateResponseDto.builder()
                .analysisId(analysis.getId())
                .questionTexts(questionTexts)
                .accessUrls(accessUrls)
                .build();
    }

    private static String[] parseUniqueValue(String uniqueValue) {
        String[] values = uniqueValue.split("_"); // 사용자 찾기
        return values;
    }

    @Override
    public AnalysisDetailResponse getAnalysis(Long analysisId) {
        Analysis findAnalysis = analsisRepository.findById(analysisId).orElseThrow(() -> new AnalysisNotFoundException());
        List<Question> questions = findAnalysis.getQuestions();
        // questions에 해당하는 answers 가져오기

        List<QuestionContent> questionContents = extractQuestionContentsBy(questions);
        List<String> questionTexts = extractQuestionTextBy(questionContents);

        List<Voice> findVoices = voiceRepository.findVoiceAllByQuestionContentIn(questionContents);

        Map<QuestionContent, String> voiceMap = mapVoiceToQuestionInformation(findVoices);
        List<String> questionVoiceAccessUrls = questionContents.stream()
                .map(voiceMap::get)
                .collect(Collectors.toList());

        List<String> answerContents = questions.stream()
                .map(question -> Optional.ofNullable(question.getAnswer())
                        .map(answer -> answer.getContent())
                        .orElse(null))
                .collect(Collectors.toList());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return AnalysisDetailResponse.builder()
                .analysisId(findAnalysis.getId())
                .time(findAnalysis.getCreatedTime().format(formatter))
                .feelingState(findAnalysis.getFeelingState())
                .questionContent(questionTexts)
                .questionContentVoiceUrls(questionVoiceAccessUrls)
                .answerContent(answerContents)
                .build();
    }

    private static Map<QuestionContent, String> mapVoiceToQuestionInformation(List<Voice> findVoices) {
        return findVoices.stream()
                .collect(Collectors.toMap(
                        Voice::getQuestionContent,
                        Voice::getAccessUrl,
                        (existing, replacement) -> existing
                ));
    }

    private static List<String> extractQuestionTextBy(List<QuestionContent> questionContents) {
        List<String> questionTexts = questionContents.stream()
                .map(questionContent -> questionContent.getText())
                .collect(Collectors.toList());
        return questionTexts;
    }

    private static List<QuestionContent> extractQuestionContentsBy(List<Question> questions) {
        List<QuestionContent> questionContents = questions.stream()
                .map(question -> question.getQuestionContent())
                .collect(Collectors.toList());
        return questionContents;
    }

    @Override
    public AnaylsisListResponseDto getAnalysisList(String uniqueValue, int pageNumber) {
        String[] values = parseUniqueValue(uniqueValue);
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
                                .analysisId(analysis.getId())
                                .time(analysis.getCreatedTime().format(formatter))
                                .feelingState(analysis.getFeelingState())
                                .questionContent(analysis.getQuestions().stream()
                                        .map(a -> a.getQuestionContent().getText())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public FeelingStateResponseListDto getAnalysisListGroupByDay(String uniqueValue) {
        String[] values = parseUniqueValue(uniqueValue);
        User user = userRepository.findBySocialTypeAndSocialId(SocialType.customValueOf(values[1]), values[0])
                .orElseThrow(() -> new UserNotFoundException());

        Optional<Analysis> analysis = analsisRepository.findFirstByUserOrderByCreatedTimeDesc(user);
        LocalDateTime endTime, startTime;
        List<FeelingStateResponseDto> result = null;
        if(analysis.isPresent())
        {
            Analysis findAnalysis = analysis.get();
            endTime = findAnalysis.getCreatedTime().plusDays(1).truncatedTo(ChronoUnit.DAYS);
            startTime = endTime.minusDays(6).truncatedTo(ChronoUnit.DAYS);
            result = analsisRepository.findAllByDateBetween(user, startTime, endTime);
        }

        return FeelingStateResponseListDto.builder()
                .feelingStateResponsesDto(result)
                .build();
    }

    @Override
    public FellingStateCreateResponse analyzeEmotion(Long analysisId) {
        Analysis findAnalysis = analsisRepository.searchAnalysisWithVoiceOfAnswer(analysisId).orElseThrow(() -> new AnalysisNotFoundException());
        // 1. 분석에 대한 답변 매칭 파일 리스트 가져오기
        List<Voice> voices = findAnalysis.getAnswers().stream()
                .map(answer -> answer.getVoice())
                .collect(Collectors.toList());

        // 2. 파일을 request 값으로 외부 lambda API 비동기 처리(동일한 외부 API 4번 호출)
        // 해당하는 외부 API는 double 값 하나를 줌.
        List<FellingStateCreateResponse> response = webClientUtil.callAnalyzeEmotion(voices); // 비동기 처리해서 값 가져오기


        double result = response.stream() // 3. 처리가 끝나면 double값 4개 값을 평균 내서 감정 수치 값 반환하기
                .mapToDouble(value -> value.getFeelingState())
                .average()
                .getAsDouble();

        findAnalysis.changeFeelingState(result);

        return FellingStateCreateResponse.builder()
                .feelingState(result)
                .build();
    }

    @Transactional
    @Override
    public void removeAnaylsis(Long analysisId) {
        // anlaysis와 관련된 answer의 voice에 해당하는 S3 파일 삭제 로직
        Analysis findAnalysis = analsisRepository.searchAnalysisWithVoiceOfAnswer(analysisId).orElseThrow(() -> new AnalysisNotFoundException());
        List<String> accessUrls = findAnalysis.getAnswers().stream().map(answer -> answer.getVoice().getAccessUrl())
                .collect(Collectors.toList());
        findAnalysis.getAnswers().stream().forEach(answer -> answer.disconnectWithVoice()); // 연관관계 끊기
        voiceService.deleteVoices(accessUrls); // voice를 참조하는 객체 없으므로 삭제 가능 -> 벌크 삭제로 쿼리 최적화 필요
        // analysis 삭제로 question, answer 삭제 로직 -> voiceService.deleteVoices로 answer.voice와 관련 S3 파일은 이미 삭제.
        analsisRepository.deleteById(analysisId);
    }




}
