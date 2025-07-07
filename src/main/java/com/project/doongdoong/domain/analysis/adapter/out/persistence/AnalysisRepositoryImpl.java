package com.project.doongdoong.domain.analysis.adapter.out.persistence;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
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
    public Optional<Analysis> findById(Long analysisId) {
        return analysisJpaRepository.findById(analysisId).map(AnalysisEntity::toModel);
    }

    @Override
    public Analysis save(AnalysisEntity analysisEntity) {
        return analysisJpaRepository.save(analysisEntity).toModel();
    }

    @Override
    public Optional<Analysis> findByUserAndId(UserEntity userEntity, Long analysisId) {
        return analysisJpaRepository.findByUserAndId(userEntity, analysisId).map(AnalysisEntity::toModel);
    }

    @Override
    public Page<Analysis> findAllByUserOrderByCreatedTime(UserEntity userEntity, Pageable pageable) {
        return analysisJpaRepository.findAllByUserOrderByCreatedTime(userEntity, pageable)
                .map(AnalysisEntity::toModel);
    }

    @Override
    public List<FeelingStateResponseDto> findAllByDateBetween(UserEntity userEntity, LocalDate startTime, LocalDate endTime) {
        return analysisJpaRepository.findAllByDateBetween(userEntity, startTime, endTime);
    }

    @Override
    public Optional<Analysis> findFirstByUserOrderByAnalyzeTimeDesc(UserEntity userEntity) {
        return analysisJpaRepository.findFirstByUserOrderByAnalyzeTimeDesc(userEntity)
                .map(AnalysisEntity::toModel);
    }

    @Override
    public Optional<Analysis> findAnalysisWithQuestion(Long analysisId) {
        return analysisJpaRepository.findAnalysisWithQuestion(analysisId)
                .map(AnalysisEntity::toModel);
    }

    @Override
    public Optional<Analysis> findAnalysis(Long id) {
        return analysisJpaRepository.findAnalysis(id).map(AnalysisEntity::toModel);
    }

    @Override
    public void deleteAnalysis(Long id) {
        analysisJpaRepository.deleteAnalysis(id);
    }

    @Override
    public Optional<Analysis> searchFullAnalysisBy(Long analysisId) {
        return analysisJpaRepository.searchFullAnalysisBy(analysisId).map(AnalysisEntity::toModel);
    }

    @Override
    public Optional<Analysis> searchAnalysisWithVoiceOfAnswer(Long analysisId) {
        return analysisJpaRepository.searchAnalysisWithVoiceOfAnswer(analysisId).map(AnalysisEntity::toModel);
    }
}
