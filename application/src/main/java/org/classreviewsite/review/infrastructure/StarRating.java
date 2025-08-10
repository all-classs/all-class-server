package org.classreviewsite.review.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarRating {
    @Column(name = "average_star_rating", nullable = true)
    private Double averageRating;

    @Column(name = "total_star_rating", nullable = true)
    private Double totalRating;

    @Column(name = "review_count", nullable = true)
    private Long reviewCount;

    public StarRating addRating(Double rating) {
        if (this.reviewCount == null) this.reviewCount = 0L;
        if (this.totalRating == null) this.totalRating = 0.0;

        this.reviewCount++;
        this.totalRating += rating;
        this.averageRating = this.totalRating / this.reviewCount;

        return this;
    }

    public StarRating removeRating(Double rating) {
        if (this.reviewCount == null || this.reviewCount <= 0) return this;

        this.reviewCount--;
        this.totalRating -= rating;

        if (this.reviewCount == 0) {
            this.averageRating = 0.0;
            this.totalRating = 0.0;
        }else {
            this.averageRating = this.totalRating / this.reviewCount;
        }

        return this;
    }

    public static StarRating createRatingBuilder() {
        return StarRating.builder()
                .averageRating(0.0)
                .totalRating(0.0)
                .reviewCount(0L)
                .build();
    }
}