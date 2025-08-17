package com.project.doongdoong.domain.counsel.adapter.out.repository;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.mapper.AnalysisEntityMapper;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.AnalysisEntity;
import com.project.doongdoong.domain.counsel.adapter.out.mapper.CounselEntityMapper;
import com.project.doongdoong.domain.counsel.application.port.out.CounselRepository;
import com.project.doongdoong.domain.counsel.domain.Counsel;
import com.project.doongdoong.domain.counsel.adapter.out.entitiy.CounselEntity;
import com.project.doongdoong.domain.user.adapter.out.persistence.mapper.UserEntityMapper;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CounselRepositoryImpl implements CounselRepository {

    private final CounselJpaRepository counselJpaRepository;

    private final CounselEntityMapper counselEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final AnalysisEntityMapper analysisEntityMapper;

    @Override
    public Counsel save(Counsel counsel) {
        UserEntity userEntity = userEntityMapper.fromId(counsel.getUserId());
        AnalysisEntity analysisEntity = counsel.hasAnalysis() ? analysisEntityMapper.fromId(counsel.getAnalysisId()) : null;
        CounselEntity counselEntity = counselJpaRepository.save(counselEntityMapper.fromModel(counsel, userEntity, analysisEntity));
        return counselEntityMapper.toModel(counselEntity);
    }

    @Override
    public Optional<Counsel> findCounselWithUserById(Long counselId) {
        return counselJpaRepository.findCounselWithUserByCounselId(counselId)
                .map(counselEntity -> counselEntityMapper.toModel(counselEntity, counselEntity.getId()));
    }

    @Override
    public Page<Counsel> searchPageCounselList(User user, Pageable pageable) {
        return counselJpaRepository.searchPageCounselList(userEntityMapper.fromModel(user), pageable)
                .map(counselEntityMapper::toModel);
    }

    @Override
    public List<Object[]> countCounselGroupByDateAndType() {
        return counselJpaRepository.countCounselGroupByDateAndType();
    }
}
