package com.project.doongdoong.domain.user.repository.spring;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.domain.user.application.port.out.UserJpaRepository;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.doongdoong.domain.question.domain.QuestionContent.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


class UserEntityRepositoryTest extends IntegrationSupportTest {
    @Autowired
    UserJpaRepository userRepository;

    @DisplayName("감정 분석 리스트를 포함한 사용자 정보를 조회합니다.")
    @Test
    void findUserWithAnalysisBySocialTypeAndSocialId() {
        // given
        String socialId = "12345";
        UserEntity userEntity = UserEntity.builder()
                .socialId(socialId)
                .socialType(SocialType.APPLE)
                .build();
        List<QuestionEntity> questionEntityList1 = List.of(QuestionEntity.of(UNFIXED_QUESTION1), QuestionEntity.of(UNFIXED_QUESTION2),
                QuestionEntity.of(UNFIXED_QUESTION2), QuestionEntity.of(UNFIXED_QUESTION4));
        List<QuestionEntity> questionEntityList2 = List.of(QuestionEntity.of(FIXED_QUESTION1), QuestionEntity.of(FIXED_QUESTION2),
                QuestionEntity.of(FIXED_QUESTION3), QuestionEntity.of(FIXED_QUESTION4));
        List<QuestionEntity> questionEntityList3 = List.of(QuestionEntity.of(UNFIXED_QUESTION1), QuestionEntity.of(UNFIXED_QUESTION2),
                QuestionEntity.of(FIXED_QUESTION3), QuestionEntity.of(FIXED_QUESTION4));

        AnalysisEntity analysisEntity1 = AnalysisEntity.of(userEntity, questionEntityList1);
        AnalysisEntity analysisEntity2 = AnalysisEntity.of(userEntity, questionEntityList2);
        AnalysisEntity analysisEntity3 = AnalysisEntity.of(userEntity, questionEntityList3);
        analysisEntity1.addUser(userEntity);
        analysisEntity2.addUser(userEntity);
        analysisEntity3.addUser(userEntity);

        LocalDateTime now = LocalDateTime.now();
        LocalDate pastDay = now.minusDays(1L).toLocalDate();
        analysisEntity1.changeFeelingStateAndAnalyzeTime(15, pastDay);
        analysisEntity2.changeFeelingStateAndAnalyzeTime(20, now.toLocalDate());

        userRepository.save(userEntity);


        // when
        Optional<UserEntity> OptionalResult = userRepository.findUserWithAnalysisBySocialTypeAndSocialId(SocialType.APPLE, socialId);

        // then
        assertThat(OptionalResult).isPresent();
        UserEntity result = OptionalResult.get();

        assertThat(result)
                .extracting(UserEntity::getSocialId, UserEntity::getSocialType)
                .containsExactly(socialId, SocialType.APPLE);

        assertThat(result.getAnalysisList())
                .hasSize(3)
                .extracting(AnalysisEntity::getAnalyzeTime, AnalysisEntity::getFeelingState)
                .containsExactlyInAnyOrder(
                        tuple(analysisEntity1.getAnalyzeTime(), analysisEntity1.getFeelingState()),
                        tuple(analysisEntity2.getAnalyzeTime(), analysisEntity2.getFeelingState()),
                        tuple(analysisEntity3.getAnalyzeTime(), analysisEntity3.getFeelingState())
                );
    }

}