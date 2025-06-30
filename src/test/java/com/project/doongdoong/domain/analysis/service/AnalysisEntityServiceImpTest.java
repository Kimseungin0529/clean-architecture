package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.adapter.in.dto.*;
import com.project.doongdoong.domain.analysis.application.port.in.AnalysisService;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.exception.AllAnswersNotFoundException;
import com.project.doongdoong.domain.analysis.exception.AlreadyAnalyzedException;
import com.project.doongdoong.domain.answer.adapter.out.persistence.repository.AnswerJpaRepository;
import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.module.IntegrationSupportTest;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.application.port.out.UserRepository;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnalysisEntityServiceImpTest extends IntegrationSupportTest {

    // TODO: 2024-08-01 : 남은 메서드 :




    // TODO: 2024-08-01 : 완료 메서드 : createAnalysis, getAnalysis, getAnalysisList

    @Autowired
    AnalysisService analysisService;

    @Autowired UserRepository userRepository;
    @Autowired VoiceRepository voiceRepository;
    @Autowired
    AnswerJpaRepository answerJpaRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    AnalysisJpaRepository analysisJpaRepository;

    @TestFactory
    @DisplayName("서비스 회원 정보와 존재하지 않는 서비스 회원 정보의 경우, 분석에 관한 정보 접근 시나리오")
    Collection<DynamicTest> createAnalysis(){
        //given
        for(QuestionContent questionContent : QuestionContent.values()){
            VoiceEntity voiceEntity = VoiceEntity.initVoiceContentBuilder()
                    .originName(questionContent.getText() +"_voice.mp3")
                    .questionContent(questionContent)
                    .build();
            voiceEntity.changeAccessUrl("임의의 접근 url 주소");
            voiceRepository.save(voiceEntity);
        }

        UserEntity userEntity1 = createUser("socialId", SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity1);
        String uniqueValue1 = savedUserEntity.getSocialId() + "_" + savedUserEntity.getSocialType().getDescription();

        int analysisRelatedSize = 4;
        List<String> allQuestionTexts = Arrays.stream(QuestionContent.values())
                .map(QuestionContent::getText)
                .collect(Collectors.toList());

        //when
        return List.of(
                DynamicTest.dynamicTest("질문 목록에 대한 접근 url, 내용 등 분석에 사용할 정보를 생성할 수 있다.", () -> {
                    //when
                    AnalysisCreateResponseDto responseDto = analysisService.createAnalysis(uniqueValue1);
                    //then
                    assertThat(responseDto).isNotNull();
                    assertThat(responseDto.getAnalysisId()).isExactlyInstanceOf(Long.class);
                    assertThat(responseDto.getQuestionTexts())
                            .hasSize(analysisRelatedSize)
                            .allMatch(allQuestionTexts::contains);
                    assertThat(responseDto.getAccessUrls()).hasSize(analysisRelatedSize);

                }),
                DynamicTest.dynamicTest("존재하지 않는 사용자 정보로는 분석 관련 정보에 접근할 수 없습니다.", () -> {
                    //given
                    UserEntity userEntity2 = createUser("notFoundSocialId", SocialType.GOOGLE);
                    String uniqueValue2 = userEntity2.getSocialId() + "_" + userEntity2.getSocialType();
                    //when & then
                    assertThatThrownBy(()->analysisService.createAnalysis(uniqueValue2))
                            .isInstanceOf(UserNotFoundException.class)
                            .hasMessage("해당 사용자는 존재하지 않습니다.");

                })
        );
    }

    @Test
    @DisplayName("분석 정보를 조회합니다.")
    void getAnalysis(){
        //given
        for(QuestionContent questionContent : QuestionContent.values()){ // initProvider 대체 -> TEST용
            VoiceEntity voiceEntity = VoiceEntity.initVoiceContentBuilder()
                    .originName(questionContent.getText() +"_voice.mp3")
                    .questionContent(questionContent)
                    .build();
            voiceEntity.changeAccessUrl("임의의 접근 url 주소");
            voiceRepository.save(voiceEntity);
        }

        AnswerEntity answerEntity1 = createAnswer("답변1보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity2 = createAnswer("답변2보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity3 = createAnswer("답변3보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity4 = createAnswer("답변4보이스를 STT로 변경한 텍스트");
        List<AnswerEntity> answerEntities = List.of(answerEntity1, answerEntity2, answerEntity3, answerEntity4);
        answerJpaRepository.saveAll(answerEntities);

        UserEntity userEntity = createUser("socialId", SocialType.APPLE);
        QuestionEntity questionEntity1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        List<QuestionEntity> questionEntities = List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4);
        AnalysisEntity analysisEntity = createAnalysis(userEntity, questionEntities);

        questionEntity1.connectAnswer(answerEntity1);
        questionEntity2.connectAnswer(answerEntity2);
        questionEntity3.connectAnswer(answerEntity3);
        questionEntity4.connectAnswer(answerEntity4);

        userRepository.save(userEntity);
        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);

        LocalDate analyzeTime = now();
        double feelingState = 72.1;
        analysisEntity.changeFeelingStateAndAnalyzeTime(feelingState, analyzeTime);

        int questionSize = 4;
        int answerSize = 4;
        //when
        AnalysisDetailResponse response = analysisService.getAnalysis(savedAnalysisEntity.getId());
        //then
        assertThat(response.getQuestionContentVoiceUrls()).hasSize(questionSize);
        assertThat(response.getQuestionIds()).hasSize(answerSize);
        assertThat(response)
                .extracting(
                        "analysisId",
                        "feelingState",
                        "questionContent",
                        "answerContent"
                )
                .containsExactly(
                        analysisEntity.getId(),
                        feelingState,
                        questionEntities.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList()),
                        answerEntities.stream()
                                .map(answer -> answer.getContent())
                                .collect(Collectors.toList())
                );
    }

    @Test
    @DisplayName("존재하지 않는 분석 정보를 조회하는 경우, 예외가 발생합니다.")
    void getAnalysis_AnalysisNotFoundException(){
        //given
        for(QuestionContent questionContent : QuestionContent.values()){ // initProvider 대체 -> TEST용
            VoiceEntity voiceEntity = VoiceEntity.initVoiceContentBuilder()
                    .originName(questionContent.getText() +"_voice.mp3")
                    .questionContent(questionContent)
                    .build();
            voiceEntity.changeAccessUrl("임의의 접근 url 주소");
            voiceRepository.save(voiceEntity);
        }
        AnswerEntity answerEntity1 = createAnswer("답변1보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity2 = createAnswer("답변2보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity3 = createAnswer("답변3보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity4 = createAnswer("답변4보이스를 STT로 변경한 텍스트");
        List<AnswerEntity> answerEntities = List.of(answerEntity1, answerEntity2, answerEntity3, answerEntity4);
        answerJpaRepository.saveAll(answerEntities);

        QuestionEntity questionEntity1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        List<QuestionEntity> questionEntities = List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4);

        String socialId = "socialId";
        SocialType socialType =SocialType.APPLE;
        UserEntity userEntity = createUser(socialId, socialType);
        UserEntity savedUserEntity = userRepository.save(userEntity);


        AnalysisEntity analysisEntity = createAnalysis(savedUserEntity, questionEntities);

        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);
        Long analysisId = savedAnalysisEntity.getId();
        Long anyLongValue = 10L;
        Long notFoundAnalysisId = analysisId + anyLongValue;

        //when & then
        assertThatThrownBy(() -> analysisService.getAnalysis(notFoundAnalysisId))
                .isInstanceOf(AnalysisNotFoundException.class)
                .hasMessage("해당 분석은 존재하지 않습니다.");

    }
    @Test
    @DisplayName("특정 사용자의 분석 정보 리스트를 페이징합니다.")
    void getAnalysisList(){
        //given
        String socialId = "socialId";
        SocialType socialType = SocialType.APPLE;
        UserEntity userEntity = createUser(socialId, socialType);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        QuestionEntity questionEntity1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(QuestionContent.FIXED_QUESTION3);
        QuestionEntity questionEntity4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        QuestionEntity questionEntity5 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        QuestionEntity questionEntity6 = createQuestion(QuestionContent.UNFIXED_QUESTION4);
        List<QuestionEntity> questions1 = List.of(questionEntity1, questionEntity2, questionEntity4, questionEntity5);
        List<QuestionEntity> questions2 = List.of(questionEntity2, questionEntity3, questionEntity5, questionEntity6);

        AnalysisEntity analysisEntity1 = createAnalysis(savedUserEntity, questions1);
        AnalysisEntity analysisEntity2 = createAnalysis(savedUserEntity, questions1);
        AnalysisEntity analysisEntity3 = createAnalysis(savedUserEntity, questions1);
        AnalysisEntity analysisEntity4 = createAnalysis(savedUserEntity, questions2);
        AnalysisEntity analysisEntity5 = createAnalysis(savedUserEntity, questions2);
        AnalysisEntity analysisEntity6 = createAnalysis(savedUserEntity, questions2);
        AnalysisEntity analysisEntity7 = createAnalysis(savedUserEntity, questions2);
        List<AnalysisEntity> analysies = List.of(analysisEntity1, analysisEntity2, analysisEntity3, analysisEntity4, analysisEntity5, analysisEntity6, analysisEntity7);
        analysisJpaRepository.saveAll(analysies);

        String uniqueValue = socialId + "_" + socialType.getDescription();
        int pageNumber = 0;

        //when
        AnalysisListResponseDto response = analysisService.getAnalysisList(uniqueValue, pageNumber);

        //then
        assertThat(response)
                .extracting("pageNumber", "totalPage")
                .containsExactly(1, 1);

        assertThat(response.getAnalysisResponseDtoList())
                .hasSize(analysies.size())
                .extracting("analysisId", "questionContent")
                .containsExactlyInAnyOrder(
                        tuple(analysisEntity1.getId(), questions1.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList())),
                        tuple(analysisEntity2.getId(), questions1.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList())),
                        tuple(analysisEntity3.getId(), questions1.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList())),
                        tuple(analysisEntity4.getId(), questions2.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList())),
                        tuple(analysisEntity5.getId(), questions2.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList())),
                        tuple(analysisEntity6.getId(), questions2.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList())),
                        tuple(analysisEntity7.getId(), questions2.stream()
                                .map(question -> question.getQuestionContent().getText())
                                .collect(Collectors.toList()))
                );
    }

    @Test
    @DisplayName("가장 최근 감정 분석 결과 날짜를 기준으로 과거 7일 동안 일자 별 평균 분석 점수 목록을 제공한다.")
    void getAnalysisListGroupByDay(){
        //given
        String socialId = "socialId";
        SocialType socialType = SocialType.APPLE;
        UserEntity userEntity = createUser(socialId, socialType);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        String uniqueValue = savedUserEntity.getSocialId() + "_" + savedUserEntity.getSocialType().getDescription();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        List<AnalysisEntity> arrayList = new ArrayList<>();
        LocalDate nowDate = now();
        double score1 = 10.0;
        for (int i = 0; i < 3; i++) { // 오늘 데이터 3개 추가
            AnalysisEntity analysisEntity = createAnalysis(userEntity);
            arrayList.add(analysisEntity);
            analysisEntity.changeFeelingStateAndAnalyzeTime(score1, nowDate);
        }
        LocalDate dateTwoDaysAgo = now().minusDays(2);
        double score2 = 30.0;
        for (int i = 0; i < 3; i++) { // 2일 전 데이터 4개 추가
            AnalysisEntity analysisEntity = createAnalysis(userEntity);
            arrayList.add(analysisEntity);
            analysisEntity.changeFeelingStateAndAnalyzeTime(score2, dateTwoDaysAgo);
        }
        LocalDate dateFourDaysAgo = now().minusDays(4);
        double score3 = 40.0;
        for (int i = 0; i < 4; i++) {        // 4일 전 데이터 4개 추가
            AnalysisEntity analysisEntity = createAnalysis(userEntity);
            arrayList.add(analysisEntity);
            analysisEntity.changeFeelingStateAndAnalyzeTime(score3, dateFourDaysAgo);
        }
        LocalDate dateFiveDaysAgo = now().minusDays(5);
        double score4 = 50.0;
        for (int i = 0; i < 3; i++) { // 5일 전 데이터 2개 추가
            AnalysisEntity analysisEntity = createAnalysis(userEntity);
            arrayList.add(analysisEntity);
            analysisEntity.changeFeelingStateAndAnalyzeTime(score4, dateFiveDaysAgo);
        }
        LocalDate dateSevenDaysAgo = now().minusDays(7);
        double score5 = 60.0;
        for (int i = 0; i < 2; i++) { // 7일 전 데이터 2개 추가
            AnalysisEntity analysisEntity = createAnalysis(userEntity);
            arrayList.add(analysisEntity);
            analysisEntity.changeFeelingStateAndAnalyzeTime(score5, dateSevenDaysAgo);
        }
        analysisJpaRepository.saveAll(arrayList);

        //when
        FeelingStateResponseListDto result = analysisService.getAnalysisListGroupByDay(uniqueValue);

        //then
        assertThat(result.getFeelingStateResponsesDto())
                .hasSize(4)
                .extracting("date", "avgFeelingState")
                .containsExactlyInAnyOrder(
                        tuple(nowDate.format(formatter), score1),
                        tuple(dateTwoDaysAgo.format(formatter), score2),
                        tuple(dateFourDaysAgo.format(formatter), score3),
                        tuple(dateFiveDaysAgo.format(formatter), score4)
                );
    }

    @TestFactory
    @DisplayName("여러 경우에 대한 감정 분석 시나리오 테스트")
    java.util.Collection<DynamicTest> analyzeEmotion(){
        //given
        String socialId = "socialId";
        SocialType socialType = SocialType.APPLE;
        UserEntity userEntity = createUser(socialId, socialType);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        String uniqueValue = savedUserEntity.getSocialId() + "_" + savedUserEntity.getSocialType().getDescription();

        QuestionEntity questionEntity1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(QuestionContent.UNFIXED_QUESTION2);
        AnalysisEntity analysisEntity = createAnalysis(savedUserEntity, List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4));
        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);

        //when & then
        return List.of(
                DynamicTest.dynamicTest("모든 질문에 대한 답변이 없는 분석 정보를 감정 분석할 수 없습니다.", () -> {
                    //when & then
                    assertThatThrownBy(() -> analysisService.analyzeEmotion(savedAnalysisEntity.getId(), uniqueValue))
                            .isInstanceOf(AllAnswersNotFoundException.class)
                            .hasMessage("질문에 해당하는 모든 답변이 존재하지 않습니다.");

                }),
                DynamicTest.dynamicTest("각 질문에 대한 모든 답변이 이뤄진 분석 정보는 감정 분석으로 감정 상태 결과값을 만든다.", () -> {
                    // given
                    AnswerEntity answerEntity1 = createAnswer("질문1에 대한 분석 완료");
                    AnswerEntity answerEntity2 = createAnswer("질문2에 대한 분석 완료");
                    AnswerEntity answerEntity3 = createAnswer("질문3에 대한 분석 완료");
                    AnswerEntity answerEntity4 = createAnswer("질문4에 대한 분석 완료");
                    answerEntity1.connectAnalysis(analysisEntity);
                    answerEntity2.connectAnalysis(analysisEntity);
                    answerEntity3.connectAnalysis(analysisEntity);
                    answerEntity4.connectAnalysis(analysisEntity);

                    List<FellingStateCreateResponse> responseListByText = new ArrayList<>();
                    List<FellingStateCreateResponse> responseListByVoice = new ArrayList<>();
                    for(int index=0; index<4; index++){
                        double randomValue1 = 3, randomValue2 = 5;
                        responseListByText.add(createFellingStatus(randomValue1, index));
                        responseListByVoice.add(createFellingStatus(randomValue2, index));
                    }
                    when(webClientUtil.callAnalyzeEmotion(any(List.class)))
                            .thenReturn(responseListByText);
                    when(webClientUtil.callAnalyzeEmotionVoice(any(List.class)))
                            .thenReturn(responseListByVoice);

                    double analysisTextRate = 0.35;
                    double analysisVoiceRate = 0.65;
                    double resultStatus = analysisTextRate * averageFellingStatusBy(responseListByText)
                            + analysisVoiceRate * averageFellingStatusBy(responseListByVoice);
                    //when
                    FellingStateCreateResponse result = analysisService.analyzeEmotion(savedAnalysisEntity.getId(), uniqueValue);
                    //then
                    assertThat(result)
                            .extracting("transcribedText", "feelingState")
                            .containsExactly(null, resultStatus);

                    assertThat(savedAnalysisEntity)
                            .extracting("feelingState")
                            .isEqualTo(resultStatus);

                }),
                DynamicTest.dynamicTest("이미 감정 분석된 분석 정보는 더 이상 감정 분석할 수 없습니다." ,() -> {
                    // when & then
                    assertThatThrownBy(() -> analysisService.analyzeEmotion(savedAnalysisEntity.getId(), uniqueValue))
                            .isInstanceOf(AlreadyAnalyzedException.class)
                            .hasMessage("이미 분석했습니다.");
                })
        );


    }

    @Test
    @DisplayName("존재하는 분석을 삭제합니다.")
    void removeAnalysis(){
        //given
        for(QuestionContent questionContent : QuestionContent.values()){ // initProvider 대체 -> TEST용
            VoiceEntity voiceEntity = VoiceEntity.initVoiceContentBuilder()
                    .originName(questionContent.getText() +"_voice.mp3")
                    .questionContent(questionContent)
                    .build();
            voiceEntity.changeAccessUrl("임의의 접근 url 주소");
            voiceRepository.save(voiceEntity);
        }

        AnswerEntity answerEntity1 = createAnswer("답변1보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity2 = createAnswer("답변2보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity3 = createAnswer("답변3보이스를 STT로 변경한 텍스트");
        AnswerEntity answerEntity4 = createAnswer("답변4보이스를 STT로 변경한 텍스트");
        List<AnswerEntity> answerEntities = List.of(answerEntity1, answerEntity2, answerEntity3, answerEntity4);
        answerJpaRepository.saveAll(answerEntities);

        UserEntity userEntity = createUser("socialId", SocialType.APPLE);
        QuestionEntity questionEntity1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        List<QuestionEntity> questionEntities = List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4);
        AnalysisEntity analysisEntity = createAnalysis(userEntity, questionEntities);

        questionEntity1.connectAnswer(answerEntity1);
        questionEntity2.connectAnswer(answerEntity2);
        questionEntity3.connectAnswer(answerEntity3);
        questionEntity4.connectAnswer(answerEntity4);

        userRepository.save(userEntity);
        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);
        //when
        analysisService.removeAnalysis(savedAnalysisEntity.getId());
        //then
        boolean exists = analysisJpaRepository.existsById(savedAnalysisEntity.getId());
        assertThat(exists).isFalse(); // 삭제되었음을 검증
    }

    private static double averageFellingStatusBy(List<FellingStateCreateResponse> responseListByText) {
        return responseListByText.stream()
                .mapToDouble(value -> value.getFeelingState())
                .average().getAsDouble();
    }

    private static FellingStateCreateResponse createFellingStatus(double randomValue, int index) {
        return FellingStateCreateResponse.builder()
                .feelingState(30.0 + randomValue)
                .transcribedText("음성 답변을 텍스트로 변환한 답변 텍스트" + index)
                .build();
    }

    private static VoiceEntity createVoice(String fileName) {
        return VoiceEntity.commonBuilder()
                .originName(fileName)
                .build();
    }


    private static AnswerEntity createAnswer(String content) {
        return AnswerEntity.builder()
                .content(content)
                .build();
    }

    private static QuestionEntity createQuestion(QuestionContent questionContent) {
        return QuestionEntity.of(questionContent);
    }

    private static AnalysisEntity createAnalysis(UserEntity userEntity, List<QuestionEntity> questionEntities) {
        return AnalysisEntity.builder()
                .userEntity(userEntity)
                .questionEntities(questionEntities)
                .build();
    }

    private static AnalysisEntity createAnalysis(UserEntity userEntity) {
        return AnalysisEntity.builder()
                .userEntity(userEntity)
                .build();
    }

    private static UserEntity createUser(String socialId, SocialType socialType) {
        return UserEntity.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }


}