package com.project.doongdoong.domain.answer.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.voice.domain.Voice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Answer {

    private Long id;

    private String content;

    private Analysis analysis;

    private Voice voice;

    @Builder
    public Answer(String content, Voice voice) {
        this.content = content;
        this.voice = voice;
    }

    public void connectAnalysis(Analysis analysis) {
        if (this.analysis != null) {
            return;
        }
        this.analysis = analysis;
        analysis.getAnswers().add(this);
    }

    public void disconnectWithVoice() {
        this.voice = null;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
