package com.project.doongdoong.domain.analysis.adapter.out.persistence.repository;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.querydls.AnalysisJpaRepositoryCustom;
import com.project.doongdoong.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository // JPA가 @Repository 없이도 빈 등록해줌.
public interface AnalysisJpaRepository extends JpaRepository<AnalysisEntity, Long>, AnalysisJpaRepositoryCustom {


    @Query("select a from AnalysisEntity a left outer join fetch a.counsel where a.user = :user and a.id = :id")
    Optional<AnalysisEntity> findByUserAndId(@Param("user") User user, @Param("id") Long analysisId);

    Page<AnalysisEntity> findAllByUserOrderByCreatedTime(User user, Pageable pageable);

    // 현재 시간 기준으로 일주일 치 분석값 하루 기준으로 그룹핑해서 가져오기
    // between보다 <= >= 이게 속도 빠르다그랫음
    @Query("select new com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto" +
            "(CONCAT(YEAR(a.analyzeTime), '-', MONTH(a.analyzeTime), '-', DAY(a.analyzeTime)), avg(a.feelingState))" +
            "from AnalysisEntity a where a.user = :user " +
            "and a.analyzeTime between :startTime AND :endTime" +
            " group by YEAR(a.analyzeTime), MONTH(a.analyzeTime), DAY(a.analyzeTime) ")
    List<FeelingStateResponseDto> findAllByDateBetween(@Param("user") User user
            , @Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime);

    Optional<AnalysisEntity> findFirstByUserOrderByAnalyzeTimeDesc(User user);

    @Query("select analysis from AnalysisEntity analysis join fetch analysis.questions where analysis.id = :analysisId")
    Optional<AnalysisEntity> findAnalysisWithQuestion(@Param("analysisId") Long analysisId);

    @Query("select analysis from AnalysisEntity analysis left outer join analysis.counsel where analysis.id = :id")
    Optional<AnalysisEntity> findAnalysis(Long id);

    @Modifying
    @Query("delete from AnalysisEntity analysis where analysis.id = :id")
    void deleteAnalysis(Long id);
}
