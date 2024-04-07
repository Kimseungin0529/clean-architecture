package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.analysis.model.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // JPA가 @Repository 없이도 빈 등록해줌.
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

}
