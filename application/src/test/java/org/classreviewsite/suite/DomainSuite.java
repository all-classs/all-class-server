package org.classreviewsite.suite;

import org.classreviewsite.domain.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthorityTest.class,
        ClassReviewTest.class,
        EnrollmentTest.class,
        ImageUrlTest.class,
        LectureTest.class,
        LikesTest.class,
        StarRatingTest.class,
        UserTest.class
})
public class DomainSuite {
}
