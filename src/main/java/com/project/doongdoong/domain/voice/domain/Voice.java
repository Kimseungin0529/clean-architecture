package com.project.doongdoong.domain.voice.domain;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Voice {

    private Long voiceId;

    private String originName; // 음성 파일의 본래 이름

    private String storedName; // 음성 파일이 S3에 저장될때 사용되는 이름

    private String accessUrl; // S3 내부 음성 파일에 접근할 수 있는 URL

    private QuestionContent questionContent;


    @Builder(builderClassName = "CommonBuilder", builderMethodName = "commonBuilder")
    public Voice(String originName) {
        this.originName = originName;
        this.storedName = gainRandomFileName(originName);
        this.accessUrl = "";
    }

    @Builder(builderClassName = "InitVoiceContentBuilder", builderMethodName = "initVoiceContentBuilder")
    private Voice(String originName, QuestionContent questionContent) {
        this.originName = originName;
        this.storedName = gainRandomFileName() + originName;
        this.accessUrl = "";
        this.questionContent = questionContent;
    }

    public static Voice of(String originName) {
        return new Voice(originName);
    }

    public static Voice ofAll(Long voiceId, String originName, String accessUrl, QuestionContent questionContent) {
        Voice voice = of(originName);
        voice.voiceId = voiceId;
        voice.accessUrl = accessUrl;
        voice.questionContent = questionContent;

        return voice;
    }

    public void changeAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    private String gainRandomFileName(String originName) {
        return UUID.randomUUID() + extractExtension(originName);
    }

    private String gainRandomFileName() {
        return UUID.randomUUID().toString();
    }

    private String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');
        return originName.substring(index);
    }
}
