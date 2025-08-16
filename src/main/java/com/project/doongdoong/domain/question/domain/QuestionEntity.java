package com.project.doongdoong.domain.question.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuestionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AnalysisEntity analysis;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private AnswerEntity answer;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private QuestionContent questionContent;
}