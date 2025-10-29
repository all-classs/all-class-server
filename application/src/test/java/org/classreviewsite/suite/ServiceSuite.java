package org.classreviewsite.suite;

import org.classreviewsite.service.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ClassListAndDetailServiceTest.class,
        EnrollmentDataServiceTest.class,
        ImageUrlServiceTest.class,
        LectureDataServiceTest.class,
        LikeDataServiceTest.class,
        ReviewServiceTest.class,
        UserServiceTest.class
})
public class ServiceSuite {
}
