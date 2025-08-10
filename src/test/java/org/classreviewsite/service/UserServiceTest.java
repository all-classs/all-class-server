package org.classreviewsite.service;

import autoparams.AutoSource;
import org.classreviewsite.handler.exception.UserNotFoundException;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.controller.data.request.CreateUserRequest;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.classreviewsite.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock UserDataRepository userDataRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserService userService;


    @Test
    @DisplayName("존재하는 사용자 ID로 조회시, 사용자 정보를 반환한다.")
    void findById() {
        Long userNumber = 12345L;
        given(userDataRepository.findById(userNumber)).willReturn(Optional.of(new User()));

        User user = userService.findById(userNumber);

        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회 시, UserNotFoundException을 발생한다.")
    void userNotFoundException() {
        Long userNumber = 12345L;
        given(userDataRepository.findById(userNumber)).willReturn(Optional.of(new User()));

        given(userService.findById(userNumber)).willThrow(new UserNotFoundException(""));

        assertThatThrownBy(() -> userService.findById(userNumber))
                .isInstanceOf(UserNotFoundException.class);
    }

    @AutoSource
    @ParameterizedTest
    @Transactional
    @DisplayName("신규 사용자 정보를 입력 시, 회원가입을 성공한다.")
    void signUp(CreateUserRequest request) {

        given(passwordEncoder.encode(request.getPassword())).willReturn(UUID.randomUUID().toString());
        given(userDataRepository.findByUserNumber(request.getUserNumber())).willReturn(Optional.of(new User()));
//        when(userDataRepository.save(new User())).thenReturn();
        willDoNothing().given(userDataRepository.save(any(User.class)));

        userService.signUp(request);

    }



}
