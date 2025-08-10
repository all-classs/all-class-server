package classreviewsite.batchenrollment;

import classreviewsite.batchenrollment.config.data.EnrollmentCsv;
import classreviewsite.batchenrollment.writer.CsvToEntityConverter;
import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.user.infrastructure.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityTest {
    @Test
    @DisplayName("chunk가 파라미터를 전달하면, Enrollment 객체를 반환한다.")
    void csvToUser() {
        EnrollmentCsv enrollmentCsv = new EnrollmentCsv();
        User user = new User();
        Lecture lecture = new Lecture();

        Enrollment enrollment = CsvToEntityConverter.toEnrollment(
                enrollmentCsv,
                user,
                lecture
        );

        assertThat(enrollment).isNotNull();
        assertThat(enrollment.getClass()).isEqualTo(Enrollment.class);
    }
}
