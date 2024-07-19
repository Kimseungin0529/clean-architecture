package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.IntegrationSupportTest;
import com.project.doongdoong.domain.analysis.dto.response.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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

    @DisplayName("특정 시간 내에 있는 사용자의 감정 분석 시간과 감정 수치를 조회합니다.")
    @ParameterizedTest
    @CsvSource({"2024-03-05, 2024-03-15", "2024-03-02, 2024-03-30","2024-03-03, 2024-03-27" })
    void findAllByDateBetween(LocalDate startTime, LocalDate endTime){
        //given
        User user = createUser("socialId", SocialType.APPLE);

        User savedUser = userRepository.save(user);

        Analysis analysis1 = createAnalysis(savedUser);
        Analysis analysis2 = createAnalysis(savedUser);
        Analysis analysis3 = createAnalysis(savedUser);
        Analysis analysis4 = createAnalysis(savedUser);
        Analysis analysis5 = createAnalysis(savedUser);

        analysis1.changeFeelingStateAndAnalyzeTime(72.5, LocalDate.of(2024,3,1));
        analysis2.changeFeelingStateAndAnalyzeTime(75, LocalDate.of(2024,3,5));
        analysis3.changeFeelingStateAndAnalyzeTime(77.5, LocalDate.of(2024,3, 15));
        analysis4.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 3, 15));
        analysis5.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 3, 31));

        analysisRepository.saveAll(List.of(analysis1, analysis2, analysis3, analysis4, analysis5));

        //when
        List<FeelingStateResponseDto> result = analysisRepository.findAllByDateBetween(savedUser, startTime, endTime);
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
    void (){
        //given
        User user = createUser("socialId", SocialType.APPLE);
        User savedUser = userRepository.save(user);

        Analysis analysis1 = createAnalysis(savedUser);
        Analysis analysis2 = createAnalysis(savedUser);
        Analysis analysis3 = createAnalysis(savedUser);
        Analysis analysis4 = createAnalysis(savedUser);

        analysis1.changeFeelingStateAndAnalyzeTime(70, LocalDate.of(2023, 12, 5));
        analysis2.changeFeelingStateAndAnalyzeTime(75, LocalDate.of(2024, 3, 5));
        analysis3.changeFeelingStateAndAnalyzeTime(80, LocalDate.of(2024, 6, 27));
        analysis4.changeFeelingStateAndAnalyzeTime(90, LocalDate.of(2024, 11, 19));

        analysisRepository.saveAll(List.of(analysis1, analysis2, analysis3, analysis4));

        //when
        Optional<Analysis> result = analysisRepository.findFirstByUserOrderByAnalyzeTimeDesc(savedUser);

        //then
        assertThat(result.get()).isNotNull()
                .isEqualTo(analysis4)
                .extracting("user", "feelingState", "analyzeTime")
                .contains(savedUser, analysis4.getFeelingState(), analysis4.getAnalyzeTime());
    }

    private static Analysis createAnalysis(User user) {
        return Analysis.builder()
                .user(user)
                .build();


    }

    private static User createUser(String socialId, SocialType socialType) {
        return User.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }

}