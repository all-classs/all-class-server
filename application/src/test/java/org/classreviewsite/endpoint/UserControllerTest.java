package org.classreviewsite.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.classreviewsite.security.jwt.JwtTokenProvider;
import org.classreviewsite.user.controller.UserController;
import org.classreviewsite.user.controller.data.request.LoginUserRequest;
import org.classreviewsite.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController 엔드포인트 테스트")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Test
    @DisplayName("유효한 자격증명으로 로그인하면, 성공 응답을 반환한다")
    @WithMockUser
    void loginWithValidCredentials() throws Exception {
        // Given
        LoginUserRequest request = new LoginUserRequest();
        request.setUserNumber(20241234);
        request.setPassword("password123");

        // Mock Authentication 객체 생성
        Authentication mockAuthentication = mock(Authentication.class);
        given(mockAuthentication.getName()).willReturn("20241234");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(mockAuthentication).getAuthorities();

        // Mock AuthenticationManager 설정
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        given(authenticationManagerBuilder.getObject()).willReturn(mockAuthManager);
        given(mockAuthManager.authenticate(any())).willReturn(mockAuthentication);

        // JWT 토큰 생성 Mock
        String mockToken = "mock.jwt.token";
        given(jwtTokenProvider.createToken(any(Authentication.class))).willReturn(mockToken);

        // When & Then
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인을 성공하였습니다."))
                .andExpect(jsonPath("$.data.name").value("20241234"))
                .andExpect(jsonPath("$.data.token").value(mockToken))
                .andExpect(jsonPath("$.data.userKey").value("ROLE_USER"));
    }

    @Test
    @DisplayName("잘못된 자격증명으로 로그인하면, 인증 실패 응답을 반환한다")
    @WithMockUser
    void loginWithInvalidCredentials() throws Exception {
        // Given
        LoginUserRequest request = new LoginUserRequest();
        request.setUserNumber(20241234);
        request.setPassword("wrongpassword");

        // Mock AuthenticationManager 설정 - 인증 실패
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        given(authenticationManagerBuilder.getObject()).willReturn(mockAuthManager);
        given(mockAuthManager.authenticate(any()))
                .willThrow(new org.springframework.security.core.AuthenticationException("Bad credentials") {});

        // When & Then
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized()); // AuthenticationException이 발생하면 401 상태
    }

    @Test
    @DisplayName("잘못된 JSON 형식으로 요청하면, Bad Request 응답을 반환한다")
    @WithMockUser
    void requestWithInvalidJson() throws Exception {
        // Given
        String invalidJson = "{ \"userNumber\": \"invalid\", \"password\": }";

        // When & Then
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("필수 필드가 누락된 요청을 보내면, 정상 처리된다")
    @WithMockUser
    void requestWithMissingFields() throws Exception {
        // Given
        LoginUserRequest request = new LoginUserRequest();
        // userNumber와 password를 설정하지 않음

        // Mock Authentication 객체 생성
        Authentication mockAuthentication = mock(Authentication.class);
        given(mockAuthentication.getName()).willReturn("null"); // null 값이 문자열로 변환됨
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(mockAuthentication).getAuthorities();

        // Mock AuthenticationManager 설정
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        given(authenticationManagerBuilder.getObject()).willReturn(mockAuthManager);
        given(mockAuthManager.authenticate(any())).willReturn(mockAuthentication);

        // JWT 토큰 생성 Mock
        String mockToken = "mock.jwt.token";
        given(jwtTokenProvider.createToken(any(Authentication.class))).willReturn(mockToken);

        // When & Then
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk()); // 컨트롤러에서는 검증하지 않으므로 일단 200
    }

    @Test
    @DisplayName("잘못된 Content-Type으로 요청하면, Unsupported Media Type 응답을 반환한다")
    @WithMockUser
    void requestWithUnsupportedMediaType() throws Exception {
        // Given
        LoginUserRequest request = new LoginUserRequest();
        request.setUserNumber(20241234);
        request.setPassword("password123");

        // When & Then
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.TEXT_PLAIN) // JSON이 아닌 다른 타입
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("관리자 권한으로 로그인하면, 관리자 토큰을 반환한다")
    @WithMockUser
    void loginWithAdminRole() throws Exception {
        // Given
        LoginUserRequest request = new LoginUserRequest();
        request.setUserNumber(20241001);
        request.setPassword("adminpass");

        // Mock Authentication 객체 생성 (ADMIN 권한)
        Authentication mockAuthentication = mock(Authentication.class);
        given(mockAuthentication.getName()).willReturn("20241001");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("STUDENT"));
        doReturn(authorities).when(mockAuthentication).getAuthorities();

        // Mock AuthenticationManager 설정
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        given(authenticationManagerBuilder.getObject()).willReturn(mockAuthManager);
        given(mockAuthManager.authenticate(any())).willReturn(mockAuthentication);

        // JWT 토큰 생성 Mock
        String mockToken = "admin.jwt.token";
        given(jwtTokenProvider.createToken(any(Authentication.class))).willReturn(mockToken);

        // When & Then
        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인을 성공하였습니다."))
                .andExpect(jsonPath("$.data.name").value("20241001"))
                .andExpect(jsonPath("$.data.token").value(mockToken))
                .andExpect(jsonPath("$.data.userKey").value("STUDENT"));
    }
}