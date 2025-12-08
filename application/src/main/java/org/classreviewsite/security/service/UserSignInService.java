package org.classreviewsite.security.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.security.jwt.JwtTokenProvider;
import org.classreviewsite.user.controller.data.request.LoginUserRequest;
import org.classreviewsite.user.controller.data.response.LoginUserResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSignInService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginUserResponse signIn(LoginUserRequest request) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUserNumber(), request.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String token = jwtTokenProvider.createToken(authentication);

        return LoginUserResponse.of(authentication.getName(), token, String.valueOf(authentication.getAuthorities().stream().collect(Collectors.toCollection(ArrayList::new)).get(0)) );
    }
}
