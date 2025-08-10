package org.classreviewsite.domain;

import org.classreviewsite.handler.exception.UpdateFailedException;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.review.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClassReviewTest {

    @InjectMocks ReviewService reviewService;


    @Nested
    @DisplayName("수강 후기 작성 로직 테스트")
    class AddReview {

    }

    @Nested
    @DisplayName("수강 후기 수정 로직 테스트")
    class UpdateReview {
        @Test
        @DisplayName("요청 값이 비어있을 경우, 예외가 발생한다.")
        void fail() {
            UpdateReviewRequest request = new UpdateReviewRequest(
                    1L,
                    "수정",
                    "수정",
                    0.0,
                    null,
                    0L,
                    1L
            );

            Assertions.assertThrows(UpdateFailedException.class, () -> {
                reviewService.updateReviewPost(request);
            });
        }
    }
}