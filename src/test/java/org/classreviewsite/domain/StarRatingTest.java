package org.classreviewsite.domain;

import org.classreviewsite.review.infrastructure.StarRating;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StarRatingTest {

    ClassReviewRequest classReviewRequest;

    @BeforeEach
    void setUp() {
        classReviewRequest = ClassReviewRequest.of("", "", 20201234L, 4.0, "");
    }

    @Test
    @DisplayName("별점이 추가될때, 평균 별점과 총합 별점을 변경한다.")
    void addRating() {
        Double rating = classReviewRequest.getStarLating();

        System.out.println(classReviewRequest.getStarLating());

        StarRating starRating = StarRating.createRatingBuilder();
        starRating.addRating(rating);

        assertThat(starRating.getTotalRating()).isNotEqualTo(0.0);
        assertThat(starRating.getAverageRating()).isNotEqualTo(0.0);
        assertThat(starRating.getReviewCount()).isEqualTo(1);
        starRating.addRating(3.0);

        assertThat(starRating.getAverageRating()).isEqualTo(3.5);
        assertThat(starRating.getReviewCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("수강후기글이 삭제될때, 별점 값은 줄어들어 반환된다.")
    void removeRating() {
        Double rating = classReviewRequest.getStarLating();
        System.out.println(classReviewRequest.getStarLating());

        StarRating starRating = StarRating.createRatingBuilder();
        starRating.addRating(rating);

        assertThat(starRating.getTotalRating()).isNotEqualTo(0.0);
        assertThat(starRating.getAverageRating()).isNotEqualTo(0.0);
        assertThat(starRating.getReviewCount()).isEqualTo(1);

        starRating.removeRating(4.0);
        assertThat(starRating.getTotalRating()).isEqualTo(0.0);
        assertThat(starRating.getReviewCount()).isEqualTo(0);
        assertThat(starRating.getAverageRating()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("글 2개를 추가하고 2개를 삭제하여, 평균 별점, 총 별점은 0.0을 반환한다.")
    void doubleAddToRemoveRating() {
        Double rating = classReviewRequest.getStarLating();
        System.out.println(classReviewRequest.getStarLating());

        StarRating starRating = StarRating.createRatingBuilder();
        starRating.addRating(rating); // 4.0
        starRating.addRating(rating); // 4.0

        assertThat(starRating.getTotalRating()).isEqualTo(8.0);
        assertThat(starRating.getAverageRating()).isEqualTo(4.0);
        assertThat(starRating.getReviewCount()).isEqualTo(2);

        starRating.removeRating(rating);
        starRating.removeRating(rating);

        assertThat(starRating.getAverageRating()).isEqualTo(0.0);
        assertThat(starRating.getTotalRating()).isEqualTo(0.0);
        assertThat(starRating.getReviewCount()).isEqualTo(0);
    }
}