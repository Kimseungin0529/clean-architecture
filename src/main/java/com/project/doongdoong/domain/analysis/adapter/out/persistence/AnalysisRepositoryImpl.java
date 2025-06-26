package com.project.doongdoong.domain.analysis.adapter.out.persistence;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * application.port.out AnalysisRepository(DB 접근)에 대한 Adapter 구현
 */
@Repository
@RequiredArgsConstructor
public class AnalysisRepositoryImpl implements AnalysisRepository {

    private final AnalysisJpaRepository analysisJpaRepository;

    @Override
    public Analysis save(Analysis analysis) {
        return analysisJpaRepository.save(analysis);
    }

    @Override
    public Optional<Analysis> findByUserAndId(User user, Long analysisId) {
        return analysisJpaRepository.findByUserAndId(user, analysisId);
    }

    @Override
    public Page<Analysis> findAllByUserOrderByCreatedTime(User user, Pageable pageable) {
        return analysisJpaRepository.findAllByUserOrderByCreatedTime(user, pageable);
    }

    @Override
    public List<FeelingStateResponseDto> findAllByDateBetween(User user, LocalDate startTime, LocalDate endTime) {
        return analysisJpaRepository.findAllByDateBetween(user, startTime, endTime);
    }

    @Override
    public Optional<Analysis> findFirstByUserOrderByAnalyzeTimeDesc(User user) {
        return analysisJpaRepository.findFirstByUserOrderByAnalyzeTimeDesc(user);
    }

    @Override
    public Optional<Analysis> findAnalysisWithQuestion(Long analysisId) {
        return analysisJpaRepository.findAnalysisWithQuestion(analysisId);
    }

    @Override
    public Optional<Analysis> findAnalysis(Long id) {
        return analysisJpaRepository.findAnalysis(id);
    }

    @Override
    public void deleteAnalysis(Long id) {
        analysisJpaRepository.deleteAnalysis(id);
    }

    @Override
    public Optional<Analysis> searchFullAnalysisBy(Long analysisId) {
        return analysisJpaRepository.searchFullAnalysisBy(analysisId);
    }

    @Override
    public Optional<Analysis> searchAnalysisWithVoiceOfAnswer(Long analysisId) {
        return analysisJpaRepository.searchAnalysisWithVoiceOfAnswer(analysisId);
    }
}
