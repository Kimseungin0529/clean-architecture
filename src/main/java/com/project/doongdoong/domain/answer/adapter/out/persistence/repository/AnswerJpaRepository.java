package com.project.doongdoong.domain.answer.adapter.out.persistence.repository;

import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

}
