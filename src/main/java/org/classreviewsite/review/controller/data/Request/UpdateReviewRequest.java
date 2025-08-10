package org.classreviewsite.review.controller.data.Request;

import lombok.Data;

@Data
public class UpdateReviewRequest {

    Long postId;

    String postTitle;

    String postContent;
}
