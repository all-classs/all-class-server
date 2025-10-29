package org.classreviewsite.service;

import org.classreviewsite.handler.exception.ReviewNotFoundException;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.ClassReviewDataRepository;
import org.classreviewsite.review.service.ReviewDataService;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewDataService 테스트")
class ReviewDataServiceTest {

    @InjectMocks
    ReviewDataService reviewDataService;

    @Mock
    ClassReviewDataRepository classReviewDataRepository;

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
    @DisplayName("전체 리뷰 조회 테스트")
    class GetAllTest {

        @Test
        @DisplayName("강의 ID로 전체 리뷰 조회 시, 리뷰 목록을 반환한다")
        void getAllSuccess() {
            // given
            Long lectureId = 1L;
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");

            ClassReview review1 = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");
            ClassReview review2 = ClassReview.create(lecture, user, 3.5, "보통 강의", "그저 그래요");

            List<ClassReview> reviews = Arrays.asList(review1, review2);

            given(classReviewDataRepository.findAll(lectureId)).willReturn(reviews);

            // when
            List<ClassReview> result = reviewDataService.getAll(lectureId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(review1, review2);
        }

        @Test
        @DisplayName("리뷰가 없을 경우, ReviewNotFoundException을 발생한다")
        void getAllWhenEmpty() {
            // given
            Long lectureId = 1L;
            given(classReviewDataRepository.findAll(lectureId)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> reviewDataService.getAll(lectureId))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessage("수강 후기가 어디에도 없습니다.");
        }

        @Test
        @DisplayName("null 강의 ID로 조회 시, 적절히 처리한다")
        void getAllWithNullLectureId() {
            // given
            given(classReviewDataRepository.findAll(null)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> reviewDataService.getAll(null))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("리뷰 ID로 조회 테스트")
    class GetReviewByIdTest {

        @Test
        @DisplayName("존재하는 리뷰 ID로 조회 시, 리뷰를 반환한다")
        void getReviewByIdSuccess() {
            // given
            Long reviewId = 1L;
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview review = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            given(classReviewDataRepository.findById(reviewId)).willReturn(Optional.of(review));

            // when
            ClassReview result = reviewDataService.getReviewById(reviewId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getPostTitle()).isEqualTo("추천합니다");
            assertThat(result.getStarLating()).isEqualTo(4.5);
        }

        @Test
        @DisplayName("존재하지 않는 리뷰 ID로 조회 시, NoSuchElementException을 발생한다")
        void getReviewByIdNotFound() {
            // given
            Long reviewId = 999L;
            given(classReviewDataRepository.findById(reviewId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewDataService.getReviewById(reviewId))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 수강후기가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("null 리뷰 ID로 조회 시, NoSuchElementException을 발생한다")
        void getReviewByIdWithNull() {
            // given
            given(classReviewDataRepository.findById(null)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewDataService.getReviewById(null))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("리뷰 저장 테스트")
    class SaveTest {

        @Test
        @DisplayName("리뷰를 정상적으로 저장한다")
        void saveSuccess() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview review = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            // when
            reviewDataService.save(review);

            // then
            verify(classReviewDataRepository).save(review);
        }

        @Test
        @DisplayName("null 리뷰 저장 시, 예외가 발생한다")
        void saveWithNull() {
            // when & then
            assertThatThrownBy(() -> reviewDataService.save(null))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("리뷰 ID와 사용자 번호로 조회 테스트")
    class FindByReviewIdAndUserNumberTest {

        @Test
        @DisplayName("리뷰 ID와 사용자 번호로 리뷰를 조회한다")
        void findByReviewIdAndUserNumberSuccess() {
            // given
            Long reviewId = 1L;
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview review = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            given(classReviewDataRepository.findByReviewIdAndUserNumber(reviewId, user))
                    .willReturn(Optional.of(review));

            // when
            ClassReview result = reviewDataService.findByReviewIdAndUserNumber(reviewId, user);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getUserNumber()).isEqualTo(user);
        }

        @Test
        @DisplayName("존재하지 않는 리뷰 조회 시, NullPointerException을 발생한다")
        void findByReviewIdAndUserNumberNotFound() {
            // given
            Long reviewId = 999L;
            User user = createTestUser(20230857);

            given(classReviewDataRepository.findByReviewIdAndUserNumber(reviewId, user))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewDataService.findByReviewIdAndUserNumber(reviewId, user))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("해당 수강후기가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("리뷰 삭제 테스트")
    class DeleteByIdTest {

        @Test
        @DisplayName("리뷰 ID로 리뷰를 삭제한다")
        void deleteByIdSuccess() {
            // given
            Long reviewId = 1L;

            // when
            reviewDataService.deleteById(reviewId);

            // then
            verify(classReviewDataRepository).deleteById(reviewId);
        }

        @Test
        @DisplayName("null ID로 삭제 시, 예외가 발생한다")
        void deleteByIdWithNull() {
            // when & then
            assertThatThrownBy(() -> reviewDataService.deleteById(null))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("사용자 번호로 리뷰 조회 테스트")
    class GetReviewsByUserNumberTest {

        @Test
        @DisplayName("사용자 번호로 리뷰 목록을 조회한다")
        void getReviewsByUserNumberSuccess() {
            // given
            int userNumber = 20230857;
            User user = createTestUser(userNumber);
            Lecture lecture1 = createTestLecture(1L, "자바프로그래밍");
            Lecture lecture2 = createTestLecture(2L, "데이터베이스");

            ClassReview review1 = ClassReview.create(lecture1, user, 4.5, "좋은 강의1", "추천1");
            ClassReview review2 = ClassReview.create(lecture2, user, 3.5, "좋은 강의2", "추천2");

            List<ClassReview> reviews = Arrays.asList(review1, review2);

            given(classReviewDataRepository.findByUserNumber(userNumber)).willReturn(reviews);

            // when
            List<ClassReview> result = reviewDataService.getReviewsByUserNumber(userNumber);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(review1, review2);
        }

        @Test
        @DisplayName("리뷰가 없는 사용자 조회 시, ReviewNotFoundException을 발생한다")
        void getReviewsByUserNumberWhenEmpty() {
            // given
            int userNumber = 99999999;
            given(classReviewDataRepository.findByUserNumber(userNumber)).willReturn(List.of());

            // when & then
            assertThatThrownBy(() -> reviewDataService.getReviewsByUserNumber(userNumber))
                    .isInstanceOf(ReviewNotFoundException.class)
                    .hasMessage("수강 후기가 어디에도 없습니다.");
        }
    }

    @Nested
    @DisplayName("사용자와 강의로 리뷰 조회 테스트")
    class GetReviewByUserNumberAndLectureIdTest {

        @Test
        @DisplayName("사용자와 강의로 리뷰를 조회한다")
        void getReviewByUserNumberAndLectureIdSuccess() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");
            ClassReview review = ClassReview.create(lecture, user, 4.5, "좋은 강의", "추천합니다");

            given(classReviewDataRepository.findByUserNumberAndLecId(user, lecture))
                    .willReturn(Optional.of(review));

            // when
            Optional<ClassReview> result = reviewDataService.getReviewByUserNumberAndLectureId(user, lecture);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(review);
        }

        @Test
        @DisplayName("리뷰가 없을 경우, Optional.empty()를 반환한다")
        void getReviewByUserNumberAndLectureIdWhenEmpty() {
            // given
            User user = createTestUser(20230857);
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");

            given(classReviewDataRepository.findByUserNumberAndLecId(user, lecture))
                    .willReturn(Optional.empty());

            // when
            Optional<ClassReview> result = reviewDataService.getReviewByUserNumberAndLectureId(user, lecture);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("null 사용자로 조회 시, Optional.empty()를 반환한다")
        void getReviewByUserNumberAndLectureIdWithNullUser() {
            // given
            Lecture lecture = createTestLecture(1L, "자바프로그래밍");

            given(classReviewDataRepository.findByUserNumberAndLecId(null, lecture))
                    .willReturn(Optional.empty());

            // when
            Optional<ClassReview> result = reviewDataService.getReviewByUserNumberAndLectureId(null, lecture);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("null 강의로 조회 시, Optional.empty()를 반환한다")
        void getReviewByUserNumberAndLectureIdWithNullLecture() {
            // given
            User user = createTestUser(20230857);

            given(classReviewDataRepository.findByUserNumberAndLecId(user, null))
                    .willReturn(Optional.empty());

            // when
            Optional<ClassReview> result = reviewDataService.getReviewByUserNumberAndLectureId(user, null);

            // then
            assertThat(result).isEmpty();
        }
    }
}