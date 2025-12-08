package org.classreviewsite.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.security.service.UserSignInService;
import org.classreviewsite.util.Result;
import org.classreviewsite.user.controller.data.request.LoginUserRequest;
import org.classreviewsite.user.controller.data.response.LoginUserResponse;
import org.classreviewsite.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "유저 정보 API", description = "유저 정보 관련 요청입니다.")
public class UserController {

   private final UserService userService;
   private final UserSignInService userSignInService;

    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "로그인 요청", description = "로그인을 요청합니다.")
    @ApiResponse(responseCode = "200", description = "로그인을 성공하였습니다.")
    @ApiResponse(responseCode = "401", description = "학번은 8자리입니다.")
    @ApiResponse(responseCode = "401", description = "아이디 및 비밀번호를 확인해주세요")
    public Result signIn(@Parameter(required = true, description = "학번, 비밀번호 요청") @RequestBody LoginUserRequest dto, HttpServletResponse response){
        LoginUserResponse loginResponse = userSignInService.signIn(dto);
        return Result.success(loginResponse, "로그인을 성공하였습니다.");
    }

}
