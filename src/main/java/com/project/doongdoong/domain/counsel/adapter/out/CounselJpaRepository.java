package com.project.doongdoong.domain.counsel.adapter.out;


import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounselJpaRepository extends JpaRepository<CounselEntity, Long>, CounselCustomRepository {

    @Query("select c from CounselEntity c left outer join fetch c.analysis where c.id = :counselId")
    Optional<CounselEntity> findWithAnalysisById(@Param("counselId") Long counselId);

    @Query(value = """
            SELECT DATE(created_time) AS date, counsel_type, COUNT(*) AS count
            FROM counselEntity
            GROUP BY DATE(created_time), counsel_type
            """, nativeQuery = true)
    List<Object[]> countCounselGroupByDateAndType();

}
