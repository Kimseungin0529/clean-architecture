package com.project.doongdoong.domain.counsel.adapter.out.mapper;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.AnalysisEntity;
import com.project.doongdoong.domain.counsel.domain.Counsel;
import com.project.doongdoong.domain.counsel.adapter.out.entitiy.CounselEntity;
import com.project.doongdoong.domain.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CounselEntityMapper {

    public CounselEntity fromModel(Counsel counsel, UserEntity userEntity, AnalysisEntity analysisEntity) {
        return CounselEntity.builder()
                .id(counsel.getId())
                .user(userEntity)
                .analysis(analysisEntity)
                .question(counsel.getQuestion())
                .answer(counsel.getAnswer())
                .imageUrl(counsel.getImageUrl())
                .counselType(counsel.getCounselType())
                .build();
    }

    public Counsel toModel(CounselEntity counselEntity) {
        return Counsel.builder()
                .id(counselEntity.getId())
                .question(counselEntity.getQuestion())
                .answer(counselEntity.getAnswer())
                .imageUrl(counselEntity.getImageUrl())
                .counselType(counselEntity.getCounselType())
                .createdAt(counselEntity.getCreatedAt())
                .build();
    }

    public Counsel toModel(CounselEntity counselEntity, Long userId) {
        return Counsel.builder()
                .id(counselEntity.getId())
                .userId(userId)
                .question(counselEntity.getQuestion())
                .answer(counselEntity.getAnswer())
                .imageUrl(counselEntity.getImageUrl())
                .counselType(counselEntity.getCounselType())
                .createdAt(counselEntity.getCreatedAt())
                .build();
    }

}
