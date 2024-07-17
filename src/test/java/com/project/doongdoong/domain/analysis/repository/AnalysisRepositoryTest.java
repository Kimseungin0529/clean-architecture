package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.IntegrationSupportTest;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnalysisRepositoryTest extends IntegrationSupportTest {

    @Autowired AnalysisRepository analysisRepository;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("접근 회원과 고유 분석 번호를 통해 일치하는 분석 정보를 조회합니다.")
    void findByUserAndId(){
        //given
        User user1 = createUser("socialId1", SocialType.APPLE);
        User user2 = createUser("socialId2", SocialType.APPLE);
        User user3 = createUser("socialId3", SocialType.GOOGLE);
        User savedUser1 = userRepository.save(user1);
        userRepository.saveAll(List.of(user2, user3));

        Analysis analysis1 = createAnalysis(user1);
        Analysis analysis2 = createAnalysis(user1);
        Analysis analysis3 = createAnalysis(user2);
        Analysis savedAnalysis1 = analysisRepository.save(analysis1);
        Analysis savedAnalysis2 = analysisRepository.save(analysis2);
        Analysis savedAnalysis3 = analysisRepository.save(analysis3);

        Long requestId = savedAnalysis1.getId();

        //when
        Optional<Analysis> findAnalysis = analysisRepository.findByUserAndId(savedUser1, requestId);

        //then
        assertThat(findAnalysis.get())
                .isNotNull()
                .isEqualTo(savedAnalysis1);
        assertThat(findAnalysis.get().getUser())
                .isNotNull()
                .isEqualTo(savedUser1);

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