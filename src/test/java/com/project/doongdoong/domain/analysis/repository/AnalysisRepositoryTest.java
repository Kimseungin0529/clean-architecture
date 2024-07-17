package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.IntegrationSupportTest;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AnalysisRepositoryTest extends IntegrationSupportTest {

    @Autowired AnalysisRepository analysisRepository;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("접근 회원과 고유 분석 번호를 통해 일치하는 분석 정보를 조회합니다.")
    void findByUserAndId(){
        //given
        User user = createUser("socialId1", SocialType.APPLE);
        User savedUser = userRepository.save(user);

        Analysis analysis = createAnalysis(user);
        Analysis savedAnalysis = analysisRepository.save(analysis);

        Long requestId = savedAnalysis.getId();

        //when
        Optional<Analysis> findAnalysis = analysisRepository.findByUserAndId(savedUser, requestId);

        //then
        assertThat(findAnalysis.get())
                .isNotNull()
                .isEqualTo(savedAnalysis);
        assertThat(findAnalysis.get().getUser())
                .isNotNull()
                .isEqualTo(savedUser);

    }

    private static Analysis createAnalysis(User user1) {
        return Analysis.builder()
                .user(user1)
                .build();
    }

    private static User createUser(String socialId, SocialType socialType) {
        return User.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }

}