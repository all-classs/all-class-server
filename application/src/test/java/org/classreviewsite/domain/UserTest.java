package org.classreviewsite.domain;

import org.classreviewsite.user.infrastructure.Authority;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Nested
    @DisplayName("User 엔티티 생성 테스트")
    class createUserTest {
        
        @Test
        @DisplayName("User 빌더로 정상적인 사용자를 생성한다")
        void createUserWithBuilder() {
            // given
            int userNumber = 20230857;
            String userName = "홍길동";
            String department = "소프트웨어학과";
            String nickname = "hong123";
            String password = "encodedPassword";
            Set<Authority> authorities = Set.of();
            
            // when
            User user = User.builder()
                    .userNumber(userNumber)
                    .userName(userName)
                    .department(department)
                    .nickname(nickname)
                    .password(password)
                    .authorities(authorities)
                    .build();
            
            // then
            assertThat(user).isNotNull();
            assertThat(user.getUserNumber()).isEqualTo(userNumber);
            assertThat(user.getUserName()).isEqualTo(userName);
            assertThat(user.getDepartment()).isEqualTo(department);
            assertThat(user.getNickname()).isEqualTo(nickname);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getAuthorities()).isEqualTo(authorities);
        }
        
        @Test
        @DisplayName("권한이 있는 사용자를 생성한다")
        void createUserWithAuthorities() {
            // given
            Authority studentAuthority = Authority.builder()
                    .authority("STUDENT")
                    .build();
            
            Set<Authority> authorities = Set.of(studentAuthority);
            
            // when
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("학생사용자")
                    .department("소프트웨어학과")
                    .nickname("student123")
                    .password("password")
                    .authorities(authorities)
                    .build();
            
            // then
            assertThat(user.getAuthorities()).isNotEmpty();
            assertThat(user.getAuthorities()).hasSize(1);
            assertThat(user.getAuthorities()).contains(studentAuthority);
        }
        
        @Test
        @DisplayName("여러 권한을 가진 사용자를 생성한다")
        void createUserWithMultipleAuthorities() {
            // given
            Authority studentAuthority = Authority.builder()
                    .authority("STUDENT")
                    .build();
            
            Authority adminAuthority = Authority.builder()
                    .authority("ADMIN")
                    .build();
            
            Set<Authority> authorities = Set.of(studentAuthority, adminAuthority);
            
            // when
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("관리자학생")
                    .department("소프트웨어학과")
                    .nickname("admin_student")
                    .password("password")
                    .authorities(authorities)
                    .build();
            
            // then
            assertThat(user.getAuthorities()).hasSize(2);
            assertThat(user.getAuthorities()).containsExactlyInAnyOrder(studentAuthority, adminAuthority);
        }
        
        @Test
        @DisplayName("8자리 학번으로 사용자를 생성한다")
        void createUserWithValidStudentNumber() {
            // given
            int validUserNumber = 20240841;
            
            // when
            User user = User.builder()
                    .userNumber(validUserNumber)
                    .userName("김학생")
                    .department("컴퓨터공학과")
                    .nickname("student2024")
                    .password("hashedPassword")
                    .authorities(Set.of())
                    .build();
            
            // then
            assertThat(user.getUserNumber()).isEqualTo(validUserNumber);
            assertThat(String.valueOf(user.getUserNumber())).hasSize(8);
        }
        
        @Test
        @DisplayName("빈 권한 세트로 사용자를 생성한다")
        void createUserWithEmptyAuthorities() {
            // given
            Set<Authority> emptyAuthorities = Set.of();
            
            // when
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("권한없는사용자")
                    .department("소프트웨어학과")
                    .nickname("no_auth")
                    .password("password")
                    .authorities(emptyAuthorities)
                    .build();
            
            // then
            assertThat(user.getAuthorities()).isEmpty();
            assertThat(user.getAuthorities()).hasSize(0);
        }
        
        @Test
        @DisplayName("긴 닉네임으로 사용자를 생성한다")
        void createUserWithLongNickname() {
            // given
            String longNickname = "very_long_nickname_that_might_be_close_to_limit";
            
            // when
            User user = User.builder()
                    .userNumber(20230857)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname(longNickname)
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            // then
            assertThat(user.getNickname()).isEqualTo(longNickname);
            assertThat(user.getNickname().length()).isLessThanOrEqualTo(200); // 최대 길이 체크
        }
        
        @Test
        @DisplayName("다양한 학과의 사용자를 생성한다")
        void createUsersFromVariousDepartments() {
            // given
            String[] departments = {"소프트웨어학과", "컴퓨터공학과", "전기전자공학과", "기계공학과", "경영학과"};
            
            for (int i = 0; i < departments.length; i++) {
                // when
                User user = User.builder()
                        .userNumber(20230800 + i)
                        .userName("학생" + i)
                        .department(departments[i])
                        .nickname("student" + i)
                        .password("password" + i)
                        .authorities(Set.of())
                        .build();
                
                // then
                assertThat(user.getDepartment()).isEqualTo(departments[i]);
                assertThat(user.getUserNumber()).isEqualTo(20230800 + i);
            }
        }
    }
}