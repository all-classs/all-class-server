package org.classreviewsite.review.controller.data.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.domain.util.NumberFormat;
import org.classreviewsite.domain.review.ClassReview;

@Data
@AllArgsConstructor
@Slf4j
public class ReviewMeResponse {
    private Long reviewId;
    private String postTitle;
    private String postContent;
    private Double starLating;
    private Integer likes;
    private String createDate;
    private String lectureName;

    public static ReviewMeResponse from(ClassReview classReview){
        return new ReviewMeResponse(
                classReview.getReviewId(),
                classReview.getPostTitle(),
                classReview.getPostContent(),
                NumberFormat.format(classReview.getStarLating()),
                classReview.getLikes(),
                classReview.getCreatedDate().getYear()+"-"+classReview.getCreatedDate().getMonth().getValue()+"-"+classReview.getCreatedDate().getDayOfMonth(),
                classReview.getLecId()

        );
    }


}
