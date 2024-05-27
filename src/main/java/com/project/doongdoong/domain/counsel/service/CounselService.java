package com.project.doongdoong.domain.counsel.service;


import com.project.doongdoong.domain.counsel.dto.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.CounselResultResponse;

public interface CounselService {
    public Long createCounsel(String socialId, CounselCreateRequest request);

    public CounselResultResponse consult(String socialId, Long counselId, boolean option);

    public void findCouselContent();

    public void findConusels();
}
