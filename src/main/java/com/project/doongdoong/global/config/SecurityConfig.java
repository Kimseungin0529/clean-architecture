package com.project.doongdoong.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    //private final static String[] PERMIT_API = {"api/v1", "/api/users/sign-up", "/api/users/sign-in", "/api/users/reissue","/", ""};
    // 이상하게 문자열 배열로 허가 넣으면 인자 넣으면 인식을 못함 -> "/api/v1/ping", "/" 이와 같은 형식은 가능. 나중에 알아 보기
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/v1/ping").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
}
