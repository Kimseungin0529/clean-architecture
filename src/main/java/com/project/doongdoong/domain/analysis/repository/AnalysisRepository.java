package com.project.doongdoong.domain.analysis.repository;

import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // JPA가 @Repository 없이도 빈 등록해줌.
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {


     @Query("select a from Analysis a join fetch Q")
    Optional<Analysis> findAnalysisByQuestionAndAnswer(Long analysisId);

    Page<Analysis> findAllByUserOrderByCreatedTime(User user, Pageable pageable);


}
