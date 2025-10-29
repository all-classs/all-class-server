package org.classreviewsite.service;

import org.classreviewsite.lecture.infrastructure.ImageUrl;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.lecture.service.LectureDataService;
import org.classreviewsite.review.controller.data.Response.ClassListResponse;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.classreviewsite.review.service.ClassListAndDetailService;
import org.classreviewsite.review.service.ImageUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClassListAndDetailService 테스트")
class ClassListAndDetailServiceTest {

    @InjectMocks
    ClassListAndDetailService classListAndDetailService;

    @Mock
    LectureDataService lectureDataService;

    @Mock
    ImageUrlService imageUrlService;

    private Lecture createTestLecture(Long id, String name, String university) {
        return new Lecture(id, name, StarRating.createRatingBuilder(),
                "소프트웨어학과", university, "김교수", LectureType.전공필수);
    }

    @Nested
    @DisplayName("대학별 강의 목록 조회 테스트")
    class GetTest {

        @Test
        @DisplayName("대학명으로 강의 목록을 조회한다")
        void getSuccess() {
            // given
            String university = "한국대학교";
            Lecture lecture1 = createTestLecture(1L, "자바프로그래밍", university);
            Lecture lecture2 = createTestLecture(2L, "데이터베이스", university);
            List<Lecture> lectures = Arrays.asList(lecture1, lecture2);

            given(lectureDataService.findByUniversity(university)).willReturn(lectures);

            // when
            List<ClassListResponse> result = classListAndDetailService.get(university);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("강의가 없는 대학 조회 시, NoSuchElementException을 발생한다")
        void getWhenEmpty() {
            // given
            String university = "존재하지않는대학교";
            given(lectureDataService.findByUniversity(university)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.get(university))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 학교의 강의가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("단일 강의만 있는 대학도 정상 조회한다")
        void getWithSingleLecture() {
            // given
            String university = "한국대학교";
            Lecture lecture = createTestLecture(1L, "자바프로그래밍", university);
            List<Lecture> lectures = Arrays.asList(lecture);

            given(lectureDataService.findByUniversity(university)).willReturn(lectures);

            // when
            List<ClassListResponse> result = classListAndDetailService.get(university);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("null 대학명으로 조회 시, NoSuchElementException을 발생한다")
        void getWithNullUniversity() {
            // given
            given(lectureDataService.findByUniversity(null)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.get(null))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("빈 문자열 대학명으로 조회 시, NoSuchElementException을 발생한다")
        void getWithEmptyUniversity() {
            // given
            String university = "";
            given(lectureDataService.findByUniversity(university)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.get(university))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("대소문자가 다른 대학명으로 조회 시, 빈 결과를 반환한다")
        void getWithDifferentCase() {
            // given
            String university = "한국대학교";
            String differentCase = "KOREAN대학교";
            given(lectureDataService.findByUniversity(differentCase)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.get(differentCase))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("여러 강의를 가진 대학을 조회할 수 있다")
        void getWithMultipleLectures() {
            // given
            String university = "한국대학교";
            Lecture lecture1 = createTestLecture(1L, "자바프로그래밍", university);
            Lecture lecture2 = createTestLecture(2L, "데이터베이스", university);
            Lecture lecture3 = createTestLecture(3L, "운영체제", university);
            Lecture lecture4 = createTestLecture(4L, "알고리즘", university);
            List<Lecture> lectures = Arrays.asList(lecture1, lecture2, lecture3, lecture4);

            given(lectureDataService.findByUniversity(university)).willReturn(lectures);

            // when
            List<ClassListResponse> result = classListAndDetailService.get(university);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(4);
        }
    }

    @Nested
    @DisplayName("강의 상세 정보 조회 테스트")
    class DetailTest {

        @Test
        @DisplayName("강의 ID로 상세 정보를 조회한다")
        void detailSuccess() {
            // given
            Long lectureId = 1L;
            Lecture lecture = createTestLecture(lectureId, "자바프로그래밍", "한국대학교");
            ImageUrl imageUrl = new ImageUrl(1L, "https://example.com/image.jpg");

            given(lectureDataService.findById(lectureId)).willReturn(lecture);
            given(imageUrlService.findById(1L)).willReturn(imageUrl);

            // when
            ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail result = 
                    classListAndDetailService.detail(lectureId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getLectureId()).isEqualTo(lectureId);
            assertThat(result.getLectureName()).isEqualTo("자바프로그래밍");
            assertThat(result.getUniversity()).isEqualTo("한국대학교");
            assertThat(result.getProfileImage()).isEqualTo("https://example.com/image.jpg");
        }

        @Test
        @DisplayName("존재하지 않는 강의 ID로 조회 시, 예외가 발생한다")
        void detailNotFound() {
            // given
            Long lectureId = 999L;
            given(lectureDataService.findById(lectureId))
                    .willThrow(new NoSuchElementException("강의를 찾을 수 없습니다."));

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.detail(lectureId))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("null 강의 ID로 조회 시, 예외가 발생한다")
        void detailWithNullId() {
            // given
            given(lectureDataService.findById(null))
                    .willThrow(new IllegalArgumentException("강의 ID가 null입니다."));

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.detail(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("음수 강의 ID로 조회 시, 예외가 발생한다")
        void detailWithNegativeId() {
            // given
            Long lectureId = -1L;
            given(lectureDataService.findById(lectureId))
                    .willThrow(new IllegalArgumentException("유효하지 않은 강의 ID입니다."));

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.detail(lectureId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("이미지가 없는 경우에도 강의 정보는 조회된다")
        void detailWithMissingImage() {
            // given
            Long lectureId = 1L;
            Lecture lecture = createTestLecture(lectureId, "자바프로그래밍", "한국대학교");

            given(lectureDataService.findById(lectureId)).willReturn(lecture);
            given(imageUrlService.findById(1L))
                    .willThrow(new NoSuchElementException("이미지를 찾을 수 없습니다."));

            // when & then
            assertThatThrownBy(() -> classListAndDetailService.detail(lectureId))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }
}