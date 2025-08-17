package com.project.doongdoong.domain.analysis.adapter.out.persistence.repository;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.mapper.AnalysisEntityMapper;
import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.AnalysisEntity;
import com.project.doongdoong.domain.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.adapter.out.persistence.entity.UserEntity;
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

    private final AnalysisEntityMapper analysisEntityMapper;
    private final UserEntityMapper userEntityMapper;

    private final AnalysisJpaRepository analysisJpaRepository;

    @Override
    public Analysis save(Analysis analysis) {
        UserEntity userEntity = userEntityMapper.fromId(analysis.getUserId());
        AnalysisEntity analysisEntity = analysisJpaRepository.save(analysisEntityMapper.fromModel(analysis, userEntity));
        return analysisEntityMapper.toModel(analysisEntity);
    }

    @Override
    public Optional<Analysis> findByUserIdAndId(Long userId, Long analysisId) {
        return analysisJpaRepository.findByUserAndId(userId, analysisId).map(analysisEntityMapper::toModel);
    }

    @Override
    public Page<Analysis> findAllByUserIdOrderByCreatedTime(Long userId, Pageable pageable) {
        return analysisJpaRepository.findAllByUserIdOrderByCreatedTime(userId, pageable)
                .map(analysisEntityMapper::toModel);
    }

    @Override
    public List<FeelingStateResponseDto> findAllByDateBetween(Long userId, LocalDate startTime, LocalDate endTime) {
        return analysisJpaRepository.findAllByDateBetween(userId, startTime, endTime);
    }

    @Override
    public Optional<Analysis> findFirstByUserOrderByAnalyzeTimeDesc(User user) {
        return analysisJpaRepository.findFirstByUserIdOrderByAnalyzeTimeDesc(user.getId())
                .map(analysisEntityMapper::toModel);
    }

}
