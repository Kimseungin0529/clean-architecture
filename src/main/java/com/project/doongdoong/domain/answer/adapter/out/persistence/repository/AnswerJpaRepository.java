package com.project.doongdoong.domain.answer.adapter.out.persistence.repository;

import com.project.doongdoong.domain.answer.adapter.out.persistence.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
