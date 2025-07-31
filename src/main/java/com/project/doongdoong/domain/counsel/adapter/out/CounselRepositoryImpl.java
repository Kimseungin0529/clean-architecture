package com.project.doongdoong.domain.counsel.adapter.out;

import com.project.doongdoong.domain.counsel.application.port.out.CounselRepository;
import com.project.doongdoong.domain.counsel.domain.Counsel;
import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.user.domain.User;
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
    public Counsel save(Counsel counsel) {
        return counselJpaRepository.save(CounselEntity.fromModel(counsel)).toModel();
    }

    @Override
    public Optional<Counsel> findWithAnalysisById(Long counselId) {
        return counselJpaRepository.findById(counselId).map(counselEntity -> counselEntity.toModel());
    }

    @Override
    public Page<Counsel> searchPageCounselList(User user, Pageable pageable) {
        return counselJpaRepository.searchPageCounselList(UserEntity.fromModel(user), pageable)
                .map(CounselEntity::toModel);
    }

    @Override
    public List<Object[]> countCounselGroupByDateAndType() {
        return counselJpaRepository.countCounselGroupByDateAndType();
    }
}
