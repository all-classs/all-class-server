package org.classreviewsite.suite;

import org.classreviewsite.service.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ClassListServiceTest.class,
        EnrollmentServiceTest.class,
        ImageUrlServiceTest.class,
        LectureDataServiceTest.class,
        LikeServiceTest.class,
        ReviewServiceTest.class,
        UserServiceTest.class
})
public class ServiceSuite {
}
