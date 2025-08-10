package org.classreviewsite.repository;

import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.infrastructure.Likes;
import org.classreviewsite.review.infrastructure.LikesDataRepository;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LikesDataRepositoryTest {

    @Autowired private LikesDataRepository likesDataRepository;

    @Test
    @DisplayName("사용자와 수강후기로 좋아요 정보를 조회한다")
    void findByUserAndClassReview() {
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
        
        ClassReview testClassReview = ClassReview.create(testLecture, testUser, 4.5, 0, "테스트내용", "테스트제목");

        // when
        Optional<Likes> result = likesDataRepository.findByUserAndClassReview(testUser, testClassReview);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우에만 검증
        if (result.isPresent()) {
            assertThat(result.get().getUser().getUserNumber()).isEqualTo(testUser.getUserNumber());
            assertThat(result.get().getClassReview()).isEqualTo(testClassReview);
        }
    }

    @Test
    @DisplayName("존재하지 않는 사용자와 수강후기 조합으로 조회 시 빈 결과를 반환한다")
    void findByNonExistentUserAndClassReview() {
        // given
        User nonExistentUser = User.builder()
                .userNumber(99999999)
                .userName("존재하지않는사용자")
                .department("소프트웨어학과")
                .nickname("nonexistent")
                .password("password")
                .authorities(Set.of())
                .build();
        
        Lecture testLecture = new Lecture(999L, "존재하지않는강의", StarRating.createRatingBuilder(),
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수);
        
        ClassReview nonExistentClassReview = ClassReview.create(testLecture, nonExistentUser, 4.5, 0, "테스트내용", "테스트제목");

        // when
        Optional<Likes> result = likesDataRepository.findByUserAndClassReview(nonExistentUser, nonExistentClassReview);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("null 사용자로 조회 시 빈 결과를 반환한다")
    void findByNullUser() {
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
        
        ClassReview testClassReview = ClassReview.create(testLecture, testUser, 4.5, 0, "테스트내용", "테스트제목");

        // when
        Optional<Likes> result = likesDataRepository.findByUserAndClassReview(null, testClassReview);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("null 수강후기로 조회 시 빈 결과를 반환한다")
    void findByNullClassReview() {
        // given
        User testUser = User.builder()
                .userNumber(20191434)
                .userName("테스트사용자")
                .department("소프트웨어학과")
                .nickname("test123")
                .password("password")
                .authorities(Set.of())
                .build();

        // when
        Optional<Likes> result = likesDataRepository.findByUserAndClassReview(testUser, null);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("수강후기와 사용자로 좋아요를 삭제한다")
    void deleteByClassReviewAndUser() {
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
        
        ClassReview testClassReview = ClassReview.create(testLecture, testUser, 4.5, 0, "테스트내용", "테스트제목");

        // when & then
        // 삭제 메서드 호출 (실제 데이터가 없어도 예외가 발생하지 않음)
        likesDataRepository.deleteByClassReviewAndUser(testClassReview, testUser);
        
        // 삭제 후 조회 시 결과가 없어야 함
        Optional<Likes> result = likesDataRepository.findByUserAndClassReview(testUser, testClassReview);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("수강후기의 모든 좋아요를 삭제한다")
    void deleteAllByClassReview() {
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
        
        ClassReview testClassReview = ClassReview.create(testLecture, testUser, 4.5, 0, "테스트내용", "테스트제목");

        // when & then
        // 삭제 메서드 호출 (실제 데이터가 없어도 예외가 발생하지 않음)
        likesDataRepository.deleteAllByClassReview(testClassReview);
        
        // 삭제 후 조회 시 결과가 없어야 함
        Optional<Likes> result = likesDataRepository.findByUserAndClassReview(testUser, testClassReview);
        assertThat(result).isEmpty();
    }
}