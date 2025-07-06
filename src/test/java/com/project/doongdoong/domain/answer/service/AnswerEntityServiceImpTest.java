package com.project.doongdoong.domain.answer.service;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.answer.adapter.in.dto.AnswerCreateResponseDto;
import com.project.doongdoong.domain.answer.application.port.in.AnswerService;
import com.project.doongdoong.domain.answer.exception.AnswerConflictException;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.domain.user.application.port.out.UserJpaRepository;
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
    AnalysisJpaRepository analysisRepository;
    @Autowired
    UserJpaRepository userRepository;


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
        UserEntity userEntity = createUser();
        userRepository.save(userEntity);

        QuestionEntity questionEntity1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        List<QuestionEntity> questionEntities = List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4);

        AnalysisEntity analysisEntity = createAnalysis(userEntity, questionEntities);
        questionEntity1.connectAnalysis(analysisEntity);
        questionEntity2.connectAnalysis(analysisEntity);
        questionEntity3.connectAnalysis(analysisEntity);
        questionEntity4.connectAnalysis(analysisEntity);
        AnswerEntity answerEntity = AnswerEntity.builder()
                .build();

        answerEntity.connectAnalysis(analysisEntity);
        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);
        Long questionId = questionEntity2.getId();

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
        UserEntity userEntity = createUser();
        userRepository.save(userEntity);

        QuestionEntity questionEntity1 = createQuestion(QuestionContent.FIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(QuestionContent.FIXED_QUESTION2);
        QuestionEntity questionEntity3 = createQuestion(QuestionContent.UNFIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(QuestionContent.UNFIXED_QUESTION3);
        List<QuestionEntity> questionEntities = List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4);

        AnalysisEntity analysisEntity = createAnalysis(userEntity, questionEntities);
        questionEntity1.connectAnalysis(analysisEntity);
        questionEntity2.connectAnalysis(analysisEntity);
        questionEntity3.connectAnalysis(analysisEntity);
        questionEntity4.connectAnalysis(analysisEntity);
        AnswerEntity answerEntity = AnswerEntity.builder()
                .build();

        answerEntity.connectAnalysis(analysisEntity);
        questionEntity2.connectAnswer(answerEntity);

        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);
        Long questionId = questionEntity2.getId();

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


    private static AnalysisEntity createAnalysis(UserEntity userEntity, List<QuestionEntity> questionEntities) {
        return AnalysisEntity.builder()
                .userEntity(userEntity)
                .questionEntities(questionEntities)
                .build();
    }

    private static QuestionEntity createQuestion(QuestionContent questionContent) {
        return QuestionEntity.of(questionContent);
    }

    private static UserEntity createUser() {
        return UserEntity.builder()
                .socialId("socialId")
                .socialType(SocialType.APPLE)
                .build();
    }

}