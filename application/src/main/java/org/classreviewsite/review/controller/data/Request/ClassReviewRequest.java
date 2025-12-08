package org.classreviewsite.review.controller.data.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.classreviewsite.domain.review.ClassReview;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClassReviewRequest {
    private String lectureName;
    private Integer userNumber;
    private Double starLating;
    private String postTitle;
    private String postContent;

    public static ClassReviewRequest from(ClassReview review){
        return new ClassReviewRequest(
                review.getLecId().getLectureName(),
                review.getUserNumber().getUserNumber(),
                review.getStarLating(),
                review.getPostTitle(),
                review.getPostContent()
        );
    }

    public static ClassReviewRequest of(String postTitle, String postContent, Integer userNumber, Double starLating, String lectureName){
        return new ClassReviewRequest(
                lectureName,
                userNumber,
                starLating,
                postTitle,
                postContent
        );
    }
}
