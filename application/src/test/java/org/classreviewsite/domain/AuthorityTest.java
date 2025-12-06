package org.classreviewsite.domain;

import org.classreviewsite.domain.user.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityTest {

    @Nested
    @DisplayName("Authority 엔티티 생성 테스트")
    class createAuthorityTest {
        
        @Test
        @DisplayName("빌더를 사용하여 권한을 생성한다")
        void createAuthorityWithBuilder() {
            // given
            String authorityName = "STUDENT";
            
            // when
            Authority authority = Authority.builder()
                    .authority(authorityName)
                    .build();
            
            // then
            assertThat(authority).isNotNull();
            assertThat(authority.getAuthority()).isEqualTo(authorityName);
        }
        
        @Test
        @DisplayName("모든 인자를 포함한 생성자로 권한을 생성한다")
        void createAuthorityWithAllArgsConstructor() {
            // given
            String authorityName = "ADMIN";
            
            // when
            Authority authority = new Authority(authorityName);
            
            // then
            assertThat(authority).isNotNull();
            assertThat(authority.getAuthority()).isEqualTo(authorityName);
        }
        
        @Test
        @DisplayName("기본 생성자로 권한을 생성한다")
        void createAuthorityWithNoArgsConstructor() {
            // when
            Authority authority = new Authority();
            
            // then
            assertThat(authority).isNotNull();
            assertThat(authority.getAuthority()).isNull();
        }
        
        @Test
        @DisplayName("정적 팩토리 메서드로 권한을 생성한다")
        void createAuthorityWithToEntity() {
            // given
            String authorityType = "TEACHER";
            
            // when
            Authority authority = Authority.toEntity(authorityType);
            
            // then
            assertThat(authority).isNotNull();
            assertThat(authority.getAuthority()).isEqualTo(authorityType);
        }
        
        @Test
        @DisplayName("STUDENT 권한을 생성한다")
        void createStudentAuthority() {
            // given & when
            Authority authority = Authority.toEntity("STUDENT");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("STUDENT");
        }
        
        @Test
        @DisplayName("ADMIN 권한을 생성한다")
        void createAdminAuthority() {
            // given & when
            Authority authority = Authority.toEntity("ADMIN");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("ADMIN");
        }
        
        @Test
        @DisplayName("TEACHER 권한을 생성한다")
        void createTeacherAuthority() {
            // given & when
            Authority authority = Authority.toEntity("TEACHER");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("TEACHER");
        }
        
        @Test
        @DisplayName("USER 권한을 생성한다")
        void createUserAuthority() {
            // given & when
            Authority authority = Authority.toEntity("USER");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("USER");
        }
        
        @Test
        @DisplayName("ROLE_ 접두사가 있는 권한을 생성한다")
        void createRolePrefixedAuthority() {
            // given & when
            Authority authority = Authority.toEntity("ROLE_USER");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("ROLE_USER");
            assertThat(authority.getAuthority()).startsWith("ROLE_");
        }
        
        @Test
        @DisplayName("소문자 권한을 생성한다")
        void createLowercaseAuthority() {
            // given & when
            Authority authority = Authority.toEntity("student");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("student");
        }
        
        @Test
        @DisplayName("대소문자 혼합 권한을 생성한다")
        void createMixedCaseAuthority() {
            // given & when
            Authority authority = Authority.toEntity("Admin");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("Admin");
        }
        
        @Test
        @DisplayName("빈 문자열 권한을 생성한다")
        void createEmptyStringAuthority() {
            // given & when
            Authority authority = Authority.toEntity("");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("");
            assertThat(authority.getAuthority()).isEmpty();
        }
        
        @Test
        @DisplayName("null 권한을 생성한다")
        void createNullAuthority() {
            // given & when
            Authority authority = Authority.toEntity(null);
            
            // then
            assertThat(authority.getAuthority()).isNull();
        }
        
        @Test
        @DisplayName("숫자가 포함된 권한을 생성한다")
        void createAuthorityWithNumbers() {
            // given & when
            Authority authority = Authority.toEntity("LEVEL_1");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("LEVEL_1");
            assertThat(authority.getAuthority()).contains("1");
        }
        
        @Test
        @DisplayName("특수문자가 포함된 권한을 생성한다")
        void createAuthorityWithSpecialCharacters() {
            // given & when
            Authority authority = Authority.toEntity("ADMIN_SUPER");
            
            // then
            assertThat(authority.getAuthority()).isEqualTo("ADMIN_SUPER");
            assertThat(authority.getAuthority()).contains("_");
        }
        
        @Test
        @DisplayName("긴 권한명을 생성한다")
        void createLongAuthorityName() {
            // given
            String longAuthorityName = "VERY_LONG_AUTHORITY_NAME_FOR_TESTING_PURPOSES";
            
            // when
            Authority authority = Authority.toEntity(longAuthorityName);
            
            // then
            assertThat(authority.getAuthority()).isEqualTo(longAuthorityName);
            assertThat(authority.getAuthority().length()).isLessThanOrEqualTo(50); // DB 제약사항 확인
        }
        
        @Test
        @DisplayName("여러 권한을 연속으로 생성한다")
        void createMultipleAuthorities() {
            // given
            String[] authorityTypes = {"STUDENT", "TEACHER", "ADMIN", "USER", "GUEST"};
            
            for (String type : authorityTypes) {
                // when
                Authority authority = Authority.toEntity(type);
                
                // then
                assertThat(authority.getAuthority()).isEqualTo(type);
            }
        }
        
        @Test
        @DisplayName("setter를 사용하여 권한을 수정한다")
        void modifyAuthorityWithSetter() {
            // given
            Authority authority = Authority.toEntity("STUDENT");
            String newAuthority = "ADMIN";
            
            // when
            authority.setAuthority(newAuthority);
            
            // then
            assertThat(authority.getAuthority()).isEqualTo(newAuthority);
            assertThat(authority.getAuthority()).isNotEqualTo("STUDENT");
        }
    }
}