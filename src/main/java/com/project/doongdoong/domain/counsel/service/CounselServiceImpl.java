package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.dto.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.CounselResultResponse;
import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.repository.CounselRepository;
import com.project.doongdoong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CounselServiceImpl implements CounselService {

    private final CounselRepository consultationRepository;
    private final UserRepository userRepository;


    @Override
    public Long createCounsel(String uniqueValue, CounselCreateRequest request) {

        Counsel counsel = Counsel.builder()
                .feellingState(request.getFeellingScore())
                .counselType(request.getCounselType())
                .build();

        Counsel createdCounsel = consultationRepository.save(counsel);

        return createdCounsel.getId();
    }

    @Override
    public CounselResultResponse consult(String socialId, Long counselId, boolean option) {

        return null;
    }

    @Override
    public void findCouselContent() {

    }

    @Override
    public void findConusels() {

    }

    private static String[] parseUniqueValue(String uniqueValue) {
        String[] values = uniqueValue.split("_"); // 사용자 찾기
        return values;
    }
}
