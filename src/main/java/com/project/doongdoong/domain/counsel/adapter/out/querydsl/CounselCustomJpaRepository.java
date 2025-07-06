package com.project.doongdoong.domain.counsel.adapter.out.querydsl;

import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CounselCustomJpaRepository {

    Page<CounselEntity> searchPageCounselList(UserEntity userEntity, Pageable pageable);
}
