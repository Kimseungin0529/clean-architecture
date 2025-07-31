package com.project.doongdoong.domain.answer.domain;

import com.project.doongdoong.domain.voice.domain.Voice;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Answer {

    private Long id;

    private String content;

    private Voice voice;

    @Builder
    public Answer(String content, Voice voice) {
        this.content = content;
        this.voice = voice;
    }

    public static Answer of(Voice voice) {

        return Answer.builder()
                .voice(voice)
                .build();
    }

    public static Answer of(Long id, String content, Voice voice) {
        Answer answer = Answer.of(voice);
        answer.id = id;
        answer.content = content;
        return answer;
    }

    public void disconnectWithVoice() {
        this.voice = null;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
