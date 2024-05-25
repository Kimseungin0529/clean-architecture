package com.project.doongdoong.domain.consultation.service;

import com.project.doongdoong.domain.consultation.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CounselServiceImpl implements CounselService {

    private final CounselRepository consultationRepository;


    @Override
    public void createCounsel() {

    }

    @Override
    public void consult() {

    }

    @Override
    public void findCouselContent() {

    }

    @Override
    public void findConusels() {

    }
}
