package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.repository.CounselRepository;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CounselServiceImplTest {
    @Autowired
    CounselService counselService;
    @Autowired
    CounselRepository counselRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("상담을 생성합니다.")
    void createCounsel(){
        //given
        User user = User.builder()
                .socialId("1")
                .build();

        //when
        Long createdValue = counselService.createCounsel("1", null);
        //then
        assertThat(createdValue).isNotNull();

    }

}