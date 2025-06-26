package com.project.doongdoong.domain.analysis.application.port.out;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnalysisRepository {

    Analysis save(Analysis analysis);

    Optional<Analysis> findByUserAndId(User user, Long analysisId);

    Page<Analysis> findAllByUserOrderByCreatedTime(User user, Pageable pageable);

    List<FeelingStateResponseDto> findAllByDateBetween(User user, LocalDate startTime, LocalDate endTime);

    Optional<Analysis> findFirstByUserOrderByAnalyzeTimeDesc(User user);

    Optional<Analysis> findAnalysisWithQuestion(Long analysisId);

    Optional<Analysis> findAnalysis(Long id);

    void deleteAnalysis(Long id);

    Optional<Analysis> searchFullAnalysisBy(Long analysisId);

    Optional<Analysis> searchAnalysisWithVoiceOfAnswer(Long analysisId);

}
