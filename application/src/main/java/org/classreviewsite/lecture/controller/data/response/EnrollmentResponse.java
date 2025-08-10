package org.classreviewsite.lecture.controller.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.classreviewsite.lecture.infrastructure.Enrollment;

@Data
@AllArgsConstructor
@Builder
public class EnrollmentResponse {

    private String CompletionYear;
    private String CompletionType;
    private String semester;
    private Long classNumber;
    private String lectureName;
    private String professorName;

    public static EnrollmentResponse from(Enrollment enrollment){
        return EnrollmentResponse.builder()
                .classNumber(enrollment.getLecture().getLectureId())
                .semester(enrollment.getSemester())
                .CompletionType(enrollment.getCompletionType())
                .CompletionYear(enrollment.getCompletionYear())
                .lectureName(enrollment.getLecture().getLectureName())
                .professorName(enrollment.getProfessor())
                .build();
    }

}
