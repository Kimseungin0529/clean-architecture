package com.project.doongdoong.domain.counsel.service;


public interface CounselService {
    public Long createCounsel(String socialId, Double score);

    public void consult();

    public void findCouselContent();

    public void findConusels();
}
