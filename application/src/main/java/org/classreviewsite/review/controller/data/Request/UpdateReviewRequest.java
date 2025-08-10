package org.classreviewsite.review.controller.data.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateReviewRequest {

    Long postId;

    String postTitle;

    String postContent;

    Double starLating;

    Long important;

    Long funny;

    Long difficulty;




}
