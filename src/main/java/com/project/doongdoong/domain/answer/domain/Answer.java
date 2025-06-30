package com.project.doongdoong.domain.answer.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import lombok.Builder;


public class Answer {

    private Long id;

    private String content;

    private Analysis analysis;

    private VoiceEntity voiceEntity;

    @Builder
    public Answer(String content, VoiceEntity voiceEntity) {
        this.content = content;
        this.voiceEntity = voiceEntity;
    }

    public void connectAnalysis(Analysis analysis) {
        if (this.analysis != null) {
            return;
        }
        this.analysis = analysis;
        analysis.getAnswers().add(this);
    }

    public void disconnectWithVoice() {
        this.voiceEntity = null;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
