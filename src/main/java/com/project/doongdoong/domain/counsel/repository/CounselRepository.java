package com.project.doongdoong.domain.counsel.repository;


import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.repository.querydsl.CounselCustumRepository;
import com.project.doongdoong.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounselRepository extends JpaRepository<Counsel, Long>, CounselCustumRepository {

}
