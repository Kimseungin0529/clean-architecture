package com.project.doongdoong.domain.counsel.adapter.out;

import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
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

    @Override
    public CounselEntity save(CounselEntity counselEntity) {
        return counselJpaRepository.save(counselEntity);
    }

    @Override
    public Optional<CounselEntity> findWithAnalysisById(Long counselId) {
        return counselJpaRepository.findById(counselId);
    }

    @Override
    public Page<CounselEntity> searchPageCounselList(UserEntity userEntity, Pageable pageable) {
        return counselJpaRepository.searchPageCounselList(userEntity, pageable);
    }

    @Override
    public List<Object[]> countCounselGroupByDateAndType() {
        return counselJpaRepository.countCounselGroupByDateAndType();
    }
}
