//package org.classreviewsite.review.controller.data.Response;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.classreviewsite.common.util.NumberFormat;
//import org.classreviewsite.lecture.infrastructure.ClassList;
//import org.classreviewsite.lecture.infrastructure.LectureType;
//
//@Data
//@AllArgsConstructor
//public class ClassListResponse {
//
//    private Long lectureId;
//    private String lectureName;
//    private String department;
//    private String university;
//    private LectureType lectureType;
//    private Double averageStarLating;
//    private String professor;
//    private boolean opened;
//    private String icon;
//
//    public static ClassListResponse from(ClassList classList){
//        return new ClassListResponse(
//                classList.getLecture().getLectureId(),
//                classList.getLecture().getLectureName(),
//                classList.getLecture().getDepartment(),
//                classList.getLecture().getUniversity(),
//                classList.getLecture().getLectureType(),
//                NumberFormat.format(classList.getLecture().getStarRating().getAverageRating()),
//                classList.getProfessor().getProfessorName(),
//                classList.isOpened(),
//                classList.getIcon().getImageUrl()
//
//        );
//    }
//
//}
