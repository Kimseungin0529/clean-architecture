package com.project.doongdoong.domain.counsel.controller;

import com.project.doongdoong.domain.counsel.service.CounselService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CounselController.class) // 컨트롤러 테스트를 위해 관련 빈만 등록해줌
class CounselControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CounselService counselService;

    @Test
    @DisplayName("신규 상담을 생성한다.")
    @WithMockUser
    void createCounsel(){
        //given

        //when

        //then

    }

}