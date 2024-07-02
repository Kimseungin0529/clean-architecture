package com.project.doongdoong.domain.counsel.repository;

import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.model.CounselType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;


@ActiveProfiles("test")
@DataJpaTest
class CounselRepositoryTest {

    @Autowired
    CounselRepository counselRepository;
    @Autowired
    AnalysisRepository analysisRepository;
    
    @Test
    @DisplayName("상담 고유 번호로 분석 정보가 담긴 상담 정보를 조회한다.")
    void findWithAnalysisById(){
        //given
        Analysis analysis = Analysis.builder()
                .build();

        Counsel counsel = Counsel.builder()
                .question("질문1")
                .counselType(CounselType.FAMILY)
                .build();

        counsel.addAnalysis(analysis);
        Analysis savedAnalysis = analysisRepository.save(analysis);
        Counsel savedCounsel = counselRepository.save(counsel);


        //when
        Optional<Counsel> findCounsel = counselRepository.findWithAnalysisById(savedCounsel.getId());
        //then
        Assertions.assertThat(findCounsel.get()).isNotNull()
                .extracting("question", "counselType", "id")
                .containsExactlyInAnyOrder(
                        "질문1", CounselType.FAMILY, savedCounsel.getId()
                );
        Assertions.assertThat(findCounsel.get().getAnalysis().getId()).isEqualTo(savedAnalysis.getId());
    }
}