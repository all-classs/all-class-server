package org.classreviewsite.review.controller.data.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.common.util.NumberFormat;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.user.controller.data.response.UserResponse;

@Data
@AllArgsConstructor
@Slf4j
public class ReviewResponse {

    private Long postId;
    private String postTitle;
    private String postContent;
    private Double starLating;
    private Integer likes;
    private String createDate;
    private UserResponse user;

    public static ReviewResponse from(ClassReview classReview){
        return new ReviewResponse(
                classReview.getReviewId(),
                classReview.getPostTitle(),
                classReview.getPostContent(),
                NumberFormat.format(classReview.getStarLating()),
                classReview.getLikes(),
                classReview.getCreatedDate().getYear()+"-"+classReview.getCreatedDate().getMonth().getValue()+"-"+classReview.getCreatedDate().getDayOfMonth(),
                UserResponse.from(classReview.getUserNumber())
        );
    }

}
