package com.project.doongdoong.domain.answer.domain;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AnswerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(length = 5000)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "analysis_id")
    private AnalysisEntity analysis;

    @OneToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "voice_id", unique = true)
    private Voice voice;

    @Builder
    public AnswerEntity(String content, Voice voice) {
        this.content = content;
        this.voice = voice;
    }

    public void connectAnalysis(AnalysisEntity analysisEntity) {
        if (this.analysis != null) {
            return;
        }
        this.analysis = analysisEntity;
        analysisEntity.getAnswerEntities().add(this);
    }

    public void disconnectWithVoice() {
        this.voice = null;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
