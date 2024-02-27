package com.project.doongdoong.global.fliter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.domain.user.service.UserService;
import com.project.doongdoong.global.JwtProvider;
import com.project.doongdoong.global.common.ApiResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j @Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            // request Header에서 AccessToken을 가져온다.
            String token = resolveToken(request);
            // 만약 "Bearer "이 붙어서 온다면 제거해줘야 함. 이따 빼주는 로직이 없다면 추가
            log.info("JWT 필터 검사 시작.");
            log.info("Bearer 삭제한 token 값 = {}", token);

            //토큰이 존재하면서 유효하다면 Authentication 객체 생성
            //시큐리티 컨텍스트 홀더에 Authentication 저장
            if(validateToken(token) && !jwtProvider.checkLogoutToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            log.info("토큰 검증 성공");
            filterChain.doFilter(request,response);

        }catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."); // 만료된 토큰이라면 rft이 있는 경우, 재발급해주기
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 토큰입니다.");
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            log.info("새로운 JWT 토큰 오류입니다.");
            log.info("예외 메세지 = {}",e.getMessage());
            setErrorResponse(response, HttpStatus.UNAUTHORIZED,  "새로운 JWT 토큰 오류입니다.");
        }


    }
        /*try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            //토큰의 유효기간 만료
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
        } catch (JwtException | IllegalArgumentException e) {
            //유효하지 않은 토큰
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }*/
    private void setErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<Object> result = ApiResponse.of(status, message, null);
        try{
            response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    public boolean validateToken(String token) {  // 토큰의 유효성 검증을 수행
        if (token == null) {
            log.info("인가가 필요없는 API 호출에서는 토큰 필요 X, 따라서 토큰이 null인 경우는 그렇게 판단하고 제외");
            return false;
        }
        Jwts.parserBuilder()
                .setSigningKey(jwtProvider.getKey())
                .build()
                .parseClaimsJws(token);
        // Jwts.parserBuilder().setSigningKey(jwtProvider.getKey()).build().parseClaimsJws(token)
        return true;
        /*try {

        }*/ /*catch (SecurityException | MalformedJwtException e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."); // 만료된 토큰이라면 rft이 있는 경우, 재발급해주기
        } catch (UnsupportedJwtException e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, "새로운 JWT 토큰 오류입니다.");
        }*/
    }

}
