package classreviewsite.batchlecture.writer;

import classreviewsite.batchlecture.config.data.LectureCsv;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;
import org.classreviewsite.lecture.infrastructure.StarRating;

public class CsvToEntityConverter {
    public static Lecture toLecture(LectureCsv lectureCsv, String university, String professor) {
        return new Lecture(
                Long.valueOf(lectureCsv.getLectureNumber()),
                lectureCsv.getName(),
                StarRating.createRatingBuilder(),
                lectureCsv.getDepartment(),
                university,
                professor,
                LectureType.valueOf(lectureCsv.getCourse())
        );
    }
}
