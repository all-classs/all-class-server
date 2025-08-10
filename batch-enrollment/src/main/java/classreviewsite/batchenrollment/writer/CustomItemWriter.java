package classreviewsite.batchenrollment.writer;

import classreviewsite.batchenrollment.config.data.EnrollmentCsv;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.lecture.infrastructure.*;
import org.classreviewsite.user.infrastructure.User;
import org.classreviewsite.user.infrastructure.UserDataRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class CustomItemWriter implements ItemWriter<EnrollmentCsv> {

    private final LectureDataRepository lectureDataRepository;
    private final UserDataRepository userDataRepository;
    private final EnrollmentDataRepository enrollmentDataRepository;

    int i = 0;

    public CustomItemWriter(LectureDataRepository lectureDataRepository, UserDataRepository userDataRepository, EnrollmentDataRepository enrollmentDataRepository) {this.lectureDataRepository = lectureDataRepository;this.userDataRepository = userDataRepository;
        this.enrollmentDataRepository = enrollmentDataRepository;
    }

    @Override
    public void write(Chunk<? extends EnrollmentCsv> chunk) {
        log.info("===== writer 작동 ====");
        chunk.getItems().forEach(item -> {
            Lecture lecture = lectureDataRepository.findById(item.getLectureId()).orElse(notExistLecture(item));
            User user = userDataRepository.findByUserNumber(item.getStudentNumber()).get();
            Enrollment enrollment = CsvToEntityConverter.toEnrollment(item, user, lecture);

            enrollmentDataRepository.findByUserNumber_UserNumberAndLecture_LectureId(user.getUserNumber(), lecture.getLectureId()).orElse(
                    enrollmentDataRepository.save(enrollment)
            );

            log.info(item.getCourseName());
            i++;
        });

    }

    public Lecture notExistLecture(EnrollmentCsv item) {
        LectureType type = LectureType.valueOf(item.getCompletionType());
        Lecture lecture =  new Lecture(
                item.getLectureId(),
                item.getCourseName(),
                StarRating.createRatingBuilder(),
                "소프트웨어학과",
                "동서대학교",
                "미분류",
                type
        );

        lectureDataRepository.save(lecture);

        return lecture;
    }
}
