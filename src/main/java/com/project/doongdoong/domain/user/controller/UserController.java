package com.project.doongdoong.domain.user.controller;

import com.project.doongdoong.domain.user.service.UserService;
import com.project.doongdoong.global.common.ApiResponse;
import com.project.doongdoong.global.common.ErrorResponse;
import com.project.doongdoong.global.dto.request.LogoutDto;
import com.project.doongdoong.global.dto.request.OAuthTokenDto;
import com.project.doongdoong.global.dto.request.ReissueDto;
import com.project.doongdoong.global.dto.response.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 API", description = "로그인, 로그아웃 등 사용자 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "jwt 없이 호출용 test API")
    @GetMapping("/ping")
    public ApiResponse<?> testPing(){
        return ApiResponse.of(HttpStatus.OK, null, "ping");
    }

    @Operation(summary = "로그인 API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                examples = {
                    @ExampleObject(value = "{\n" +
                            " \"accessToken\": \"카카오 access token\",\n" +
                            " \"refreshToken\": \"카카오 refresh token\",\n " +
                            " \"nickname\": \"jini\",\n " +
                            " \"email\": \"whffkaos007@naver.com\",\n" +
                            " \"socailType\": \"KAKAO\"\n" +
                            "}")
                }
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400_1",
                    description = "잘못된 입력 형식이 존재합니다. 각 필드는 빈 칸 X, email은 형식을 맞추고 soailType은 대문자로 입력해야 함.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"errorCode\": \"400\",\n" +
                                            "  \"message\": \"잘못된 입력 형식이 존재합니다.\",\n" +
                                            "  \"detail\": [ \n" +
                                            "        \"알파벳 대문자로 입력해주세요.\",\n" +
                                            "        \"이메일이 공백입니다.\",\n" +
                                            "        \"refresh_tokne이 존재하지 않습니다.\",\n" +
                                            "        \"access_tokne이 존재하지 않습니다.\",\n" +
                                            "        \"닉네임이 공백입니다.\"\n" +
                                            "   ]" +
                                            "}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "해당 소셜 타입은 존재하지 않습니다. 대문자로 입력해주세요.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"errorCode\": \"400\",\n" +
                                            "  \"message\": \"해당 소셜 타입은 존재하지 않습니다. 대문자로 입력해주세요.\",\n" +
                                            "  \"detail\": null\n" +
                                            "}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 로그인 성공",
                    content = @Content(
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"code\": 200,\n" + "  \"state\": \"OK\",\n" + "\"message\" : null,\n" +
                                            "  \"data\": { \n" +
                                            "  \"accessToken\": \"Bearer asdfmj7zo2f.fasd1fb.z123dd1t\",\n" +
                                            "  \"refreshToken\": \"Bearer fasdfasfdasf.f2as7d9fb.z1_1k231t\" \n" +
                                            "  } \n" +
                                            "}")
                            }
                    )
            )
        }
    )
    @PostMapping("/login-oauth")
    public ApiResponse<?> userSignIn(@Valid @RequestBody OAuthTokenDto oAuthTokenInfo){
        TokenDto tokenInfoResponse = userService.checkRegistration(oAuthTokenInfo);

        return ApiResponse.of(HttpStatus.OK, null, tokenInfoResponse);
    }



    @Operation(summary = "로그아웃 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400_1",
                    description = "잘못된 입력 형식이 존재합니다. 각 필드는 빈 칸 X",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"errorCode\": \"400\",\n" +
                                            "  \"message\": \"잘못된 입력 형식이 존재합니다.\", \n" +
                                            "  \"detail\": [ \n" +
                                            "        \"refreshToken이 비어 있습니다.\"\n" +
                                            "   ]" +
                                            "}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "\"act과 rft 간 이메일 정보가 일치하지 않습니다.\"",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"errorCode\": \"400\",\n" +
                                            "  \"message\": \"act과 rft 간 이메일 정보가 일치하지 않습니다.\",\n" +
                                            "  \"detail\": null\n" +
                                            "}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403_1",
                    description = "\"act과 rft 간 소셜 타입 정보가 일치하지 않습니다.\"",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"errorCode\": \"400\",\n" +
                                            "  \"message\": \"act과 rft 간 소셜 타입 정보가 일치하지 않습니다.\",\n" +
                                            "  \"detail\": null\n" +
                                            "}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 로그아웃 성공",
                    content = @Content(
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"code\": 200,\n" + "  \"state\": \"OK\",\n" + "\"message\" : null,\n" +
                                            "  \"data\": \"logout success\" \n" +
                                            "}")
                            }
                    )
            )
    }
    )
    @PostMapping("/logout-oauth")
    public ApiResponse<?> userLogout(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            " \"refreshToken\": \"Bearer.XXXXXXXXX.XXXXXXXXX.XXXXXXXXXXXXX\"\n " +
                                            "}")
                            }
                    )
            )
                                    @Valid @RequestBody LogoutDto logoutDto,
            //@Parameter(description = "Authorization token", required = true,schema = @Schema(type = "string"), in = ParameterIn.HEADER)
            @RequestHeader("Authorization") String accessToken){
        userService.logout(logoutDto, accessToken);

        return ApiResponse.of(HttpStatus.OK, null, "logout success");
    }

    @Operation(summary = "토큰 재발급 API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = {
                            @ExampleObject(value = "{\n" +
                                    " \"refreshToken\": \"Bearer.XXXXXXXXX.XXXXXXXXX.XXXXXXXXXXXXX\"\n " +
                                    "}")
                    }
            )
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력 형식이 존재합니다. 각 필드는 빈 칸 X",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"errorCode\": \"400\",\n" +
                                            "  \"message\": \"잘못된 입력 형식이 존재합니다.\", \n" +
                                            "  \"detail\": [ \n" +
                                            "        \"refreshToken이 비어 있습니다.\"\n" +
                                            "   ]" +
                                            "}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "\"rft이 존재하지 않습니다.\"",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "  \"errorCode\": \"403\",\n" +
                                            "  \"message\": \"refreshToken이 존재하지 않아 토큰 갱신에 실패했습니다.\",\n" +
                                            "  \"detail\": null\n" +
                                            "}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    content = @Content(
                            examples = {
                                    @ExampleObject(value = "{\n" +
                                            "    \"code\": 200,\n" + "    \"state\": \"OK\",\n" + "    \"message\": null,\n" +
                                            "    \"data\": {\n" +
                                            "        \"accessToken\": \"Bearer XXXXXXXXXXXXX.XXXXXXXXXXXXXXXXXXXXXXXXXX.XXXXXXXXXXXXXXX\",\n" +
                                            "        \"refreshToken\": null\n" +
                                            "    }\n" +
                                            "}")
                            }
                    )
            )
    }
    )
    @PostMapping("/reissue")
    public ApiResponse<?> userReissue(@Valid @RequestBody ReissueDto reissueDto){
        TokenDto reissuedToken = userService.reissue(reissueDto);

        return ApiResponse.of(HttpStatus.OK, null, reissuedToken);
    }

    @Operation(summary = "jwt 테스트 API")
    @GetMapping("/jwt-test")
    public ApiResponse<?> test(){
        
        return ApiResponse.of(HttpStatus.OK, null, "성공");
    }

}
