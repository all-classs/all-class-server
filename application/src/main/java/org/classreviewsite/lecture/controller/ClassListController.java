//package org.classreviewsite.lecture.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.classreviewsite.review.controller.data.Response.ClassListResponse;
//import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
//import org.classreviewsite.review.controller.data.Response.UserClassListResponse;
//import org.classreviewsite.review.service.ClassListService;
//import org.classreviewsite.review.service.UserClassListService;
//import org.classreviewsite.common.Result;
//import org.classreviewsite.lecture.controller.data.response.DepartmentResponse;
//import org.classreviewsite.lecture.service.LectureService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@Tag(name = "강의 정보 API", description = "강의 정보 관련 요청입니다.")
//public class ClassListController {
//
//    private final UserClassListService userClassListService;
//    private final ClassListService classListService;
//    private final LectureService lectureService;
//
//    @GetMapping("/class")
//    @Operation(summary = "전체 강의 정보", description = "university(학교이름)을 param으로 요청하시면 해당 학교의 강의정보가 조회됩니다. 해당 학교의 강의정보가 없을경우 401 상태번호를 반환합니다.")
//    @ApiResponse(responseCode = "200", description = "전체 강의 목록입니다.")
//    @ApiResponse(responseCode = "401", description = "해당 학교의 강의가 존재하지 않습니다.")
//    public Result findClassList(@RequestParam(value = "university") String university, @RequestParam(value = "lectureId", required = false) Long lectureId){
//
//        if(lectureId==null){
//            List<ClassListResponse> response = classListService.getByUniversity(university);
//            return new Result(200, response, "전체 강의 목록입니다.");
//        }
//
//        ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail response = classListService.detail(lectureId);
//        return new Result(200, response, "강의 상세 정보 조회입니다.");
//    }
//
//    @GetMapping("/class/me")
//    @Operation(summary = "나의 수강 정보", description = "userNumber(학번)을 param으로 요청하시면 해당 학생의 수강 정보가 조회됩니다.")
//    @ApiResponse(responseCode = "200", description = "해당 학생의 수강한 강의 목록입니다.")
//    @ApiResponse(responseCode = "401", description = "해당 학생이 수강한 강의는 없습니다.")
//    public Result findUserClassList(@RequestParam("userNumber") int userNumber){
//        List<UserClassListResponse> response = userClassListService.findClassForSemester(userNumber);
//        return new Result(200, response, "해당 학생의 수강한 강의 목록입니다.");
//    }
//
//    @GetMapping("/class/recommend")
//    @Operation(summary = "강의 무작위 추천 3개", description = "무작위로 3과목 강의를 추천합니다.")
//    @ApiResponse(responseCode = "200", description = "무작위 강의 3과목을 추천합니다.")
//    public Result findRecommendClass(@RequestParam("university") String university){
//        return new Result<>(200, classListService.getRecommend(university), "무작위 강의 추천입니다.");
//    }
//
//    @GetMapping("/department")
//    @Operation(summary = "학과 전체조회" , description = " 기존 강의 목록에서 학과들을 추려 중복을 제거하고 출력하고있습니다.")
//    @ApiResponse(responseCode = "200", description = "학과 전체조회입니다.")
//    public Result findDepartmentRequest(){
//        List<String> departmentList =  lectureService.findDistinctDepartment();
//        DepartmentResponse response = new DepartmentResponse(200, departmentList, "학과 전체조회입니다.");
//        return new Result<>(200, response, "학과 전체조회입니다.");
//    }
//
//
//}
