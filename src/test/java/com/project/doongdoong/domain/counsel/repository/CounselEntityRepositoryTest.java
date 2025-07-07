package com.project.doongdoong.domain.counsel.repository;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.counsel.adapter.out.CounselJpaRepository;
import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.counsel.domain.CounselType;
import com.project.doongdoong.domain.user.adapter.out.persistence.UserJpaRepository;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;


class CounselEntityRepositoryTest extends IntegrationSupportTest {

    @Autowired
    CounselJpaRepository counselRepository;
    @Autowired
    AnalysisJpaRepository analysisRepository;
    @Autowired
    UserJpaRepository userRepository;

    @Test
    @DisplayName("상담 고유 번호로 분석 정보가 담긴 상담 정보를 조회한다.")
    void findWithAnalysisById() {
        //given
        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .build();

        CounselEntity counselEntity = CounselEntity.builder()
                .question("질문1")
                .counselType(CounselType.FAMILY)
                .build();

        counselEntity.addAnalysis(analysisEntity);
        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);
        CounselEntity savedCounselEntity = counselRepository.save(counselEntity);


        //when
        Optional<CounselEntity> findCounsel = counselRepository.findWithAnalysisById(savedCounselEntity.getId());
        //then
        Assertions.assertThat(findCounsel.get()).isNotNull()
                .extracting("question", "counselType", "id")
                .containsExactlyInAnyOrder(
                        "질문1", CounselType.FAMILY, savedCounselEntity.getId()
                );
        Assertions.assertThat(findCounsel.get().getAnalysis().getId()).isEqualTo(savedAnalysisEntity.getId());
    }

    @Test
    @DisplayName("본인의 상담 기록 중 특정 페이지를 조회한다.")
    void searchPageCounselList() {
        //given
        UserEntity userEntity = UserEntity.builder()
                .nickname("짱구")
                .email("whffkaos007@naver.com")
                .build();
        UserEntity savedUserEntity = userRepository.save(userEntity);

        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .build();
        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);

        CounselEntity counselEntity1 = CounselEntity.builder()
                .userEntity(savedUserEntity)
                .question("질문1")
                .counselType(CounselType.FAMILY)
                .build();
        CounselEntity counselEntity2 = CounselEntity.builder()
                .userEntity(savedUserEntity)
                .question("질문2")
                .counselType(CounselType.JOB)
                .build();
        CounselEntity counselEntity3 = CounselEntity.builder()
                .userEntity(savedUserEntity)
                .question("질문3")
                .counselType(CounselType.LOVE)
                .build();

        counselEntity2.addAnalysis(savedAnalysisEntity);
        counselRepository.saveAll(List.of(counselEntity1, counselEntity2, counselEntity3));

        int pageNumber = 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        //when
        Page<CounselEntity> counselPages = counselRepository.searchPageCounselList(savedUserEntity, pageRequest);

        //then
        Assertions.assertThat(counselPages.getNumber()).isEqualTo(pageNumber);
        Assertions.assertThat(counselPages.getSize()).isEqualTo(pageSize);
        Assertions.assertThat(counselPages.getContent())
                .hasSize(3)
                .extracting("user", "question", "counselType", "analysis")
                .containsExactlyInAnyOrder(
                        Assertions.tuple(savedUserEntity, "질문1", CounselType.FAMILY, null),
                        Assertions.tuple(savedUserEntity, "질문2", CounselType.JOB, savedAnalysisEntity),
                        Assertions.tuple(savedUserEntity, "질문3", CounselType.LOVE, null)
                );


    }
}