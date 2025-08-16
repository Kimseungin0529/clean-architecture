package com.project.doongdoong.domain.counsel.adapter.out.repository.querydsl;

import com.project.doongdoong.domain.counsel.adapter.out.entitiy.CounselEntity;
import com.project.doongdoong.domain.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CounselCustomJpaRepository {

    Page<CounselEntity> searchPageCounselList(UserEntity userEntity, Pageable pageable);
}
