package org.classreviewsite.endpoint;

import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.lecture.controller.ClassController;
import org.classreviewsite.lecture.controller.data.response.EnrollmentResponse;
import org.classreviewsite.lecture.service.EnrollmentDataService;
import org.classreviewsite.review.controller.data.Response.ClassListResponse;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.classreviewsite.review.service.ClassListAndDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassController.class)
@DisplayName("ClassController 엔드포인트 테스트")
class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentDataService enrollmentDataService;

    @MockBean
    private ClassListAndDetailService classListAndDetailService;

    @Nested
    @DisplayName("강의 목록 조회 테스트")
    class classListTest {
        
        @Test
        @DisplayName("대학명으로 강의 목록 조회 시, 성공 응답을 반환한다")
        @WithMockUser
        void getClassListByUniversity() throws Exception {
            // given
            String university = "한국대학교";
            List<ClassListResponse> mockResponse = Arrays.asList(
                createMockClassListResponse(1L, "자바프로그래밍"),
                createMockClassListResponse(2L, "데이터베이스")
            );
            
            given(classListAndDetailService.get(university)).willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/class")
                            .param("university", university))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("전체 강의 목록입니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isNotEmpty());
        }
        
        @Test
        @DisplayName("대학명과 강의ID로 강의 상세 조회 시, 성공 응답을 반환한다")
        @WithMockUser
        void getClassDetailByUniversityAndLectureId() throws Exception {
            // given
            String university = "한국대학교";
            Long lectureId = 1L;
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail mockResponse = 
                createMockClassDetailResponse(lectureId, "자바프로그래밍", "김교수");
            
            given(classListAndDetailService.detail(lectureId)).willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/class")
                            .param("university", university)
                            .param("lectureId", String.valueOf(lectureId)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("강의 상세 정보 조회입니다."))
                    .andExpect(jsonPath("$.data").isNotEmpty());
        }
        
        @Test
        @DisplayName("필수 파라미터 university 없이 요청 시, Bad Request를 반환한다")
        @WithMockUser
        void getClassListWithoutUniversity() throws Exception {
            // when & then
            mockMvc.perform(get("/class"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
        
        @Test
        @DisplayName("존재하지 않는 대학명으로 조회 시, 적절한 에러 응답을 반환한다")
        @WithMockUser
        void getClassListWithNonExistentUniversity() throws Exception {
            // given
            String nonExistentUniversity = "존재하지않는대학교";
            
            given(classListAndDetailService.get(nonExistentUniversity))
                    .willThrow(new java.util.NoSuchElementException("해당 학교의 강의가 존재하지 않습니다."));
            
            // when & then
            mockMvc.perform(get("/class")
                            .param("university", nonExistentUniversity))
                    .andDo(print())
                    .andExpect(status().isUnauthorized()); // 401 상태 코드 기대
        }
        
        @Test
        @DisplayName("빈 문자열 대학명으로 조회 시, Bad Request를 반환한다")
        @WithMockUser
        void getClassListWithEmptyUniversity() throws Exception {
            // when & then
            mockMvc.perform(get("/class")
                            .param("university", ""))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
        
        @Test
        @DisplayName("특수문자가 포함된 대학명으로 조회 시, 정상 처리한다")
        @WithMockUser
        void getClassListWithSpecialCharactersInUniversity() throws Exception {
            // given
            String universityWithSpecialChars = "한국-대학교 (본캠퍼스)";
            List<ClassListResponse> mockResponse = Arrays.asList(
                createMockClassListResponse(1L, "특수강의")
            );
            
            given(classListAndDetailService.get(universityWithSpecialChars)).willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/class")
                            .param("university", universityWithSpecialChars))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200));
        }
    }

    @Nested
    @DisplayName("내 수강 정보 조회 테스트")
    class myClassListTest {
        
        @Test
        @DisplayName("유효한 학번으로 내 수강 정보 조회 시, 성공 응답을 반환한다")
        @WithMockUser
        void getMyClassListSuccess() throws Exception {
            // given
            int userNumber = 20230857;
            List<EnrollmentResponse> mockResponse = Arrays.asList(
                createMockEnrollmentResponse(1L, "자바프로그래밍"),
                createMockEnrollmentResponse(2L, "데이터베이스")
            );
            
            given(enrollmentDataService.findClassForSemester(userNumber)).willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/class/me")
                            .param("userNumber", String.valueOf(userNumber)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("해당 학생의 수강한 강의 목록입니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isNotEmpty());
        }
        
        @Test
        @DisplayName("필수 파라미터 userNumber 없이 요청 시, Bad Request를 반환한다")
        @WithMockUser
        void getMyClassListWithoutUserNumber() throws Exception {
            // when & then
            mockMvc.perform(get("/class/me"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
        
        @Test
        @DisplayName("존재하지 않는 학번으로 조회 시, 적절한 에러 응답을 반환한다")
        @WithMockUser
        void getMyClassListWithNonExistentUserNumber() throws Exception {
            // given
            int nonExistentUserNumber = 99999999;
            
            given(enrollmentDataService.findClassForSemester(nonExistentUserNumber))
                    .willThrow(new org.classreviewsite.handler.exception.EnrollmentNotFoundException("해당 학생의 수강 정보가 없습니다."));
            
            // when & then
            mockMvc.perform(get("/class/me")
                            .param("userNumber", String.valueOf(nonExistentUserNumber)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized()); // 401 상태 코드 기대
        }
        
        @Test
        @DisplayName("잘못된 형식의 학번으로 조회 시, Bad Request를 반환한다")
        @WithMockUser
        void getMyClassListWithInvalidUserNumber() throws Exception {
            // when & then
            mockMvc.perform(get("/class/me")
                            .param("userNumber", "invalid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
        
        @Test
        @DisplayName("음수 학번으로 조회 시, Bad Request를 반환한다")
        @WithMockUser
        void getMyClassListWithNegativeUserNumber() throws Exception {
            // when & then
            mockMvc.perform(get("/class/me")
                            .param("userNumber", "-1"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
        
        @Test
        @DisplayName("8자리 학번으로 조회 시, 정상 처리한다")
        @WithMockUser
        void getMyClassListWithValidEightDigitUserNumber() throws Exception {
            // given
            int validUserNumber = 20240841;
            List<EnrollmentResponse> mockResponse = Arrays.asList(
                createMockEnrollmentResponse(1L, "자바프로그래밍")
            );
            
            given(enrollmentDataService.findClassForSemester(validUserNumber)).willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/class/me")
                            .param("userNumber", String.valueOf(validUserNumber)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200));
        }
        
        @Test
        @DisplayName("수강 정보가 없는 학생의 조회 시, 적절한 응답을 반환한다")
        @WithMockUser
        void getMyClassListWithNoEnrollments() throws Exception {
            // given
            int userNumber = 20230999;
            
            given(enrollmentDataService.findClassForSemester(userNumber))
                    .willThrow(new org.classreviewsite.handler.exception.UserNotFoundException("해당 학생이 수강한 강의는 없습니다."));
            
            // when & then
            mockMvc.perform(get("/class/me")
                            .param("userNumber", String.valueOf(userNumber)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized()); // 401 상태 코드 기대
        }
    }

    // 헬퍼 메서드들
    private ClassListResponse createMockClassListResponse(Long lectureId, String lectureName) {
        return new ClassListResponse(
            lectureId,
            lectureName,
            "소프트웨어학과",
            "한국대학교",
            LectureType.전공필수,
            4.5,
            "김교수"
        );
    }
    
    private ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail createMockClassDetailResponse(
            Long lectureId, String lectureName, String professor) {
        return ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail.builder()
                .lectureId(lectureId)
                .lectureName(lectureName)
                .averageStarLating(4.5)
                .totalStarLating(9.0)
                .reviewCount(2L)
                .department("소프트웨어학과")
                .university("한국대학교")
                .lectureType(LectureType.전공필수)
                .professor(professor)
                .introduction("강의 소개")
                .profileImage("image.jpg")
                .build();
    }
    
    private EnrollmentResponse createMockEnrollmentResponse(Long classNumber, String lectureName) {
        return EnrollmentResponse.builder()
                .classNumber(classNumber)
                .lectureName(lectureName)
                .professorName("김교수")
                .semester("1학기")
                .CompletionType("전공필수")
                .CompletionYear("2023")
                .build();
    }
}