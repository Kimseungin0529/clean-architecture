package com.project.doongdoong.domain.answer.adapter.out.persistence.entity;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.AnalysisEntity;
import com.project.doongdoong.domain.voice.adapter.out.persistence.entity.VoiceEntity;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "answer")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class AnswerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AnalysisEntity analysis;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_id", nullable = false)
    private VoiceEntity voice;

    @Column(length = 5000)
    private String content;
}
