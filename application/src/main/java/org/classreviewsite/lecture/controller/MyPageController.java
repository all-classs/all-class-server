package org.classreviewsite.lecture.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.classreviewsite.review.controller.data.Response.MyPageStudentResponse;
import org.classreviewsite.review.service.UserClassListService;
import org.classreviewsite.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final UserClassListService userClassListService;

    @GetMapping("/mypage/student")
    @Operation(summary = "마이페이지 학생 조회(수강한 강의)", description = "userNumber(학번)을 param으로 전송해주시면 해당 학생의 강의 조회를 합니다.")
    @ApiResponse(responseCode = "200", description = "학생의 마이페이지 강의 조회입니다.")
    @ApiResponse(responseCode = "401", description = "해당 학생을 찾을 수 없습니다")
    public Result myPageWithStudent(@RequestParam("userNumber") int userNumber){
        List<MyPageStudentResponse> response = userClassListService.myPageWithStudent(userNumber);
        return new Result<>(200, response, "학생의 마이페이지 강의 조회입니다.");
    }

}
