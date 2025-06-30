package com.project.doongdoong.domain.user.application.port.in;

import com.project.doongdoong.domain.user.adapter.in.dto.UserInformationResponseDto;
import com.project.doongdoong.global.dto.request.LogoutDto;
import com.project.doongdoong.global.dto.request.OAuthTokenDto;
import com.project.doongdoong.global.dto.request.ReissueDto;
import com.project.doongdoong.global.dto.response.TokenDto;

public interface UserService {
    TokenDto checkRegistration(OAuthTokenDto oAuthTokenInfo);

    TokenDto reissue(ReissueDto reissueTokenDto);

    void logout(LogoutDto tokenInfoDto, String accessToken);

    UserInformationResponseDto getMyPage(String uniqueValue);
}
