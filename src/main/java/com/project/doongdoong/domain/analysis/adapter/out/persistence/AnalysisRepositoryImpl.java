package com.project.doongdoong.domain.analysis.adapter.out.persistence;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.user.domain.UserEntity;
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
    public AnalysisEntity findById(Long analysisId) {
        return analysisJpaRepository.findById(analysisId).orElseThrow(AnalysisNotFoundException::new);
    }

    @Override
    public AnalysisEntity save(AnalysisEntity analysisEntity) {
        return analysisJpaRepository.save(analysisEntity);
    }

    @Override
    public Optional<AnalysisEntity> findByUserAndId(UserEntity userEntity, Long analysisId) {
        return analysisJpaRepository.findByUserAndId(userEntity, analysisId);
    }

    @Override
    public Page<AnalysisEntity> findAllByUserOrderByCreatedTime(UserEntity userEntity, Pageable pageable) {
        return analysisJpaRepository.findAllByUserOrderByCreatedTime(userEntity, pageable);
    }

    @Override
    public List<FeelingStateResponseDto> findAllByDateBetween(UserEntity userEntity, LocalDate startTime, LocalDate endTime) {
        return analysisJpaRepository.findAllByDateBetween(userEntity, startTime, endTime);
    }

    @Override
    public Optional<AnalysisEntity> findFirstByUserOrderByAnalyzeTimeDesc(UserEntity userEntity) {
        return analysisJpaRepository.findFirstByUserOrderByAnalyzeTimeDesc(userEntity);
    }

    @Override
    public Optional<AnalysisEntity> findAnalysisWithQuestion(Long analysisId) {
        return analysisJpaRepository.findAnalysisWithQuestion(analysisId);
    }

    @Override
    public Optional<AnalysisEntity> findAnalysis(Long id) {
        return analysisJpaRepository.findAnalysis(id);
    }

    @Override
    public void deleteAnalysis(Long id) {
        analysisJpaRepository.deleteAnalysis(id);
    }

    @Override
    public Optional<AnalysisEntity> searchFullAnalysisBy(Long analysisId) {
        return analysisJpaRepository.searchFullAnalysisBy(analysisId);
    }

    @Override
    public Optional<AnalysisEntity> searchAnalysisWithVoiceOfAnswer(Long analysisId) {
        return analysisJpaRepository.searchAnalysisWithVoiceOfAnswer(analysisId);
    }
}
