package com.project.doongdoong.domain.counsel.application.port.out;

import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CounselRepository {

    CounselEntity save(CounselEntity counselEntity);

    Optional<CounselEntity> findWithAnalysisById(Long counselId);

    Page<CounselEntity> searchPageCounselList(UserEntity userEntity, Pageable pageable);

    List<Object[]> countCounselGroupByDateAndType();
}
