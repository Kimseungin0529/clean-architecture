package com.project.doongdoong.domain.counsel.service;


import com.project.doongdoong.domain.counsel.dto.CounselCreateRequest;

public interface CounselService {
    public Long createCounsel(String socialId, CounselCreateRequest request);

    public void consult();

    public void findCouselContent();

    public void findConusels();
}
