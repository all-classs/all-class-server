package classreviewsite.batchdummy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 더미 수강후기 데이터 모델
 */
@Getter
@AllArgsConstructor
public class DummyReviewData {
    private final int likes;
    private final double starRating;
    private final Long userNumber;
    private final Long lecId;
    private final String postContent;
    private final String postTitle;
}