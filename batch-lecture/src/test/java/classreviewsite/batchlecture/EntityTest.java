package classreviewsite.batchlecture;

import classreviewsite.batchlecture.config.data.LectureCsv;
import classreviewsite.batchlecture.writer.CsvToEntityConverter;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityTest {
    @Test
    @DisplayName("chunk가 파라미터를 전달하면, Lecture 객체를 반환한다.")
    void csvToUser() {
        LectureCsv lectureCsv = new LectureCsv();
        lectureCsv.setLectureNumber("12345");
        lectureCsv.setCourse("교양필수");
        lectureCsv.setDepartment("소프트웨어학과");
        lectureCsv.setGrade("1");
        lectureCsv.setName("강의명");
        lectureCsv.setTerm("2학기");

         Lecture lecture = CsvToEntityConverter.toLecture(
                        lectureCsv,
                "동서대학교",
                "교수1"
        );

        assertThat(lecture).isNotNull();
        assertThat(lecture.getClass()).isEqualTo(Lecture.class);
    }
}
