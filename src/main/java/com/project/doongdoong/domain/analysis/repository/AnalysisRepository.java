package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.analysis.dto.response.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.querydls.AnalysisRepositoryCustom;
import com.project.doongdoong.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository // JPA가 @Repository 없이도 빈 등록해줌.
public interface AnalysisRepository extends JpaRepository<Analysis, Long>, AnalysisRepositoryCustom {


    Page<Analysis> findAllByUserOrderByCreatedTime(User user, Pageable pageable);

    // 현재 시간 기준으로 일주일 치 분석값 하루 기준으로 그룹핑해서 가져오기
    @Query("select new com.project.doongdoong.domain.analysis.dto.response.FeelingStateResponseDto" +
            "(CONCAT(YEAR(a.createdTime), '-', MONTH(a.createdTime), '-', DAY(a.createdTime)), avg(a.feelingState))" +
            "from Analysis a where a.user = :user " +
            "and a.createdTime between :startTime AND :endTime" +
            " group by YEAR(a.createdTime), MONTH(a.createdTime), DAY(a.createdTime) ")
    List<FeelingStateResponseDto> findAllByDateBetween(@Param("user") User user
            , @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    Optional<Analysis> findFirstByUserOrderByCreatedTimeDesc(User user);

    @Query("select analysis from Analysis analysis join fetch analysis.questions where analysis.id = :analysisId")
    Optional<Analysis> findAnalysisWithQuestion(@Param("analysisId") Long analysisId);
}
