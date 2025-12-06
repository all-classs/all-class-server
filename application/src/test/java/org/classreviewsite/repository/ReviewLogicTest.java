package org.classreviewsite.repository;

import jakarta.persistence.EntityManager;
import org.classreviewsite.domain.lecture.Lecture;
import org.classreviewsite.domain.lecture.LectureDataRepository;
import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.domain.review.ClassReview;
import org.classreviewsite.domain.user.User;
import org.classreviewsite.domain.user.UserDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class ReviewLogicTest {
    @Autowired EntityManager em;
    @Autowired LectureDataRepository lectureDataRepository;
    @Autowired ClassReviewDataRepository classReviewDataRepository;
    @Autowired UserDataRepository userDataRepository;

    @Transactional
    @Test
    @DisplayName("수강후기 작성 요청이 오면, 수강후기를 추가하고 별점을 증가시킨다.")
    void addReview() {
        ClassReviewRequest request = ClassReviewRequest.of(
                "작성글제목",
                "작성글내용",
                20191434,
                4.5,
                "아이데이션융합실습4-SW(캡스톤디자인)"
        );
        Lecture lecture = lectureDataRepository.findByLectureName(request.getLectureName()).get();
        User user = userDataRepository.findById(request.getUserNumber()).get();

        ClassReview review = ClassReview.builder()
                .lecId(lecture)
                .userNumber(user)
                .starLating(request.getStarLating())
                .likes(0)
                .postTitle(request.getPostTitle())
                .postContent(request.getPostContent())
                .build();

        assertThat(classReviewDataRepository.findByUserNumberAndLecId(user, lecture)).isNotNull();

        lecture.addStarRating(request.getStarLating());
        em.persist(review);
        em.flush();
        em.clear();

        Lecture lecture2 = lectureDataRepository.findByLectureName(request.getLectureName()).get();

        assertThat(lecture2.getStarRating().getTotalRating()).isEqualTo(request.getStarLating());
        assertThat(lecture2.getStarRating().getReviewCount()).isNotZero();
    }

}
