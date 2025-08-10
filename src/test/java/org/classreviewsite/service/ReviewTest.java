package org.classreviewsite.service;

import autoparams.AutoSource;
import org.classreviewsite.lecture.service.LectureService;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.infrastructure.ClassReviewDataRepository;
import org.classreviewsite.review.service.LikeService;
import org.classreviewsite.review.service.ReviewService;
import org.classreviewsite.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class ReviewTest {

    @InjectMocks private ReviewService reviewService;
    @Mock ClassReviewDataRepository classReviewDataRepository;
    @Mock LectureService lectureService;
    @Mock UserService userService;
    @Mock LikeService likeService;

    @AutoSource
    @ParameterizedTest
    @DisplayName("수강후기를 작성하면, Persist 처리한다.")
    void addReviewTest(ClassReviewRequest classReviewRequest) {


    }

}
