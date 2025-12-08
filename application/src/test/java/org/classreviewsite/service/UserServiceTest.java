package org.classreviewsite.service;

import org.classreviewsite.handler.exception.UserNotFoundException;
import org.classreviewsite.domain.user.Authority;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.domain.user.UserDataRepository;
import org.classreviewsite.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock UserDataRepository userDataRepository;
    @InjectMocks UserService userService;

    @Test
    @DisplayName("존재하는 사용자 ID로 조회시, 사용자 정보를 반환한다.")
    void findUserUserSuccess() {
        // given
        Integer userNumber = 20230857;
        User expectedUser = User.builder()
                .userNumber(userNumber)
                .userName("홍길동")
                .department("소프트웨어학과")
                .nickname("hong123")
                .password("encodedPassword")
                .authorities(Set.of())
                .build();
        
        given(userDataRepository.findById(userNumber)).willReturn(Optional.of(expectedUser));

        // when
        User user = userService.findUser(userNumber);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUserNumber()).isEqualTo(userNumber);
        assertThat(user.getUserName()).isEqualTo("홍길동");
        assertThat(user.getDepartment()).isEqualTo("소프트웨어학과");
        assertThat(user.getNickname()).isEqualTo("hong123");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회 시, UserNotFoundException을 발생한다.")
    void findUserUserNotFound() {
        // given
        Integer userNumber = 99999999;
        given(userDataRepository.findById(userNumber)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.findUser(userNumber))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 학생이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("null 사용자 ID로 조회 시, UserNotFoundException을 발생한다.")
    void findUserUserWithNullId() {
        // given
        Integer userNumber = null;
        given(userDataRepository.findById(userNumber)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.findUser(userNumber))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 학생이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("음수 사용자 ID로 조회 시, UserNotFoundException을 발생한다.")
    void findUserUserWithNegativeId() {
        // given
        Integer userNumber = -1;
        given(userDataRepository.findById(userNumber)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.findUser(userNumber))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 학생이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("8자리 학번 형식의 사용자 ID로 조회시, 정상적으로 사용자 정보를 반환한다.")
    void findUserUserWithValidStudentNumber() {
        // given
        Integer userNumber = 20240841; // 8자리 학번 형식
        User expectedUser = User.builder()
                .userNumber(userNumber)
                .userName("김학생")
                .department("컴퓨터공학과")
                .nickname("student2024")
                .password("hashedPassword")
                .authorities(Set.of())
                .build();
        
        given(userDataRepository.findById(userNumber)).willReturn(Optional.of(expectedUser));

        // when
        User user = userService.findUser(userNumber);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUserNumber()).isEqualTo(20240841);
        assertThat(user.getUserName()).isEqualTo("김학생");
        assertThat(user.getDepartment()).isEqualTo("컴퓨터공학과");
    }

    @Test
    @DisplayName("권한이 있는 사용자 조회시, 권한 정보도 함께 반환한다.")
    void findUserUserWithAuthorities() {
        // given
        Integer userNumber = 20230919;
        Authority userAuthority = Authority.builder()
                .authority("ROLE_USER")
                .build();
        
        User expectedUser = User.builder()
                .userNumber(userNumber)
                .userName("권한사용자")
                .department("소프트웨어학과")
                .nickname("auth_user")
                .password("password123")
                .authorities(Set.of(userAuthority))
                .build();
        
        given(userDataRepository.findById(userNumber)).willReturn(Optional.of(expectedUser));

        // when
        User user = userService.findUser(userNumber);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getAuthorities()).isNotEmpty();
        assertThat(user.getAuthorities()).hasSize(1);
    }

}
