package org.classreviewsite.review.controller.data.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteReviewRequest {
    private Long postId;

    private int userNumber;


    public static DeleteReviewRequest of(Long postId, int usernumber){
        return new DeleteReviewRequest(
                postId,
                usernumber
        );
    }
}
