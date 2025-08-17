package com.project.doongdoong.domain.answer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Answer {

    private Long id;

    private Long analysisId;

    private Long voiceId;

    private String content;

    public static Answer of(Long voiceId) {
        return Answer.builder()
                .voiceId(voiceId)
                .build();
    }

    public void disconnectWithVoice() {
        this.voiceId = null;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
