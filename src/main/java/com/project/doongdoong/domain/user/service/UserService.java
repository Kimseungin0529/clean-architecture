package com.project.doongdoong.domain.user.service;

import com.project.doongdoong.domain.user.exeception.RefreshTokenNoutFoundException;
import com.project.doongdoong.domain.user.exeception.TokenInfoFobiddenException;
import com.project.doongdoong.domain.user.exeception.UserProviderNotFoundException;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.global.BlackAccessToken;
import com.project.doongdoong.global.JwtProvider;
import com.project.doongdoong.global.RefreshToken;
import com.project.doongdoong.global.dto.request.LogoutDto;
import com.project.doongdoong.global.dto.request.OAuthTokenDto;
import com.project.doongdoong.global.dto.request.ReissueDto;
import com.project.doongdoong.global.dto.response.TokenDto;
import com.project.doongdoong.global.repositoty.BlackAccessTokenRepository;
import com.project.doongdoong.global.repositoty.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackAccessTokenRepository blackAccessTokenRepository;
    private final JwtProvider jwtProvider;


    @Transactional
    public TokenDto checkRegistration(OAuthTokenDto oAuthTokenInfo) {

        String socialId = oAuthTokenInfo.getSocialId();
        String email = oAuthTokenInfo.getEmail();
        String nickname = oAuthTokenInfo.getNickname();
        SocialType socialType = SocialType.customValueOf(oAuthTokenInfo.getSocailType());
        if(socialType == null){
            new UserProviderNotFoundException();
        }

        User user = userRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .orElse(toEntity(socialId, email, nickname, socialType));

        checkChange(email, nickname, user);
        userRepository.save(user); // 기존 회원 정보가 없는 경우에는 영속화 필요

        Optional<BlackAccessToken> blackAccessToken = blackAccessTokenRepository.findByOtherKey(email + " " + socialType.getText());
        log.info("blackAccessToken db에서 조회하기");
        if(blackAccessToken.isPresent()){ //
            blackAccessTokenRepository.delete(blackAccessToken.get());
            log.info("blackAccessToken 존재해서 삭제");
        }else{
            log.info("blackAccessToken 존재 X");
        }
        TokenDto tokenInfoResponse = jwtProvider.generateToken(email, "ROLE_USER", socialType.getText());
        /**
         * rft을 저장하는데 다시 로그인하는 경우, 기존 rft이 존재한다면 닷
         */
        RefreshToken refresh = RefreshToken.of(email, tokenInfoResponse.getRefreshToken(), socialType.getText());
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findById(refresh.getId());

        if(findRefreshToken.isPresent() && findRefreshToken.get().getSocialType().equals(socialType.getText())){ // 기존 rft이 존재한다면 삭제하고 새롭게 저장
            refreshTokenRepository.delete(findRefreshToken.get());
            log.info("기존 RefreshToken 삭제");
        }else{
            log.info("기존 RefreshToken 존재 X");
        }
        refreshTokenRepository.save(refresh);
        log.info("새로운 RefreshToken 저장");

        return tokenInfoResponse;
    }

    private void checkChange(String email, String nickname, User user) {
        if (!user.getEmail().equals(email) || !user.getNickname().equals(nickname)) {
            user.changeEmail(email);
            user.changeNickname(nickname);
        }
    }

    private User toEntity(String socialId, String email, String nickname, SocialType socialType) {
        return User
                .builder()
                .socailId(socialId)
                .email(email)
                .nickname(nickname)
                .socialType(socialType)
                .build();
    }

    @Transactional
    public TokenDto reissue(ReissueDto reissueTokenDto){
        String refreshToken = reissueTokenDto.getRefreshToken();
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        
        if(findRefreshToken.isPresent()) { // rft이 존재한다면
            String token = findRefreshToken.get().getRefreshToken().substring(7);
            String email = jwtProvider.extractEmail(token);
            String socialType = jwtProvider.extractSocialType(token);
            String role = jwtProvider.extractRole(token);
            String accessToken = jwtProvider.createAccessToken(email, socialType, role); // act 갱신

            return TokenDto.builder()
                    .accessToken(accessToken)
                    .build();
        }else{
            new RefreshTokenNoutFoundException();
        }

        return null;
    }

    @Transactional
    public void logout(LogoutDto tokenInfoDto, String accessToken) {
        String socialType = jwtProvider.extractSocialType(accessToken.substring(7));
        String email = jwtProvider.extractEmail(accessToken.substring(7));
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByRefreshToken(tokenInfoDto.getRefreshToken());

        String accessEmail = jwtProvider.extractEmail(accessToken.substring(7));
        String accessSocailType = jwtProvider.extractSocialType(accessToken.substring(7));
        if (!accessEmail.equals(email)){
            new TokenInfoFobiddenException();
        }
        if (!accessSocailType.equals(socialType)){
            new TokenInfoFobiddenException();
        }

        log.info("단계1");
        BlackAccessToken blackAccessToken = BlackAccessToken.of(email, accessToken, socialType);
        blackAccessTokenRepository.save(blackAccessToken); // blackAccessToken 저장 -> 해당 act은 만료기간 남았더라도 접근 불가
        log.info("단계2 -> blackToken 생성");
        if(refreshToken.isPresent()){       // blackAccessToken으로 이미 접근 권한을 막았지만 더 안전한 보안으로 rft도 삭제
            refreshTokenRepository.delete(refreshToken.get()); // rft이 존재한다면 삭제
            log.info("로그아웃으로 인해 기존 refreshToken 삭제");
        }
        else{
            /**
             * 인증되지 않은 사용자라는 예외처리 필요
             */
        }
        log.info("로그아웃 메소드 종료");
    }
    /*public boolean checkBlackToken(HttpServletRequest request){
        String accessToken = request.getHeader("Authorization");
        Optional<BlackAccessToken> findBlackToken = blackAccessTokenRepository.findByAccessToken(accessToken);
        if(findBlackToken.isPresent()){
            throw new CustomException.UnauthorizedException(HttpStatus.UNAUTHORIZED, "로그아웃으로 인해 인가 권한이 없습니다.");
        }
    }*/
}
