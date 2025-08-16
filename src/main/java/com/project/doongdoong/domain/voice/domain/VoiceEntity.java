package com.project.doongdoong.domain.voice.domain;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Table(name = "voice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VoiceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voiceId;

    private String originName; // 음성 파일의 본래 이름

    private String storedName; // 음성 파일이 S3에 저장될때 사용되는 이름

    private String accessUrl; // S3 내부 음성 파일에 접근할 수 있는 URL

    @Enumerated(EnumType.STRING)
    private QuestionContent questionContent;


    @Builder(builderClassName = "CommonBuilder", builderMethodName = "commonBuilder")
    public VoiceEntity(String originName) {
        this.originName = originName;
        this.storedName = gainRandomFileName(originName);
        this.accessUrl = "";
    }

    @Builder(builderClassName = "InitVoiceContentBuilder", builderMethodName = "initVoiceContentBuilder")
    public VoiceEntity(String originName, QuestionContent questionContent) {
        this.originName = originName;
        this.storedName = gainRandomFileName() + originName;
        this.accessUrl = "";
        this.questionContent = questionContent;
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
