package com.project.doongdoong.domain.voice.adapter.out.persistence.mapper;

import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import org.springframework.stereotype.Component;

@Component
public class VoiceEntityMapper {

    public VoiceEntity fromId(Long voiceId) {
        return VoiceEntity.builder()
                .voiceId(voiceId)
                .build();
    }

    public VoiceEntity fromModel(Voice voice) {
        return VoiceEntity.commonBuilder()
                .originName(voice.getOriginName())
                .build();
    }

    public Voice toModel(VoiceEntity voiceEntity) {
        return Voice.builder()
                .voiceId(voiceEntity.getVoiceId())
                .originName(voiceEntity.getOriginName())
                .storedName(voiceEntity.getStoredName())
                .accessUrl(voiceEntity.getAccessUrl())
                .build();
    }
}
