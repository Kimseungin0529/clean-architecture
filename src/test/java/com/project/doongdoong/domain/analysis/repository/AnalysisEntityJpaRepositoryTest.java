package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.module.IntegrationSupportTest;
import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.answer.application.port.out.AnswerJpaRepository;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.question.repository.QuestionRepository;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
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

import static com.project.doongdoong.domain.question.model.QuestionContent.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class AnalysisEntityJpaRepositoryTest extends IntegrationSupportTest {

    @Autowired
    AnalysisJpaRepository analysisJpaRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired UserRepository userRepository;
    @Autowired
    AnswerJpaRepository answerJpaRepository;
    @Autowired VoiceRepository voiceRepository;

    @Test
    @DisplayName("접근 회원과 고유 분석 번호를 통해 일치하는 분석 정보를 조회합니다.")
    void findByUserAndId(){
        //given
        User user = createUser("socialId1", SocialType.APPLE);
        User savedUser = userRepository.save(user);

        AnalysisEntity analysisEntity = createAnalysis(user);
        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);

        Long requestId = savedAnalysisEntity.getId();

        //when
        Optional<AnalysisEntity> findAnalysis = analysisJpaRepository.findByUserAndId(savedUser, requestId);

        //then
        assertThat(findAnalysis.get())
                .isNotNull()
                .isEqualTo(savedAnalysisEntity);
        assertThat(findAnalysis.get().getUser())
                .isNotNull()
                .isEqualTo(savedUser);

    }

    @Test
    @DisplayName("사용자의 분석 결과를 최신순으로 페이징 조회합니다.")
    void findAllByUserOrderByCreatedTime(){
        //given
        User user = createUser("socialId", SocialType.APPLE);
        User savedUser = userRepository.save(user);
        int pageNumber = 1;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        AnalysisEntity analysisEntity1 = createAnalysis(user);
        AnalysisEntity analysisEntity2 = createAnalysis(user);
        AnalysisEntity analysisEntity3 = createAnalysis(user);
        AnalysisEntity analysisEntity4 = createAnalysis(user);
        AnalysisEntity analysisEntity5 = createAnalysis(user);
        AnalysisEntity analysisEntity6 = createAnalysis(user);
        AnalysisEntity analysisEntity7 = createAnalysis(user);
        analysisEntity1.changeFeelingStateAndAnalyzeTime(10, null);
        analysisEntity2.changeFeelingStateAndAnalyzeTime(20, null);
        analysisEntity3.changeFeelingStateAndAnalyzeTime(30, null);
        analysisEntity4.changeFeelingStateAndAnalyzeTime(40, null);
        analysisEntity5.changeFeelingStateAndAnalyzeTime(50, null);
        analysisEntity6.changeFeelingStateAndAnalyzeTime(60, null);
        analysisEntity7.changeFeelingStateAndAnalyzeTime(70, null);

        analysisJpaRepository.saveAll(List.of(analysisEntity1, analysisEntity2, analysisEntity3, analysisEntity4, analysisEntity5, analysisEntity6, analysisEntity7));

        //when
        Page<AnalysisEntity> result = analysisJpaRepository.findAllByUserOrderByCreatedTime(savedUser, pageRequest);

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
    @CsvSource({"2024-03-05, 2024-03-15", "2024-03-02, 2024-03-30","2024-03-03, 2024-03-27" })
    void findAllByDateBetween(LocalDate startTime, LocalDate endTime){
        //given
        User user = createUser("socialId", SocialType.APPLE);

        User savedUser = userRepository.save(user);

        AnalysisEntity analysisEntity1 = createAnalysis(savedUser);
        AnalysisEntity analysisEntity2 = createAnalysis(savedUser);
        AnalysisEntity analysisEntity3 = createAnalysis(savedUser);
        AnalysisEntity analysisEntity4 = createAnalysis(savedUser);
        AnalysisEntity analysisEntity5 = createAnalysis(savedUser);

        analysisEntity1.changeFeelingStateAndAnalyzeTime(72.5, LocalDate.of(2024,3,1));
        analysisEntity2.changeFeelingStateAndAnalyzeTime(75, LocalDate.of(2024,3,5));
        analysisEntity3.changeFeelingStateAndAnalyzeTime(77.5, LocalDate.of(2024,3, 15));
        analysisEntity4.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 3, 15));
        analysisEntity5.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 3, 31));

        analysisJpaRepository.saveAll(List.of(analysisEntity1, analysisEntity2, analysisEntity3, analysisEntity4, analysisEntity5));

        //when
        List<FeelingStateResponseDto> result = analysisJpaRepository.findAllByDateBetween(savedUser, startTime, endTime);
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
    void findFirstByUserOrderByAnalyzeTimeDesc(){
        //given
        User user = createUser("socialId", SocialType.APPLE);
        User savedUser = userRepository.save(user);

        AnalysisEntity analysisEntity1 = createAnalysis(savedUser);
        AnalysisEntity analysisEntity2 = createAnalysis(savedUser);
        AnalysisEntity analysisEntity3 = createAnalysis(savedUser);
        AnalysisEntity analysisEntity4 = createAnalysis(savedUser);

        analysisEntity1.changeFeelingStateAndAnalyzeTime(70, LocalDate.of(2023, 12, 5));
        analysisEntity2.changeFeelingStateAndAnalyzeTime(75, LocalDate.of(2024, 3, 5));
        analysisEntity3.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 6, 27));
        analysisEntity4.changeFeelingStateAndAnalyzeTime(90, LocalDate.of(2024, 11, 19));

        analysisJpaRepository.saveAll(List.of(analysisEntity1, analysisEntity2, analysisEntity3, analysisEntity4));

        //when
        Optional<AnalysisEntity> result = analysisJpaRepository.findFirstByUserOrderByAnalyzeTimeDesc(savedUser);

        //then
        assertThat(result.get()).isNotNull()
                .isEqualTo(analysisEntity4)
                .extracting("user", "feelingState", "analyzeTime")
                .contains(savedUser, analysisEntity4.getFeelingState(), analysisEntity4.getAnalyzeTime());
    }

    @Test
    @DisplayName("분석 정보와 분석에 사용된 질문들을 조회합니다.")
    void findAnalysisWithCounselByWithQuestion(){
        //given
        User user = createUser("socialId", SocialType.APPLE);
        User savedUser = userRepository.save(user);

        Question question1 = createQuestion(FIXED_QUESTION1);
        Question question2 = createQuestion(FIXED_QUESTION2);
        Question question3 = createQuestion(UNFIXED_QUESTION1);
        Question question4 = createQuestion(UNFIXED_QUESTION1);

        List<Question> questions = List.of(question1, question2, question3, question4);

        AnalysisEntity analysisEntity = createAnalysis(savedUser,questions);
        question1.connectAnalysis(analysisEntity);
        question2.connectAnalysis(analysisEntity);
        question3.connectAnalysis(analysisEntity);
        question4.connectAnalysis(analysisEntity);

        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);

        //when
        Optional<AnalysisEntity> result = analysisJpaRepository.findAnalysisWithQuestion(savedAnalysisEntity.getId());
        //then
        assertThat(result.get()).isNotNull()
                .isEqualTo(savedAnalysisEntity)
                .extracting("id", "user")
                .contains(savedAnalysisEntity.getId(), savedUser);

        assertThat(result.get().getQuestions())
                .hasSize(questions.size())
                .containsExactlyInAnyOrder(question1, question2, question3, question4);
        ;
    }


    /**
     * QueryDsl Test
     */

    @Test
    @DisplayName("음성 답변과 답변 정보가 담긴 분석 정보를 조회한다.")
    void searchAnalysisWithVoiceOfAnswer(){
        //given
        String socialId = "socialId";
        SocialType socialType = SocialType.APPLE;
        User user = createUser(socialId, socialType);

        AnalysisEntity analysisEntity = createAnalysis(user);
        userRepository.save(user);

        Voice voice1 = createVoice("파일이름1", FIXED_QUESTION1);
        Voice voice2 = createVoice("파일이름2", FIXED_QUESTION2);
        Voice voice3 = createVoice("파일이름3", UNFIXED_QUESTION1);
        AnswerEntity answerEntity1 = createAnswer(voice1, "질문에 대한 답변 텍스트1");
        AnswerEntity answerEntity2 = createAnswer(voice2, "질문에 대한 답변 텍스트2");
        AnswerEntity answerEntity3 = createAnswer(voice3, "질문에 대한 답변 텍스트3");
        answerEntity1.connectAnalysis(analysisEntity);
        answerEntity2.connectAnalysis(analysisEntity);
        answerEntity3.connectAnalysis(analysisEntity);

        voiceRepository.saveAll(List.of(voice1,voice2,voice3));
        answerJpaRepository.saveAll(List.of(answerEntity1, answerEntity2, answerEntity3));
        analysisJpaRepository.save(analysisEntity);

        //when
        Optional<AnalysisEntity> result = analysisJpaRepository.searchFullAnalysisBy(analysisEntity.getId());

        //then
        AnalysisEntity findAnalysisEntity = result.get();
        assertThat(findAnalysisEntity).isNotNull();
        List<AnswerEntity> answerEntities = findAnalysisEntity.getAnswerEntities();
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
        User user = createUser(socialId, socialType);
        Question question1 = createQuestion(FIXED_QUESTION1);
        Question question2 = createQuestion(FIXED_QUESTION2);
        Question question3 = createQuestion(UNFIXED_QUESTION1);
        Question question4 = createQuestion(UNFIXED_QUESTION3);

        AnalysisEntity analysisEntity = createAnalysis(user, List.of(question1, question2, question3, question4));
        question1.connectAnalysis(analysisEntity);
        question2.connectAnalysis(analysisEntity);
        question3.connectAnalysis(analysisEntity);
        question4.connectAnalysis(analysisEntity);
        assertThat(analysisEntity.getQuestions()).hasSize(4);

        userRepository.save(user);

        Voice voice1 = createVoice("파일이름1", FIXED_QUESTION1);
        Voice voice2 = createVoice("파일이름2", FIXED_QUESTION2);
        Voice voice3 = createVoice("파일이름3", UNFIXED_QUESTION1);
        Voice voice4 = createVoice("파일이름4", UNFIXED_QUESTION3);
        voiceRepository.saveAll(List.of(voice1, voice2, voice3, voice4));

        AnswerEntity answerEntity1 = createAnswer(voice1, "질문에 대한 답변 텍스트1");
        AnswerEntity answerEntity2 = createAnswer(voice2, "질문에 대한 답변 텍스트2");
        AnswerEntity answerEntity3 = createAnswer(voice3, "질문에 대한 답변 텍스트3");
        AnswerEntity answerEntity4 = createAnswer(voice4, "질문에 대한 답변 텍스트4");
        answerEntity1.connectAnalysis(analysisEntity);
        answerEntity2.connectAnalysis(analysisEntity);
        answerEntity3.connectAnalysis(analysisEntity);
        answerEntity4.connectAnalysis(analysisEntity);

        question1.connectAnswer(answerEntity1);
        question2.connectAnswer(answerEntity2);
        question3.connectAnswer(answerEntity3);
        question4.connectAnswer(answerEntity4);

        answerJpaRepository.saveAll(List.of(answerEntity1, answerEntity2, answerEntity3, answerEntity4));
        analysisJpaRepository.save(analysisEntity);

        // when
        Optional<AnalysisEntity> result = analysisJpaRepository.searchAnalysisWithVoiceOfAnswer(analysisEntity.getId());

        // then
        assertThat(result).isPresent();
        AnalysisEntity findAnalysisEntity = result.get();

        List<AnswerEntity> answerEntities = findAnalysisEntity.getAnswerEntities();
        assertThat(answerEntities).hasSize(4);
        assertThat(answerEntities)
                .extracting("content", "voice.originName")
                .containsExactlyInAnyOrder(
                        tuple("질문에 대한 답변 텍스트1", "파일이름1"),
                        tuple("질문에 대한 답변 텍스트2", "파일이름2"),
                        tuple("질문에 대한 답변 텍스트3", "파일이름3"),
                        tuple("질문에 대한 답변 텍스트4", "파일이름4")
                );

        List<Question> questions = findAnalysisEntity.getQuestions();
        assertThat(questions).hasSize(4);

        assertThat(questions)
                .extracting("questionContent")
                .containsExactlyInAnyOrder(FIXED_QUESTION1, FIXED_QUESTION2, UNFIXED_QUESTION1, UNFIXED_QUESTION3);

        Counsel counsel = findAnalysisEntity.getCounsel();
        assertThat(counsel).isNull();

    }



    private static Voice createVoice(String fileName, QuestionContent questionContent) {
        return Voice.initVoiceContentBuilder()
                .originName(fileName)
                .questionContent(questionContent)
                .build();
    }

    private static AnswerEntity createAnswer(Voice voice, String content) {
        return AnswerEntity.builder()
                .voice(voice)
                .content(content)
                .build();
    }

    private static Question createQuestion(QuestionContent questionContent) {

        return Question.of(questionContent);
    }

    private static AnalysisEntity createAnalysis(User user) {
        return AnalysisEntity.builder()
                .user(user)
                .build();
    }
    private static AnalysisEntity createAnalysis(User user, List<Question> questions) {
        return AnalysisEntity.builder()
                .user(user)
                .questions(questions)
                .build();
    }

    private static User createUser(String socialId, SocialType socialType) {
        return User.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }

}