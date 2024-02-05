package com.project.doongdoong.global.fliter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.domain.user.service.UserService;
import com.project.doongdoong.global.JwtProvider;
import com.project.doongdoong.global.common.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j @Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request Header에서 AccessToken을 가져온다.
        String token = resolveToken(request);
        // 만약 "Bearer "이 붙어서 온다면 제거해줘야 함. 이따 빼주는 로직이 없다면 추가
        log.info("JWT 필터 검사 시작.");
        log.info("Bearer 삭제한 token 값 = {}", token);
        if(token == null){
            new IllegalStateException("토큰이 비어 있습니다.");
        }
        /*log.info("token이 null이 아님."); // 아래와 같이 필터 예외처리하면 정상적으로 필터 통과가 안됨. 마지막 filterChain.doFilter에서 에러 발생
        if(!jwtProvider.validateToken(token)) {
            log.info("validation 예외 발생");
            jwtExceptionHandler(response,HttpStatus.FORBIDDEN, List.of("만료된 accessToken입니다."));
        }
        log.info("validation 통과, checkLogoutToken 시작");
        if(jwtProvider.checkLogoutToken(token)){ // 로그아웃할 때 있던 act은 더 이상 사용못하게 블랙 토큰 지정
            log.info("로그아웃 토큰 접근 예외 발생");
            jwtExceptionHandler(response,HttpStatus.FORBIDDEN, List.of("로그아웃으로 인해 금지된 accessToken입니다."));
        }*/
        //토큰이 존재하면서 유효하다면 Authentication 객체 생성
        //시큐리티 컨텍스트 홀더에 Authentication 저장
        if(jwtProvider.validateToken(token) && !jwtProvider.checkLogoutToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("토큰 검증 성공");
        filterChain.doFilter(request,response);

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    // 토큰에 대한 오류가 발생했을 때, 커스터마이징해서 Exception 처리 값을 클라이언트에게 알려준다.
    public void jwtExceptionHandler(HttpServletResponse response, HttpStatus status, List<String> messages) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ApiResponse.of(status,messages,null));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
