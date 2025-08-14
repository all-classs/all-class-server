package org.classreviewsite.suite;

import org.classreviewsite.dto.ClassReviewRequestTest;
import org.classreviewsite.dto.LikeRequestTest;
import org.classreviewsite.dto.ReviewResponseTest;
import org.classreviewsite.dto.UpdateReviewRequestTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ClassReviewRequestTest.class,
        LikeRequestTest.class,
        ReviewResponseTest.class,
        UpdateReviewRequestTest.class
})
public class DtoSuite {
}
