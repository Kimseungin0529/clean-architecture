package com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.user.adapter.out.persistence.entity.UserEntity;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "analysis")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnalysisEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "analysis_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    private double feelingState;

    private LocalDate analyzeTime;

    private boolean isUsed;
}
