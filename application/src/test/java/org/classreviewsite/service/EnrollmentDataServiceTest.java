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
import org.classreviewsite.lecture.service.EnrollmentDataService;
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
@DisplayName("EnrollmentDataService 테스트")
class EnrollmentDataServiceTest {

    @InjectMocks
    EnrollmentDataService enrollmentDataService;

    @Mock
    EnrollmentDataRepository enrollmentDataRepository;

    private User createTestUser(int userNumber) {
        return User.builder()
                .userNumber(userNumber)
                .userName("홍길동")
                .department("소프트웨어학과")
                .nickname("hong123")
                .password("password")
                .authorities(Set.of())
                .build();
    }

    private Lecture createTestLecture(Long id, String name) {
        return new Lecture(id, name, StarRating.createRatingBuilder(),
                "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수);
    }

    @Nested
    @DisplayName("학생 수강 정보 조회 테스트")
    class FindClassForSemesterTest {

        @Test
        @DisplayName("존재하는 학번으로 수강 정보 조회 시, 수강 목록을 반환한다")
        void findClassForSemesterSuccess() {
            // given
            int userNumber = 20230857;
            User user = createTestUser(userNumber);
            Lecture lecture1 = createTestLecture(1L, "자바프로그래밍");
            Lecture lecture2 = createTestLecture(2L, "데이터베이스");

            Enrollment enrollment1 = new Enrollment(1L, user, lecture1);
            Enrollment enrollment2 = new Enrollment(2L, user, lecture2);

            List<Enrollment> enrollments = Arrays.asList(enrollment1, enrollment2);

            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.of(enrollments));

            // when
            List<EnrollmentResponse> result = enrollmentDataService.findClassForSemester(userNumber);

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
            assertThatThrownBy(() -> enrollmentDataService.findClassForSemester(userNumber))
                    .isInstanceOf(EnrollmentNotFoundException.class)
                    .hasMessage("해당 학생의 수강 정보가 없습니다.");
        }

        @Test
        @DisplayName("빈 수강 목록일 경우, UserNotFoundException을 발생한다")
        void findClassForSemesterEmptyList() {
            // given
            int userNumber = 20230857;
            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.of(List.of()));

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findClassForSemester(userNumber))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("해당 학생이 수강한 강의는 없습니다.");
        }

        @Test
        @DisplayName("단일 수강 정보 조회도 정상 동작한다")
        void findClassForSemesterSingleEnrollment() {
            // given
            int userNumber = 20230857;
            User user = createTestUser(userNumber);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            Enrollment enrollment = new Enrollment(1L, user, lecture);

            List<Enrollment> enrollments = Arrays.asList(enrollment);

            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.of(enrollments));

            // when
            List<EnrollmentResponse> result = enrollmentDataService.findClassForSemester(userNumber);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("0 학번으로 조회 시, EnrollmentNotFoundException을 발생한다")
        void findClassForSemesterWithZeroUserNumber() {
            // given
            int userNumber = 0;
            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findClassForSemester(userNumber))
                    .isInstanceOf(EnrollmentNotFoundException.class);
        }

        @Test
        @DisplayName("음수 학번으로 조회 시, EnrollmentNotFoundException을 발생한다")
        void findClassForSemesterWithNegativeUserNumber() {
            // given
            int userNumber = -1;
            given(enrollmentDataRepository.findByUserNumber_UserNumber(userNumber))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findClassForSemester(userNumber))
                    .isInstanceOf(EnrollmentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("학번과 강의명으로 수강 정보 조회 테스트")
    class FindByUserNumberTest {

        @Test
        @DisplayName("학번과 강의명으로 수강 정보를 조회한다")
        void findByUserNumberSuccess() {
            // given
            int userNumber = 20230857;
            String lectureName = "자바프로그래밍";
            User user = createTestUser(userNumber);
            Lecture lecture = createTestLecture(1L, lectureName);
            Enrollment enrollment = new Enrollment(1L, user, lecture);

            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.of(enrollment));

            // when
            Enrollment result = enrollmentDataService.findByUserNumber(userNumber, lectureName);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getUserNumber().getUserNumber()).isEqualTo(userNumber);
            assertThat(result.getLecture().getLectureName()).isEqualTo(lectureName);
        }

        @Test
        @DisplayName("존재하지 않는 수강 정보 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberNotFound() {
            // given
            int userNumber = 20230857;
            String lectureName = "존재하지않는강의";

            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findByUserNumber(userNumber, lectureName))
                    .isInstanceOf(NoPermissionReviewException.class);
        }

        @Test
        @DisplayName("null 강의명으로 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberWithNullLectureName() {
            // given
            int userNumber = 20230857;

            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, null))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findByUserNumber(userNumber, null))
                    .isInstanceOf(NoPermissionReviewException.class);
        }

        @Test
        @DisplayName("빈 문자열 강의명으로 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberWithEmptyLectureName() {
            // given
            int userNumber = 20230857;
            String lectureName = "";

            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findByUserNumber(userNumber, lectureName))
                    .isInstanceOf(NoPermissionReviewException.class);
        }

        @Test
        @DisplayName("대소문자가 다른 강의명으로 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberWithDifferentCase() {
            // given
            int userNumber = 20230857;
            String lectureName = "JAVA프로그래밍";

            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findByUserNumber(userNumber, lectureName))
                    .isInstanceOf(NoPermissionReviewException.class);
        }

        @Test
        @DisplayName("다른 학생이 수강한 강의 조회 시, NoPermissionReviewException을 발생한다")
        void findByUserNumberWithDifferentStudent() {
            // given
            int userNumber = 20230858;
            String lectureName = "자바프로그래밍";

            given(enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureName(userNumber, lectureName))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> enrollmentDataService.findByUserNumber(userNumber, lectureName))
                    .isInstanceOf(NoPermissionReviewException.class);
        }
    }
}