package org.classreviewsite.suite;

import org.classreviewsite.repository.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ClassReviewDataRepositoryTest.class,
        EnrollmentDataRepositoryTest.class,
        ImageUrlDataRepositoryTest.class,
        LectureDataRepositoryTest.class,
        LikesDataRepositoryTest.class,
        UserDataRepositoryTest.class,
        ReviewLogicTest.class
})
public class RepositorySuite {
}
