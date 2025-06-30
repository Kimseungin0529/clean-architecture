package com.project.doongdoong.domain.analysis.application.port.out;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnalysisRepository {

    AnalysisEntity save(AnalysisEntity analysisEntity);

    Optional<AnalysisEntity> findByUserAndId(User user, Long analysisId);

    Page<AnalysisEntity> findAllByUserOrderByCreatedTime(User user, Pageable pageable);

    List<FeelingStateResponseDto> findAllByDateBetween(User user, LocalDate startTime, LocalDate endTime);

    Optional<AnalysisEntity> findFirstByUserOrderByAnalyzeTimeDesc(User user);

    Optional<AnalysisEntity> findAnalysisWithQuestion(Long analysisId);

    Optional<AnalysisEntity> findAnalysis(Long id);

    void deleteAnalysis(Long id);

    Optional<AnalysisEntity> searchFullAnalysisBy(Long analysisId);

    Optional<AnalysisEntity> searchAnalysisWithVoiceOfAnswer(Long analysisId);

}
