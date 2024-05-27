package com.project.doongdoong.domain.counsel.service;


import com.project.doongdoong.domain.counsel.dto.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.CounselResultResponse;

public interface CounselService {
    public CounselResultResponse consult(String socialId, CounselCreateRequest request);

    public void findCouselContent();

    public void findConusels();
}
