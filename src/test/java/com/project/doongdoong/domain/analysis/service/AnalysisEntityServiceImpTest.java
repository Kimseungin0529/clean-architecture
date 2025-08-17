package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.adapter.in.dto.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisRepositoryImpl;
import com.project.doongdoong.domain.analysis.application.port.in.AnalysisService;
import com.project.doongdoong.domain.question.application.port.in.QuestionProvidable;
import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.user.adapter.out.persistence.repository.UserJpaRepository;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.adapter.out.persistence.entity.UserEntity;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.domain.voice.adapter.out.persistence.repository.VoiceJpaRepository;
import com.project.doongdoong.domain.voice.adapter.out.persistence.entity.VoiceEntity;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AnalysisEntityServiceImpTest extends IntegrationSupportTest {


    @Autowired
    AnalysisService analysisService;
    @Autowired
    UserJpaRepository userRepository;
    @Autowired
    VoiceJpaRepository voiceRepository;


    @TestFactory
    @DisplayName("서비스 회원 정보와 존재하지 않는 서비스 회원 정보의 경우, 분석에 관한 정보 접근 시나리오")
    Collection<DynamicTest> createAnalysis() {
        //given
        for (QuestionContent questionContent : QuestionContent.values()) {
            VoiceEntity voiceEntity = VoiceEntity.initVoiceContentBuilder()
                    .originName(questionContent.getText() + "_voice.mp3")
                    .questionContent(questionContent)
                    .build();
            voiceEntity.changeAccessUrl("임의의 접근 url 주소");
            voiceRepository.save(voiceEntity);
        }

        UserEntity userEntity1 = createUser("socialId", SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity1);

        //when
        return List.of(
                DynamicTest.dynamicTest("존재하지 않는 사용자 정보로는 분석 관련 정보에 접근할 수 없습니다.", () -> {
                    //given
                    UserEntity userEntity2 = createUser("notFoundSocialId", SocialType.GOOGLE);
                    String uniqueValue2 = userEntity2.getSocialId() + "_" + userEntity2.getSocialType();
                    //when & then
                    assertThatThrownBy(() -> analysisService.createAnalysis(uniqueValue2))
                            .isInstanceOf(UserNotFoundException.class)
                            .hasMessage("해당 사용자는 존재하지 않습니다.");

                })
        );
    }


    private static UserEntity createUser(String socialId, SocialType socialType) {
        return UserEntity.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }


}