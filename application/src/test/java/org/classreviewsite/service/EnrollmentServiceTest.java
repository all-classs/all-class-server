package org.classreviewsite.service;

import org.classreviewsite.handler.exception.EnrollmentNotFoundException;
import org.classreviewsite.handler.exception.NoPermissionReviewException;
import org.classreviewsite.handler.exception.UserNotFoundException;
import org.classreviewsite.lecture.controller.data.response.EnrollmentResponse;
import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.classreviewsite.lecture.infrastructure.EnrollmentDataRepository;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.lecture.service.EnrollmentService;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @InjectMocks EnrollmentService enrollmentService;
    @Mock EnrollmentDataRepository enrollmentDataRepository;

    @Nested
    @DisplayName("학생 수강 정보 조회 테스트")
    class findClassForSemesterTest {
        
        @Test
        @DisplayName("존재하는 학번으로 수강 정보 조회 시, 수강 목록을 반환한다")
        void findClassForSemesterSuccess() {
            // given
            int userNumber = 20230857;
            User user = User.builder()
                    .userNumber(userNumber)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture1 = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            Lecture lecture2 = new Lecture(2L, "데이터베이스", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", "한국대학교", "이교수", LectureType.전공선택);
            
            Enrollment enrollment1 = new Enrollment(1L, user, lecture1);
            Enrollment enrollment2 = new Enrollment(2L, user, lecture2);
            
            List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);
            
            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.of(enrollments));
            
            // when
            List<EnrollmentResponse> result = enrollmentService.findClassForSemester(userNumber);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
        }
        
        @Test
        @DisplayName("수강 정보가 없는 학번으로 조회 시, EnrollmentNotFoundException을 발생한다")
        void findClassForSemesterNotFound() {
            // given
            int userNumber = 99999999;
            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> enrollmentService.findClassForSemester(userNumber))
                    .isInstanceOf(EnrollmentNotFoundException.class)
                    .hasMessage("해당 학생의 수강 정보가 없습니다.");
        }
        
        @Test
        @DisplayName("수강 목록이 비어있는 경우, UserNotFoundException을 발생한다")
        void findClassForSemesterEmptyList() {
            // given
            int userNumber = 20230857;
            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.of(List.of()));
            
            // when & then
            assertThatThrownBy(() -> enrollmentService.findClassForSemester(userNumber))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("해당 학생이 수강한 강의는 없습니다.");
        }
        
        @Test
        @DisplayName("여러 강의를 수강한 학생의 정보 조회 시, 모든 수강 정보를 반환한다")
        void findClassForSemesterMultipleLectures() {
            // given
            int userNumber = 20230857;
            User user = User.builder()
                    .userNumber(userNumber)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture1 = new Lecture(1L, "자바프로그래밍", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            Lecture lecture2 = new Lecture(2L, "데이터베이스", StarRating.createRatingBuilder(),
                                         "소프트웨어학과", "한국대학교", "이교수", LectureType.전공선택);
            Lecture lecture3 = new Lecture(3L, "영어회화", StarRating.createRatingBuilder(),
                                         "외국어학과", "한국대학교", "박교수", LectureType.교양선택);
            
            Enrollment enrollment1 = new Enrollment(1L, user, lecture1);
            Enrollment enrollment2 = new Enrollment(2L, user, lecture2);
            Enrollment enrollment3 = new Enrollment(3L, user, lecture3);
            
            List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2, enrollment3);
            
            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.of(enrollments));
            
            // when
            List<EnrollmentResponse> result = enrollmentService.findClassForSemester(userNumber);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(3);
        }
    }

    @Nested
    @DisplayName("특정 강의 수강 여부 확인 테스트")
    class findByUserNumberTest {
        
        @Test
        @DisplayName("수강한 강의로 조회 시, 수강 정보를 반환한다")
        void findByUserNumberSuccess() {
            // given
            int userNumber = 20230857;
            String lectureName = "자바프로그래밍";
            
            User user = User.builder()
                    .userNumber(userNumber)
                    .userName("홍길동")
                    .department("소프트웨어학과")
                    .nickname("hong123")
                    .password("password")
                    .authorities(Set.of())
                    .build();
            
            Lecture lecture = new Lecture(1L, lectureName, StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
            
            Enrollment enrollment = new Enrollment(1L, user, lecture);
            
            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.of(enrollment));
            
            // when
            Enrollment result = enrollmentService.findByUserNumber(userNumber, lectureName);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getUserNumber().getUserNumber()).isEqualTo(userNumber);
            assertThat(result.getLecture().getLectureName()).isEqualTo(lectureName);
        }
        
        @Test
        @DisplayName("수강하지 않은 강의로 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberNotEnrolled() {
            // given
            int userNumber = 20230857;
            String lectureName = "수강하지않은강의";
            
            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> enrollmentService.findByUserNumber(userNumber, lectureName))
                    .isInstanceOf(NoPermissionReviewException.class)
                    .hasMessage("");
        }
        
        @Test
        @DisplayName("존재하지 않는 학번으로 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberInvalidUser() {
            // given
            int userNumber = 99999999;
            String lectureName = "자바프로그래밍";
            
            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> enrollmentService.findByUserNumber(userNumber, lectureName))
                    .isInstanceOf(NoPermissionReviewException.class)
                    .hasMessage("");
        }
        
        @Test
        @DisplayName("null 강의명으로 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberWithNullLecture() {
            // given
            int userNumber = 20230857;
            String lectureName = null;
            
            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> enrollmentService.findByUserNumber(userNumber, lectureName))
                    .isInstanceOf(NoPermissionReviewException.class)
                    .hasMessage("");
        }
    }
}