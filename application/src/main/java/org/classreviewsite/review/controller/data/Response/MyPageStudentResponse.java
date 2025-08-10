package org.classreviewsite.review.controller.data.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.classreviewsite.lecture.infrastructure.Enrollment;
import org.classreviewsite.lecture.infrastructure.LectureType;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class MyPageStudentResponse {

    //        private Long lectureId;
    private Long classNumber;
    private String lectureName;
    private String CompletionYear;
    private String grade;
    private String semester;
    private LectureType lectureType;
    private String professor;

        public static List<MyPageStudentResponse> from(List<Enrollment> classLists){
            List<MyPageStudentResponse> list = new ArrayList<>();

            for(Enrollment userClassList : classLists){
                list.add(
                        new MyPageStudentResponse(
                                userClassList.getLecture().getLectureId(),
                                userClassList.getLecture().getLectureName(),
                                userClassList.getCompletionYear(),
                                userClassList.getGrade(),
                                userClassList.getSemester(),
                                userClassList.getLecture().getLectureType(),
                                userClassList.getProfessor()
                        )
                );
            }

            return list;
        }
}
