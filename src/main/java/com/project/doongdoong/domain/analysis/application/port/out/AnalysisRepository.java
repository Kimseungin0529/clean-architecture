package com.project.doongdoong.domain.analysis.application.port.out;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnalysisRepository {
    Optional<Analysis> findById(Long analysisId);

    Analysis save(Analysis analysis);

    Optional<Analysis> findByUserAndId(UserEntity userEntity, Long analysisId);

    Page<Analysis> findAllByUserOrderByCreatedTime(UserEntity userEntity, Pageable pageable);

    List<FeelingStateResponseDto> findAllByDateBetween(UserEntity userEntity, LocalDate startTime, LocalDate endTime);

    Optional<Analysis> findFirstByUserOrderByAnalyzeTimeDesc(UserEntity userEntity);

    Optional<Analysis> findAnalysisWithQuestion(Long analysisId);

    Optional<Analysis> findAnalysis(Long id);

    void deleteAnalysis(Long id);

    Optional<Analysis> searchFullAnalysisBy(Long analysisId);

    Optional<Analysis> searchAnalysisWithVoiceOfAnswer(Long analysisId);

}
