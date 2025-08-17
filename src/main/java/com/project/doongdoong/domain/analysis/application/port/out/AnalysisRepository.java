package com.project.doongdoong.domain.analysis.application.port.out;

import com.project.doongdoong.domain.analysis.adapter.in.dto.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnalysisRepository {

    Analysis save(Analysis analysis);

    Optional<Analysis> findByUserIdAndId(Long userId, Long analysisId);

    Page<Analysis> findAllByUserIdOrderByCreatedTime(Long userId, Pageable pageable);

    List<FeelingStateResponseDto> findAllByDateBetween(Long userId, LocalDate startTime, LocalDate endTime);

    Optional<Analysis> findFirstByUserOrderByAnalyzeTimeDesc(User user);


}
