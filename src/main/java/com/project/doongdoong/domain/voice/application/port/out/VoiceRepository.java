package com.project.doongdoong.domain.voice.application.port.out;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoiceRepository extends JpaRepository<VoiceEntity, Long> {
    public Optional<VoiceEntity> findByAccessUrl(String voiceUrl);

    public Optional<VoiceEntity> findVoiceByQuestionContent(QuestionContent questionContent);
    public List<VoiceEntity> findVoiceAllByQuestionContentIn(List<QuestionContent> questionContent);

    public Optional<VoiceEntity> findVoiceByAccessUrl(String accessUrl);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from VoiceEntity voice where voice.voiceId in :voiceIds")
    void deleteVoicesByUrls(@Param("voiceIds") List<Long> voiceIds);

}

