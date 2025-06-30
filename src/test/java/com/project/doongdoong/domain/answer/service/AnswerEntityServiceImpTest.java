package com.project.doongdoong.domain.answer.service;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.answer.adapter.in.dto.AnswerCreateResponseDto;
import com.project.doongdoong.domain.answer.application.port.in.AnswerService;
import com.project.doongdoong.domain.answer.exception.AnswerConflictException;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswerEntityServiceImpTest extends IntegrationSupportTest {

    @Autowired
    AnswerService answerService;
    @Autowired
    AnalysisJpaRepository analysisJpaRepository;
    @Autowired
    UserRepository userRepository;


    File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        // 임시 파일 생성
        tempFile = File.createTempFile("test-audio", ".mp3");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("dummy audio content".getBytes()); // 더미 데이터 작성
        }
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 임시 파일 삭제
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @DisplayName("분석 설문지의 질문에 대해 음성으로 답변한다.")
    @Test
    void createAnswer() throws IOException {
        // given
        User user = createUser();
        userRepository.save(user);

        Question question1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        Question question2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        Question question3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        Question question4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        List<Question> questions = List.of(question1, question2, question3, question4);

        AnalysisEntity analysisEntity = createAnalysis(user, questions);
        question1.connectAnalysis(analysisEntity);
        question2.connectAnalysis(analysisEntity);
        question3.connectAnalysis(analysisEntity);
        question4.connectAnalysis(analysisEntity);
        AnswerEntity answerEntity = AnswerEntity.builder()
                .build();

        answerEntity.connectAnalysis(analysisEntity);
        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);
        Long questionId = question2.getId();

        MultipartFile multipartFile = new MockMultipartFile("file",
                tempFile.getName(),
                Files.probeContentType(tempFile.toPath()),
                Files.readAllBytes(tempFile.toPath())
        );

        // when
        AnswerCreateResponseDto result = answerService.createAnswer(savedAnalysisEntity.getId(), multipartFile, questionId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAnswerId()).isInstanceOf(Long.class);
    }

    @DisplayName("이미 질문에 대한 답변이 있다면 답변할 수 없다.")
    @Test
    void createAnswerException() throws IOException {
        // given
        User user = createUser();
        userRepository.save(user);

        Question question1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        Question question2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        Question question3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        Question question4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        List<Question> questions = List.of(question1, question2, question3, question4);

        AnalysisEntity analysisEntity = createAnalysis(user, questions);
        question1.connectAnalysis(analysisEntity);
        question2.connectAnalysis(analysisEntity);
        question3.connectAnalysis(analysisEntity);
        question4.connectAnalysis(analysisEntity);
        AnswerEntity answerEntity = AnswerEntity.builder()
                .build();

        answerEntity.connectAnalysis(analysisEntity);
        question2.connectAnswer(answerEntity);

        AnalysisEntity savedAnalysisEntity = analysisJpaRepository.save(analysisEntity);
        Long questionId = question2.getId();

        MultipartFile multipartFile = new MockMultipartFile("file",
                tempFile.getName(),
                Files.probeContentType(tempFile.toPath()),
                Files.readAllBytes(tempFile.toPath())
        );

        // when && then
        assertThatThrownBy(() -> answerService.createAnswer(savedAnalysisEntity.getId(), multipartFile, questionId))
                .isInstanceOf(AnswerConflictException.class)
                .hasMessage("이미 질문에 대한 답변이 존재합니다.");
    }


    private static AnalysisEntity createAnalysis(User user, List<Question> questions) {
        return AnalysisEntity.builder()
                .user(user)
                .questions(questions)
                .build();
    }

    private static Question createQuestion(QuestionContent questionContent) {
        return Question.of(questionContent);
    }

    private static User createUser() {
        return User.builder()
                .socialId("socialId")
                .socialType(SocialType.APPLE)
                .build();
    }

}