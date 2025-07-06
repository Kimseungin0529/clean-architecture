package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.adapter.out.persistence.repository.AnswerJpaRepository;
import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.question.adapter.out.persistence.QuestionJpaRepository;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.user.adapter.out.persistence.UserJpaRepository;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.domain.voice.application.port.out.VoiceJpaRepository;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.project.doongdoong.domain.question.domain.QuestionContent.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class AnalysisEntityJpaRepositoryTest extends IntegrationSupportTest {

    @Autowired
    AnalysisJpaRepository analysisRepository;
    @Autowired
    QuestionJpaRepository questionRepository;
    @Autowired
    UserJpaRepository userRepository;
    @Autowired
    AnswerJpaRepository answerJpaRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    VoiceJpaRepository voiceRepository;

    @Test
    @DisplayName("접근 회원과 고유 분석 번호를 통해 일치하는 분석 정보를 조회합니다.")
    void findByUserAndId() {
        //given
        UserEntity userEntity = createUser("socialId1", SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        AnalysisEntity analysisEntity = createAnalysis(userEntity);
        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);

        Long requestId = savedAnalysisEntity.getId();

        //when
        Optional<AnalysisEntity> findAnalysis = analysisRepository.findByUserAndId(savedUserEntity, requestId);

        //then
        assertThat(findAnalysis.get())
                .isNotNull()
                .isEqualTo(savedAnalysisEntity);
        assertThat(findAnalysis.get().getUser())
                .isNotNull()
                .isEqualTo(savedUserEntity);

    }

    @Test
    @DisplayName("사용자의 분석 결과를 최신순으로 페이징 조회합니다.")
    void findAllByUserOrderByCreatedTime() {
        //given
        UserEntity userEntity = createUser("socialId", SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        int pageNumber = 1;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        AnalysisEntity analysisEntity1 = createAnalysis(userEntity);
        AnalysisEntity analysisEntity2 = createAnalysis(userEntity);
        AnalysisEntity analysisEntity3 = createAnalysis(userEntity);
        AnalysisEntity analysisEntity4 = createAnalysis(userEntity);
        AnalysisEntity analysisEntity5 = createAnalysis(userEntity);
        AnalysisEntity analysisEntity6 = createAnalysis(userEntity);
        AnalysisEntity analysisEntity7 = createAnalysis(userEntity);
        analysisEntity1.changeFeelingStateAndAnalyzeTime(10, null);
        analysisEntity2.changeFeelingStateAndAnalyzeTime(20, null);
        analysisEntity3.changeFeelingStateAndAnalyzeTime(30, null);
        analysisEntity4.changeFeelingStateAndAnalyzeTime(40, null);
        analysisEntity5.changeFeelingStateAndAnalyzeTime(50, null);
        analysisEntity6.changeFeelingStateAndAnalyzeTime(60, null);
        analysisEntity7.changeFeelingStateAndAnalyzeTime(70, null);

        analysisRepository.saveAll(List.of(analysisEntity1, analysisEntity2, analysisEntity3, analysisEntity4, analysisEntity5, analysisEntity6, analysisEntity7));

        //when
        Page<AnalysisEntity> result = analysisRepository.findAllByUserOrderByCreatedTime(savedUserEntity, pageRequest);

        //then
        assertThat(result.hasNext()).isFalse();
        assertThat(result.getTotalElements()).isEqualTo(7);
        assertThat(result.getContent())
                .hasSize(2)
                .extracting("user.socialId", "feelingState")
                .containsExactly(
                        tuple("socialId", 60.0),
                        tuple("socialId", 70.0)
                );


    }

    @DisplayName("특정 시간 내에 있는 사용자의 감정 분석 시간과 감정 수치를 조회합니다.")
    @ParameterizedTest
    @CsvSource({"2024-03-05, 2024-03-15", "2024-03-02, 2024-03-30", "2024-03-03, 2024-03-27"})
    void findAllByDateBetween(LocalDate startTime, LocalDate endTime) {
        //given
        UserEntity userEntity = createUser("socialId", SocialType.APPLE);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        AnalysisEntity analysisEntity1 = createAnalysis(savedUserEntity);
        AnalysisEntity analysisEntity2 = createAnalysis(savedUserEntity);
        AnalysisEntity analysisEntity3 = createAnalysis(savedUserEntity);
        AnalysisEntity analysisEntity4 = createAnalysis(savedUserEntity);
        AnalysisEntity analysisEntity5 = createAnalysis(savedUserEntity);

        analysisEntity1.changeFeelingStateAndAnalyzeTime(72.5, LocalDate.of(2024, 3, 1));
        analysisEntity2.changeFeelingStateAndAnalyzeTime(75, LocalDate.of(2024, 3, 5));
        analysisEntity3.changeFeelingStateAndAnalyzeTime(77.5, LocalDate.of(2024, 3, 15));
        analysisEntity4.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 3, 15));
        analysisEntity5.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 3, 31));

        analysisRepository.saveAll(List.of(analysisEntity1, analysisEntity2, analysisEntity3, analysisEntity4, analysisEntity5));

        //when
        List<FeelingStateResponseDto> result = analysisRepository.findAllByDateBetween(savedUserEntity, startTime, endTime);
        //then
        assertThat(result).hasSize(2)
                .extracting("date", "avgFeelingState")
                .containsExactly(
                        tuple("2024-3-5", Double.valueOf(75)),
                        tuple("2024-3-15", Double.valueOf(78.75))
                );

    }

    @Test
    @DisplayName("사용자의 가장 최근 분석 조회하기")
    void findFirstByUserOrderByAnalyzeTimeDesc() {
        //given
        UserEntity userEntity = createUser("socialId", SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        AnalysisEntity analysisEntity1 = createAnalysis(savedUserEntity);
        AnalysisEntity analysisEntity2 = createAnalysis(savedUserEntity);
        AnalysisEntity analysisEntity3 = createAnalysis(savedUserEntity);
        AnalysisEntity analysisEntity4 = createAnalysis(savedUserEntity);

        analysisEntity1.changeFeelingStateAndAnalyzeTime(70, LocalDate.of(2023, 12, 5));
        analysisEntity2.changeFeelingStateAndAnalyzeTime(75, LocalDate.of(2024, 3, 5));
        analysisEntity3.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 6, 27));
        analysisEntity4.changeFeelingStateAndAnalyzeTime(90, LocalDate.of(2024, 11, 19));

        analysisRepository.saveAll(List.of(analysisEntity1, analysisEntity2, analysisEntity3, analysisEntity4));

        //when
        Optional<AnalysisEntity> result = analysisRepository.findFirstByUserOrderByAnalyzeTimeDesc(savedUserEntity);

        //then
        assertThat(result.get()).isNotNull()
                .isEqualTo(analysisEntity4)
                .extracting("user", "feelingState", "analyzeTime")
                .contains(savedUserEntity, analysisEntity4.getFeelingState(), analysisEntity4.getAnalyzeTime());
    }

    @Test
    @DisplayName("분석 정보와 분석에 사용된 질문들을 조회합니다.")
    void findAnalysisWithCounselByWithQuestion() {
        //given
        UserEntity userEntity = createUser("socialId", SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        QuestionEntity questionEntity1 = createQuestion(FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(UNFIXED_QUESTION1);

        List<QuestionEntity> questionEntities = List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4);

        AnalysisEntity analysisEntity = createAnalysis(savedUserEntity, questionEntities);
        questionEntity1.connectAnalysis(analysisEntity);
        questionEntity2.connectAnalysis(analysisEntity);
        questionEntity3.connectAnalysis(analysisEntity);
        questionEntity4.connectAnalysis(analysisEntity);

        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);

        //when
        Optional<AnalysisEntity> result = analysisRepository.findAnalysisWithQuestion(savedAnalysisEntity.getId());
        //then
        assertThat(result.get()).isNotNull()
                .isEqualTo(savedAnalysisEntity)
                .extracting("id", "user")
                .contains(savedAnalysisEntity.getId(), savedUserEntity);

        assertThat(result.get().getQuestions())
                .hasSize(questionEntities.size())
                .containsExactlyInAnyOrder(questionEntity1, questionEntity2, questionEntity3, questionEntity4);
        ;
    }


    /**
     * QueryDsl Test
     */

    @Test
    @DisplayName("음성 답변과 답변 정보가 담긴 분석 정보를 조회한다.")
    void searchAnalysisWithVoiceOfAnswer() {
        //given
        String socialId = "socialId";
        SocialType socialType = SocialType.APPLE;
        UserEntity userEntity = createUser(socialId, socialType);

        AnalysisEntity analysisEntity = createAnalysis(userEntity);
        userRepository.save(userEntity);

        VoiceEntity voiceEntity1 = createVoice("파일이름1", FIXED_QUESTION1);
        VoiceEntity voiceEntity2 = createVoice("파일이름2", FIXED_QUESTION2);
        VoiceEntity voiceEntity3 = createVoice("파일이름3", UNFIXED_QUESTION1);
        AnswerEntity answerEntity1 = createAnswer(voiceEntity1, "질문에 대한 답변 텍스트1");
        AnswerEntity answerEntity2 = createAnswer(voiceEntity2, "질문에 대한 답변 텍스트2");
        AnswerEntity answerEntity3 = createAnswer(voiceEntity3, "질문에 대한 답변 텍스트3");
        answerEntity1.connectAnalysis(analysisEntity);
        answerEntity2.connectAnalysis(analysisEntity);
        answerEntity3.connectAnalysis(analysisEntity);

        voiceRepository.saveAll(List.of(voiceEntity1, voiceEntity2, voiceEntity3));
        answerJpaRepository.saveAll(List.of(answerEntity1, answerEntity2, answerEntity3));
        analysisRepository.save(analysisEntity);

        //when
        Optional<AnalysisEntity> result = analysisRepository.searchFullAnalysisBy(analysisEntity.getId());

        //then
        AnalysisEntity findAnalysisEntity = result.get();
        assertThat(findAnalysisEntity).isNotNull();
        List<AnswerEntity> answerEntities = findAnalysisEntity.getAnswers();
        assertThat(answerEntities).hasSize(3);
        assertThat(answerEntities)
                .extracting("content")
                .containsExactlyInAnyOrder(
                        "질문에 대한 답변 텍스트1",
                        "질문에 대한 답변 텍스트2",
                        "질문에 대한 답변 텍스트3"
                );
        assertThat(answerEntities)
                .extracting("voice.originName")
                .containsExactlyInAnyOrder(
                        "파일이름1",
                        "파일이름2",
                        "파일이름3"
                );
    }

    @DisplayName("상담, 질문, 답변, 음성과 같이 분석에 대한 모든 정보를 조회한다.")
    @Test
    void searchFullAnalysisBy() {
        //given
        String socialId = "socialId";
        SocialType socialType = SocialType.APPLE;
        UserEntity userEntity = createUser(socialId, socialType);
        QuestionEntity questionEntity1 = createQuestion(FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(UNFIXED_QUESTION3);

        AnalysisEntity analysisEntity = createAnalysis(userEntity, List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4));
        questionEntity1.connectAnalysis(analysisEntity);
        questionEntity2.connectAnalysis(analysisEntity);
        questionEntity3.connectAnalysis(analysisEntity);
        questionEntity4.connectAnalysis(analysisEntity);
        assertThat(analysisEntity.getQuestions()).hasSize(4);

        userRepository.save(userEntity);

        VoiceEntity voiceEntity1 = createVoice("파일이름1", FIXED_QUESTION1);
        VoiceEntity voiceEntity2 = createVoice("파일이름2", FIXED_QUESTION2);
        VoiceEntity voiceEntity3 = createVoice("파일이름3", UNFIXED_QUESTION1);
        VoiceEntity voiceEntity4 = createVoice("파일이름4", UNFIXED_QUESTION3);
        voiceRepository.saveAll(List.of(voiceEntity1, voiceEntity2, voiceEntity3, voiceEntity4));

        AnswerEntity answerEntity1 = createAnswer(voiceEntity1, "질문에 대한 답변 텍스트1");
        AnswerEntity answerEntity2 = createAnswer(voiceEntity2, "질문에 대한 답변 텍스트2");
        AnswerEntity answerEntity3 = createAnswer(voiceEntity3, "질문에 대한 답변 텍스트3");
        AnswerEntity answerEntity4 = createAnswer(voiceEntity4, "질문에 대한 답변 텍스트4");
        answerEntity1.connectAnalysis(analysisEntity);
        answerEntity2.connectAnalysis(analysisEntity);
        answerEntity3.connectAnalysis(analysisEntity);
        answerEntity4.connectAnalysis(analysisEntity);

        questionEntity1.connectAnswer(answerEntity1);
        questionEntity2.connectAnswer(answerEntity2);
        questionEntity3.connectAnswer(answerEntity3);
        questionEntity4.connectAnswer(answerEntity4);

        answerJpaRepository.saveAll(List.of(answerEntity1, answerEntity2, answerEntity3, answerEntity4));
        analysisRepository.save(analysisEntity);

        // when
        Optional<AnalysisEntity> result = analysisRepository.searchAnalysisWithVoiceOfAnswer(analysisEntity.getId());

        // then
        assertThat(result).isPresent();
        AnalysisEntity findAnalysisEntity = result.get();

        List<AnswerEntity> answerEntities = findAnalysisEntity.getAnswers();
        assertThat(answerEntities).hasSize(4);
        assertThat(answerEntities)
                .extracting("content", "voice.originName")
                .containsExactlyInAnyOrder(
                        tuple("질문에 대한 답변 텍스트1", "파일이름1"),
                        tuple("질문에 대한 답변 텍스트2", "파일이름2"),
                        tuple("질문에 대한 답변 텍스트3", "파일이름3"),
                        tuple("질문에 대한 답변 텍스트4", "파일이름4")
                );

        List<QuestionEntity> questionEntities = findAnalysisEntity.getQuestions();
        assertThat(questionEntities).hasSize(4);

        assertThat(questionEntities)
                .extracting("questionContent")
                .containsExactlyInAnyOrder(FIXED_QUESTION1, FIXED_QUESTION2, UNFIXED_QUESTION1, UNFIXED_QUESTION3);

        CounselEntity counselEntity = findAnalysisEntity.getCounsel();
        assertThat(counselEntity).isNull();

    }


    private static VoiceEntity createVoice(String fileName, QuestionContent questionContent) {
        return VoiceEntity.initVoiceContentBuilder()
                .originName(fileName)
                .questionContent(questionContent)
                .build();
    }

    private static AnswerEntity createAnswer(VoiceEntity voiceEntity, String content) {
        return AnswerEntity.builder()
                .voice(voiceEntity)
                .content(content)
                .build();
    }

    private static QuestionEntity createQuestion(QuestionContent questionContent) {

        return QuestionEntity.of(questionContent);
    }

    private static AnalysisEntity createAnalysis(UserEntity userEntity) {
        return AnalysisEntity.builder()
                .userEntity(userEntity)
                .build();
    }

    private static AnalysisEntity createAnalysis(UserEntity userEntity, List<QuestionEntity> questionEntities) {
        return AnalysisEntity.builder()
                .userEntity(userEntity)
                .questionEntities(questionEntities)
                .build();
    }

    private static UserEntity createUser(String socialId, SocialType socialType) {
        return UserEntity.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }

}