package com.project.doongdoong.domain.answer.model;

import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
public class Answer extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    @Builder
    public Answer(String content, Analysis analysis) {
        this.content = content;
        this.analysis = analysis;
    }
}
