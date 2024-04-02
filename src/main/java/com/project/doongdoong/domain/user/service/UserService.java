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
import com.project.doongdoong.global.exception.CustomException;
import com.project.doongdoong.global.repositoty.BlackAccessTokenRepository;
import com.project.doongdoong.global.repositoty.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

        /**
         * 받아온 act, rft이 카카오 로그인을 통해 받아온 토큰이 맞는지 확인이 필요할까?
         * 필요하다면 검증 로직 추가해야 함.
         * -> 필요한 거 같다. 해당 메소드를 호출하는 API(소셜 로그인)는 이미 client에서 검증이 된 상태로 서버한테 넘어온다.
         * 해당 API는 서버에서 인증할 수 없으므로 security 설정에서 인증 허용해야 접근 가능하다.
         * 또한 클라이언트 측에서 제공한 토큰이 직접 카카오(OAuth)한테 검증받은 토큰인지 알 수 없다.
         * 현재 생각난 방안은 해당 토큰으로 카카오 API를 호출하여 성공하면 서버 자체 jwt를 제공해주고 실패하면 jwt를 발급하지 않는 방법을 사용하거나
         * 이를 보완해줄 security 설정이 필요해 보인다.
         */

        String email = oAuthTokenInfo.getEmail();
        String nickname = oAuthTokenInfo.getNickname();
        SocialType socialType = SocialType.customValueOf(oAuthTokenInfo.getSocailType());
        if(socialType == null){
            new UserProviderNotFoundException();
        }

        Optional<User> findUser = userRepository.findBySocialTypeAndEmail(socialType, email);
        if(findUser.isEmpty()){ // 만약 없다면 사용자 정보 DB에 저장
            User user = User
                    .builder()
                    .email(email)
                    .nickname(nickname)
                    .socialType(socialType)
                    .build();

            userRepository.save(user);
            log.info("사용자 정보 DB 저장");
        }else{
            log.info("이미 존재하므로 DB 저장 X");
        }
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
