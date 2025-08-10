package org.classreviewsite.review.controller.data.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UpdateReviewRequest {

    Long postId;

    String postTitle;

    String postContent;

    Double starLating;

    Long userNumber;

    public static UpdateReviewRequest of(Long postId, String postTitle, String postContent, Double starLating, Long userNumber) {
        return new UpdateReviewRequest(
                postId,
                postTitle,
                postContent,
                starLating,
                userNumber
        );
    }
}
