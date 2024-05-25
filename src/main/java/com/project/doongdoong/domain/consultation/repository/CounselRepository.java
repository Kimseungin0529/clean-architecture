package com.project.doongdoong.domain.consultation.repository;


import com.project.doongdoong.domain.consultation.model.Counsel;
import com.project.doongdoong.domain.consultation.repository.querydsl.CounselCustumRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselRepository extends JpaRepository<Counsel, Long>, CounselCustumRepository {

}
