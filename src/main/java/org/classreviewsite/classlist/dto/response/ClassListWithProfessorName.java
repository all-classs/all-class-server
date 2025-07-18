package org.classreviewsite.classlist.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.classreviewsite.auth.util.NumberFormat;
import org.classreviewsite.classlist.data.ClassList;
import org.classreviewsite.lecture.data.LectureType;
import org.classreviewsite.lecture.data.Professor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ClassListWithProfessorName {

    private Double AverageStarLating;
    private String lectureName;
    private String department;
    private LectureType LectureType;
    private Long ReviewCount;
    private String university;
    private Long lectureId;
    private Double TotalStarLating;
    private String professorName;


    public static List<ClassListWithProfessorName> from(List<ClassList> classList){
        List<ClassListWithProfessorName> list = new ArrayList<>();
        for(ClassList Class : classList){
            list.add(ClassListWithProfessorName.builder()
                    .professorName(Class.getProfessor().getProfessorName())
                    .lectureName(Class.getLecture().getLectureName())
                    .LectureType(Class.getLecture().getLectureType())
                    .AverageStarLating(Class.getLecture().getStarRating().getAverageRating())
                    .department(Class.getLecture().getDepartment())
                    .lectureId(Class.getLecture().getLectureId())
                    .ReviewCount(Class.getLecture().getStarRating().getReviewCount())
                    .TotalStarLating(Class.getLecture().getStarRating().getTotalRating())
                    .university(Class.getLecture().getUniversity())
                    .build()
            );
        }

        return list;
    }


    @Data
    @AllArgsConstructor
    @Builder
    public static class ClassListWithProfessorNameInDetail{
        private Long lectureId;
        private String lectureName;
        private Double averageStarLating;
        private Double totalStarLating;
        private Long reviewCount;
        private String department;
        private String university;
        private LectureType lectureType;
        private String professor;
        private String introduction;
        private String profileImage;
        private Long classNumber;
        private String icon;


        public static ClassListWithProfessorNameInDetail from(ClassList classList){
            Professor professor = classList.getProfessor();

            return ClassListWithProfessorNameInDetail.builder()
                    .averageStarLating(NumberFormat.format(classList.getLecture().getStarRating().getAverageRating()))
                    .professor(professor.getProfessorName())
                    .lectureId(classList.getLecture().getLectureId())
                    .lectureName(classList.getLecture().getLectureName())
                    .lectureType(classList.getLecture().getLectureType())
                    .department(classList.getLecture().getDepartment())
                    .reviewCount(classList.getLecture().getStarRating().getReviewCount())
                    .totalStarLating(classList.getLecture().getStarRating().getTotalRating())
                    .university(classList.getLecture().getUniversity())
                    .introduction(classList.getClassIntroduction())
                    .profileImage(professor.getProfileImage().getImageUrl())
                    .classNumber(classList.getClassNumber())
                    .icon(classList.getIcon().getImageUrl())
                    .build();
        }
    }

}
