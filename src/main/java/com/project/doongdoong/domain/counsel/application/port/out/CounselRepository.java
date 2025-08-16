package com.project.doongdoong.domain.counsel.application.port.out;

import com.project.doongdoong.domain.counsel.domain.Counsel;
import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CounselRepository {

    Counsel save(Counsel counsel);

    Optional<Counsel> findCounselWithUserById(Long counselId);

    Page<Counsel> searchPageCounselList(User user, Pageable pageable);

    List<Object[]> countCounselGroupByDateAndType();
}
