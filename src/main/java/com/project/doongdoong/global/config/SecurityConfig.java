    package com.project.doongdoong.global.config;

    import com.project.doongdoong.global.fliter.JwtAuthFilter;
    import com.project.doongdoong.global.fliter.JwtExceptionHandlerFilter;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration @Slf4j
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;
        //private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
        //private final JwtExceptionFilter jwtExceptionFilter;
        /*private final CustomOAuth2UserService customOAuth2UserService;
        private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
        private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;*/
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


            return  http
                    .httpBasic(httpBasic -> httpBasic.disable())
                    .csrf(csrf -> csrf.disable())
                    // 모바일(프론트엔드)와 협업이 필요하므로 추후 cors 활성화 추가 필요
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) /**
                     세션관리 정책에 대한 설정을 STATELESS로 설정하는 코드입니다. jwt를 사용할거니 session은 빼기(다만 STATELESS는 세션이 있다면 사용함)
                     SessionCreationPolicy는 네가지 값이 존재합니다. 주의해야 할 점은 NEVER는 세션을 아예 사용하지 않겠다는 뜻이 아니라는 점에 유의해야 합니다
                     */
                    .authorizeHttpRequests(auth ->
                            auth
                                    .requestMatchers("/").permitAll()
                                    .requestMatchers("/api/v1/ping").permitAll() // 통신 test용 url
                                    .requestMatchers("/api/v1/login-oauth", "/api/v1/reissue").permitAll()
                                    .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                    /* .requestMatchers("/token/**").permitAll() // 토근 발급 경로 허용
                                     .requestMatchers("/kakao/callback").permitAll()
                                     .requestMatchers("/", "/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()*/
                                    .anyRequest().authenticated()
                    )
                    //.addFilterBefore(jwtExceptionHandlerFilter, jwtAuthFilter.getClass())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
                    /*.oauth2Login(oauth2 -> oauth2.
                            userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                            .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패시 처리할 핸들러를 지정해준다.
                            .successHandler(oAuth2LoginSuccessHandler) // OAuth2 로그인 성공시 처리할 핸들러를 지정해준다.
                    ); // 추후 웹 사용으로 인해 위 설정이 필요하다면 블로그 링크를 통해 다시 구현
                    웹과 다르게 앱에서는 카카오 SDK와 같이 클라이언트 측에서 직접 OAuth 검증을 하므로 필요가 없음.
                    // OAuth2 로그인 설정*/
        }


    }
