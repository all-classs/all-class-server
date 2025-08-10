package org.classreviewsite.review.controller.data.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.classreviewsite.lecture.infrastructure.Enrollment;

@Data
@AllArgsConstructor
@Builder
public class UserClassListResponse {

    private String CompletionYear;
    private String CompletionType;
    private String semester;
    private Long classNumber;
    private String lectureName;
    private String professorName;

    public static UserClassListResponse from(Enrollment userClassList){
        return UserClassListResponse.builder()
                .classNumber(userClassList.getLecture().getLectureId())
                .semester(userClassList.getSemester())
                .CompletionType(userClassList.getCompletionType())
                .CompletionYear(userClassList.getCompletionYear())
                .lectureName(userClassList.getLecture().getLectureName())
                .professorName(userClassList.getProfessor())
                .build();
    }

}
