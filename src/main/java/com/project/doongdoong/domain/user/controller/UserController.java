package com.project.doongdoong.domain.user.controller;

import com.project.doongdoong.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class UserController {


    @GetMapping("/ping")
    public ApiResponse<?> testPing(){
        return ApiResponse.of(HttpStatus.OK, null, "ping");
    }
}
