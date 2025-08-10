package classreviewsite.batchenrollment.writer;

import classreviewsite.batchenrollment.config.data.EnrollmentCsv;
import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.user.infrastructure.User;

public class CsvToEntityConverter {
    public static Enrollment toEnrollment(EnrollmentCsv enrollmentCsv, User user, Lecture lecture){
        return new Enrollment(
                0L,
                enrollmentCsv.getCompletionType(),
                String.valueOf(enrollmentCsv.getCompletionYear()),
                String.valueOf(enrollmentCsv.getSemester()),
                user,
                lecture,
                enrollmentCsv.getProfessorName()
        );
    }
}
