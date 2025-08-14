package org.classreviewsite.review.controller.data.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.classreviewsite.common.util.NumberFormat;
import org.classreviewsite.lecture.infrastructure.Lecture;
import org.classreviewsite.lecture.infrastructure.LectureType;

@Data
@AllArgsConstructor
public class ClassListResponse {

    private Long lectureId;
    private String lectureName;
    private String department;
    private String university;
    private LectureType lectureType;
    private Double averageStarLating;
    private String professor;

    public static ClassListResponse from(Lecture lecture){
        return new ClassListResponse(
                lecture.getLectureId(),
                lecture.getLectureName(),
                lecture.getDepartment(),
                lecture.getUniversity(),
                lecture.getLectureType(),
                lecture.getAverageRating(),
                lecture.getProfessor()
        );
    }

}
