package com.project.doongdoong.domain.analysis.adapter.out.persistence.repository;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.AnalysisEntity;
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
public interface AnalysisJpaRepository extends JpaRepository<AnalysisEntity, Long> {


    @Query("select a from AnalysisEntity a where a.id = :userId and a.id = :id")
    Optional<AnalysisEntity> findByUserAndId(@Param("userId") Long userId, @Param("id") Long analysisId);

    Page<AnalysisEntity> findAllByUserIdOrderByCreatedTime(Long userId, Pageable pageable);

    // 현재 시간 기준으로 일주일 치 분석값 하루 기준으로 그룹핑해서 가져오기
    // between보다 <= >= 이게 속도 빠르다그랫음
    @Query("select new com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto" +
            "(CONCAT(YEAR(a.analyzeTime), '-', MONTH(a.analyzeTime), '-', DAY(a.analyzeTime)), avg(a.feelingState))" +
            "from AnalysisEntity a where a.user.id = :userId " +
            "and a.analyzeTime between :startTime AND :endTime" +
            " group by YEAR(a.analyzeTime), MONTH(a.analyzeTime), DAY(a.analyzeTime) ")
    List<FeelingStateResponseDto> findAllByDateBetween(@Param("user") Long userId
            , @Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime);

    @Query("select analysis from AnalysisEntity analysis " +
            "where analysis.user.id = :userId order by analysis.analyzeTime desc limit 1")
    Optional<AnalysisEntity> findFirstByUserIdOrderByAnalyzeTimeDesc(Long userId);

    @Modifying
    @Query("delete from AnalysisEntity analysis where analysis.id = :id")
    void deleteAnalysis(Long id);
}
