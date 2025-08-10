package org.classreviewsite.lecture.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.classreviewsite.lecture.service.EnrollmentService;
import org.classreviewsite.review.controller.data.Response.ClassListResponse;
import org.classreviewsite.review.controller.data.Response.ClassListWithProfessorResponse;
import org.classreviewsite.lecture.controller.data.response.EnrollmentResponse;
import org.classreviewsite.review.service.ClassListService;
import org.classreviewsite.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "강의 정보 API", description = "강의 정보 관련 요청입니다.")
public class ClassController {

    private final EnrollmentService enrollmentService;
    private final ClassListService classListService;

    @GetMapping("/class")
    @Operation(summary = "전체 강의 정보", description = "university(학교이름)을 param으로 요청하시면 해당 학교의 강의정보가 조회됩니다. 해당 학교의 강의정보가 없을경우 401 상태번호를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "전체 강의 목록입니다.")
    @ApiResponse(responseCode = "401", description = "해당 학교의 강의가 존재하지 않습니다.")
    public Result classList(@RequestParam(value = "university") String university, @RequestParam(value = "lectureId", required = false) Long lectureId){

        if(lectureId==null){
            List<ClassListResponse> response = classListService.get(university);
            return new Result(200, response, "전체 강의 목록입니다.");
        }

        ClassListWithProfessorResponse.ClassListWithProfessorNameInDetail response = classListService.detail(lectureId);
        return new Result(200, response, "강의 상세 정보 조회입니다.");
    }

    @GetMapping("/class/me")
    @Operation(summary = "나의 수강 정보", description = "userNumber(학번)을 param으로 요청하시면 해당 학생의 수강 정보가 조회됩니다.")
    @ApiResponse(responseCode = "200", description = "해당 학생의 수강한 강의 목록입니다.")
    @ApiResponse(responseCode = "401", description = "해당 학생이 수강한 강의는 없습니다.")
    public Result findUserClassList(@RequestParam("userNumber") int userNumber){
        List<EnrollmentResponse> response = enrollmentService.findClassForSemester(userNumber);
        return new Result(200, response, "해당 학생의 수강한 강의 목록입니다.");
    }

}
