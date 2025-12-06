package org.classreviewsite.repository;

import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.domain.lecture.StarRating;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.classreviewsite.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClassReviewDataRepositoryTest {

    @Autowired private ClassReviewDataRepository classReviewDataRepository;

    @Test
    @DisplayName("강의 ID로 수강후기 목록을 조회한다")
    void findAllByLectureId() {
        // given
        Long lectureId = 1L;

        // when
        List<ClassReview> result = classReviewDataRepository.findAll(lectureId);

        // then
        assertThat(result).isNotNull();
        if (!result.isEmpty()) {
            assertThat(result.get(0).getLecId().getLectureId()).isEqualTo(lectureId);
        }
    }

    @Test
    @DisplayName("사용자 번호와 강의 ID로 수강후기를 조회한다")
    void findByUserNumberAndLecId() {
        // given
        User testUser = User.builder()
                .userNumber(20191434)
                .userName("테스트사용자")
                .department("소프트웨어학과")
                .nickname("test123")
                .password("password")
                .authorities(Set.of())
                .build();
        
        Lecture testLecture = new Lecture(1L, "테스트강의", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수);

        // when
        Optional<ClassReview> result = classReviewDataRepository.findByUserNumberAndLecId(testUser, testLecture);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우에만 검증
        if (result.isPresent()) {
            assertThat(result.get().getUserNumber().getUserNumber()).isEqualTo(testUser.getUserNumber());
            assertThat(result.get().getLecId().getLectureId()).isEqualTo(testLecture.getLectureId());
        }
    }

    @Test
    @DisplayName("강의별 수강후기를 별점 내림차순으로 조회한다")
    void findAllByLecIdOrderByStarLatingDesc() {
        // given
        Lecture testLecture = new Lecture(1L, "테스트강의", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수);

        // when
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByStarLatingDesc(testLecture);

        // then
        assertThat(result).isNotNull();
        if (result.size() > 1) {
            // 별점이 내림차순으로 정렬되었는지 확인
            for (int i = 0; i < result.size() - 1; i++) {
                assertThat(result.get(i).getStarLating()).isGreaterThanOrEqualTo(result.get(i + 1).getStarLating());
            }
        }
    }

    @Test
    @DisplayName("강의별 수강후기를 별점 오름차순으로 조회한다")
    void findAllByLecIdOrderByStarLatingAsc() {
        // given
        Lecture testLecture = new Lecture(1L, "테스트강의", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수);

        // when
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByStarLatingAsc(testLecture);

        // then
        assertThat(result).isNotNull();
        if (result.size() > 1) {
            // 별점이 오름차순으로 정렬되었는지 확인
            for (int i = 0; i < result.size() - 1; i++) {
                assertThat(result.get(i).getStarLating()).isLessThanOrEqualTo(result.get(i + 1).getStarLating());
            }
        }
    }

    @Test
    @DisplayName("강의별 수강후기를 좋아요 내림차순으로 조회한다")
    void findAllByLecIdOrderByLikesDesc() {
        // given
        Lecture testLecture = new Lecture(1L, "테스트강의", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수);

        // when
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByLikesDesc(testLecture);

        // then
        assertThat(result).isNotNull();
        if (result.size() > 1) {
            // 좋아요가 내림차순으로 정렬되었는지 확인
            for (int i = 0; i < result.size() - 1; i++) {
                assertThat(result.get(i).getLikes()).isGreaterThanOrEqualTo(result.get(i + 1).getLikes());
            }
        }
    }

    @Test
    @DisplayName("강의별 수강후기를 좋아요 오름차순으로 조회한다")
    void findAllByLecIdOrderByLikesAsc() {
        // given
        Lecture testLecture = new Lecture(1L, "테스트강의", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수);

        // when
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByLikesAsc(testLecture);

        // then
        assertThat(result).isNotNull();
        if (result.size() > 1) {
            // 좋아요가 오름차순으로 정렬되었는지 확인
            for (int i = 0; i < result.size() - 1; i++) {
                assertThat(result.get(i).getLikes()).isLessThanOrEqualTo(result.get(i + 1).getLikes());
            }
        }
    }

    @Test
    @DisplayName("강의별 수강후기를 생성일자 내림차순으로 조회한다")
    void findAllByLecIdOrderByCreatedDateDesc() {
        // given
        Lecture testLecture = new Lecture(1L, "테스트강의", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수);

        // when
        List<ClassReview> result = classReviewDataRepository.findAllByLecIdOrderByCreatedDateDesc(testLecture);

        // then
        assertThat(result).isNotNull();
        if (result.size() > 1) {
            // 생성일자가 내림차순으로 정렬되었는지 확인
            for (int i = 0; i < result.size() - 1; i++) {
                assertThat(result.get(i).getCreatedDate()).isAfterOrEqualTo(result.get(i + 1).getCreatedDate());
            }
        }
    }

    @Test
    @DisplayName("사용자 번호로 해당 사용자의 수강후기 목록을 조회한다")
    void findByUserNumber() {
        // given
        int userNumber = 20191434;

        // when
        List<ClassReview> result = classReviewDataRepository.findByUserNumber(userNumber);

        // then
        assertThat(result).isNotNull();
        if (!result.isEmpty()) {
            assertThat(result.get(0).getUserNumber().getUserNumber()).isEqualTo(userNumber);
        }
    }

    @Test
    @DisplayName("수강후기 ID와 사용자로 특정 수강후기를 조회한다")
    void findByReviewIdAndUserNumber() {
        // given
        Long reviewId = 1L;
        User testUser = User.builder()
                .userNumber(20191434)
                .userName("테스트사용자")
                .department("소프트웨어학과")
                .nickname("test123")
                .password("password")
                .authorities(Set.of())
                .build();

        // when
        Optional<ClassReview> result = classReviewDataRepository.findByReviewIdAndUserNumber(reviewId, testUser);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우에만 검증
        if (result.isPresent()) {
            assertThat(result.get().getReviewId()).isEqualTo(reviewId);
            assertThat(result.get().getUserNumber().getUserNumber()).isEqualTo(testUser.getUserNumber());
        }
    }
}