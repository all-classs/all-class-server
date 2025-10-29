package org.classreviewsite.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.handler.exception.UserNotFoundException;
import org.classreviewsite.security.jwt.JwtTokenProvider;
import org.classreviewsite.user.controller.data.request.LoginUserRequest;
import org.classreviewsite.user.controller.data.response.LoginUserResponse;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDataRepository userDataRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(readOnly = true)
    public User get(Long userNumber) {
        return userDataRepository.findById(userNumber)
                .orElseThrow(() -> new UserNotFoundException("해당 학생이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public User findUser(Long userNumber) {
        return userDataRepository.findById(userNumber)
                .orElseThrow(() -> new UserNotFoundException("해당 학생이 존재하지 않습니다."));
    }

    @Transactional
    public LoginUserResponse signIn(LoginUserRequest dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUserNumber(), dto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        return LoginUserResponse.of(
                authentication.getName(),
                token,
                String.valueOf(authentication.getAuthorities().stream()
                        .collect(Collectors.toCollection(ArrayList::new)).get(0))
        );
    }
}