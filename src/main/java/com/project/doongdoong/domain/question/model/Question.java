package com.project.doongdoong.domain.question.model;

import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String question;

    @Column(nullable = false, updatable = false)
    private String answer;

    @Builder
    public Question(String question) {
        this.question = question;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;



}
