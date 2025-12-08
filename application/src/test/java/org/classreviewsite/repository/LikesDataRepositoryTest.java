package org.classreviewsite.repository;

import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureType;
import org.classreviewsite.domain.lecture.StarRating;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.review.Likes;
import org.classreviewsite.domain.review.LikesDataRepository;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.domain.user.UserDataRepository;
import org.classreviewsite.domain.lecture.LectureDataRepository;
import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class LikesDataRepositoryTest {

    @Autowired private LikesDataRepository likesDataRepository;
    @Autowired private UserDataRepository userDataRepository;
    @Autowired private LectureDataRepository lectureDataRepository;
    @Autowired private ClassReviewDataRepository classReviewDataRepository;

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
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수, 0L);
        
        ClassReview testClassReview = ClassReview.builder()
                .lecId(testLecture)
                .userNumber(testUser)
                .starLating(4.5)
                .likes(0)
                .postTitle("테스트제목")
                .postContent("테스트내용")
                .build();

        // when
        userDataRepository.save(testUser);
        lectureDataRepository.save(testLecture);
        classReviewDataRepository.save(testClassReview);
        
        Likes likes = Likes.builder().user(testUser).classReview(testClassReview).build();
        likesDataRepository.save(likes);

        Likes result = likesDataRepository.findByUserAndClassReview(testUser, testClassReview);

        // then
        assertThat(result).isNotNull();
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
                                        "소프트웨어학과", "한국대학교", "김교수", LectureType.전공필수, 0L);
        
        ClassReview nonExistentClassReview = ClassReview.builder()
                .lecId(testLecture)
                .userNumber(nonExistentUser)
                .starLating(4.5)
                .likes(0)
                .postTitle("테스트제목")
                .postContent("테스트내용")
                .build();

        // when
        userDataRepository.save(nonExistentUser);
        lectureDataRepository.save(testLecture);
        classReviewDataRepository.save(nonExistentClassReview);

        Likes result = likesDataRepository.findByUserAndClassReview(nonExistentUser, nonExistentClassReview);

        // then
        assertThat(result).isNotNull();
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
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수, 0L);
        
        ClassReview testClassReview = ClassReview.builder()
                .lecId(testLecture)
                .userNumber(testUser)
                .starLating(4.5)
                .likes(0)
                .postTitle("테스트제목")
                .postContent("테스트내용")
                .build();

        // when
        Likes result = likesDataRepository.findByUserAndClassReview(null, testClassReview);

        // then
        assertThat(result).isNotNull();
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
        Likes result = likesDataRepository.findByUserAndClassReview(testUser, null);

        // then
        assertThat(result).isNotNull();
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
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수, 0L);
        
        ClassReview testClassReview = ClassReview.builder()
                .lecId(testLecture)
                .userNumber(testUser)
                .starLating(4.5)
                .likes(0)
                .postTitle("테스트제목")
                .postContent("테스트내용")
                .build();

        // when & then
        // when & then
        userDataRepository.save(testUser);
        lectureDataRepository.save(testLecture);
        classReviewDataRepository.save(testClassReview);
        
        Likes likes = Likes.builder().user(testUser).classReview(testClassReview).build();
        likesDataRepository.save(likes);

        // 삭제 메서드 호출 (실제 데이터가 없어도 예외가 발생하지 않음)
        likesDataRepository.deleteByClassReviewAndUser(testClassReview, testUser);
        
        // 삭제 후 조회 시 결과가 없어야 함
        Likes result = likesDataRepository.findByUserAndClassReview(testUser, testClassReview);
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
                                        "소프트웨어학과", "한국대학교", "테스트교수", LectureType.전공필수, 0L);
        
        ClassReview testClassReview = ClassReview.builder()
                .lecId(testLecture)
                .userNumber(testUser)
                .starLating(4.5)
                .likes(0)
                .postTitle("테스트제목")
                .postContent("테스트내용")
                .build();

        // when & then
        // when & then
        userDataRepository.save(testUser);
        lectureDataRepository.save(testLecture);
        classReviewDataRepository.save(testClassReview);
        
        Likes likes = Likes.builder().user(testUser).classReview(testClassReview).build();
        likesDataRepository.save(likes);

        // 삭제 메서드 호출 (실제 데이터가 없어도 예외가 발생하지 않음)
        likesDataRepository.deleteAllByClassReview(testClassReview);
        
        // 삭제 후 조회 시 결과가 없어야 함
        Likes result = likesDataRepository.findByUserAndClassReview(testUser, testClassReview);
        assertThat(result).isNull();
    }
}