package org.classreviewsite.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.classreviewsite.common.Result;
import org.classreviewsite.review.infrastructure.ClassReview;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.review.controller.data.Response.ReviewResponse;
import org.classreviewsite.review.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "수강 후기정보 API ", description = "수강 후기 관련 요청입니다.")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/review")
    @Operation(summary = "수강 후기 전체 목록 조회", description = "Param 설명 :lectureId 꼭 입력해주셔야됩니다. lowness true일 경우 별점 낮은 순 조회입니다. likes true일 경우 좋아요 높은순 조회입니다. recent true일 경우 날짜 최신순 조회입니다. lectureId만 줄 경우 기본값 별점 높은순 조회입니다. ex) ?lectureId=1&lowness=true , ?lectureId=1")
    @ApiResponse(responseCode = "200", description = "수강 후기 전체 목록 조회입니다.")
    @ApiResponse(responseCode = "202", description = "수강 후기가 어디에도 없습니다.")
    public Result ReviewPostList(@RequestParam("lectureId") Long lectureId,
                                 @RequestParam(value = "lowness" , required = false) boolean lowness,
                                 @RequestParam(value = "likes", required = false) boolean likes,
                                 @RequestParam(value = "recent" , required = false) boolean recent
    ){
        if(lowness == true){
            log.info("별점 낮은 순");
            List<ReviewResponse> response = reviewService.findByLectureIdOrderByStarLatingAsc(lectureId);
            return new Result(200, response, "수강 후기 별점 낮은 순 조회입니다.");
        }else if(likes == true){
            log.info("좋아요 높은 순");
            List<ReviewResponse> response = reviewService.findByLectureIdOrderByLikesDesc(lectureId);
            return new Result(200, response, "수강 후기 좋아요 높은 순 조회입니다.");
        }else if(recent == true){
            log.info("날짜 최신순");
            List<ReviewResponse> response = reviewService.findByLectureIdOrderByCreateDateDesc(lectureId);
            return new Result(200, response, "수강 후기 날짜 최신순 조회입니다.");
        }

        log.info("별점 높은 순");
        List<ReviewResponse> response = reviewService.findByLectureIdOrderByStarLatingDesc(lectureId);
        return new Result(200, response, "수강 후기 별점 높은 순 조회입니다.");
    }

    @PostMapping("/review")
    @Operation(summary = "수강 후기 작성 요청", description = "수강 후기 작성을 요청합니다. String postTitle, String postContent, Double starLating, String lecture, Long userNumber 를 json 형태로 body로 전송하시면 됩니다.")
    @ApiResponse(responseCode = "200", description = "수강후기 작성이 완료되었습니다.")
    @ApiResponse(responseCode = "401", description = "해당 학생이 존재하지 않습니다.")
    @ApiResponse(responseCode = "40?", description = "해당 수강후기가 존재하지 않습니다.")
    @ApiResponse(responseCode = "403", description = "수강하지 않은 강의입니다. (reject 처리필요)")
    public Result addReviewPost(@RequestBody ClassReviewRequest request
    ){
        reviewService.addReviewPost(request);
        return new Result(200, null, "수강후기 작성이 완료되었습니다.");
    }

    @DeleteMapping("/review")
    @Operation(summary = "수강 후기 삭제 요청", description = "수강 후기 삭제를 요청합니다. Long postId 만 body를 통해서 전송 {} 없이 요청하시면됩니다.")
    @ApiResponse(responseCode = "200", description = "수강후기 삭제가 완료되었습니다.")
    public Result deleteReviewPost(@RequestBody DeleteReviewRequest request
    ){
        reviewService.deleteReviewPost(request);
        return new Result(200, null, "수강후기 삭제가 완료되었습니다.");
    }

    @PatchMapping("/review")
    @Operation(summary = "수강 후기 수정 요청", description = "수강 후기 수정을 요청합니다. Long postId, String postTitle, String postContent, Double starLating, Long important, Long funny, Long difficulty 를 json에 담아서 body로 요청하시면 됩니다.")
    @ApiResponse(responseCode = "200", description = "수강후기 수정이 완료되었습니다.")
    public Result updateReviewPost(@RequestBody UpdateReviewRequest request
    ){
        reviewService.updateReviewPost(request); // 수정쪽 예외 한번더 검토해야함
        return new Result(200, null, "수강후기 수정이 완료되었습니다.");
    }

    @PostMapping("/review/like")
    @Operation(summary = "수강 후기에 좋아요 요청", description = "수강 후기 좋아요를 요청합니다.")
    @ApiResponse(responseCode = "200", description = "좋아요가 추가되었습니다.")
    @ApiResponse(responseCode = "202", description = "좋아요가 취소되었습니다.")
    public Result likeReviewPost(@Parameter(required = true, description = "int userNumber, Long postId를 json에 담아서 body로 전송") @RequestBody LikeRequest request
    ){
        String message = reviewService.likeReview(request);
        if(message.equals("좋아요가 취소되었습니다.")){
            return new Result(202, null, message);
        }
        return new Result(200, null, message);
    }

    @GetMapping("/review/me")
    @Operation(summary = "해당 학생의 수강후기 전체 조회입니다.", description = "userNumber(학번)을 param을 요청하시면 해당 학생의 수강후기 리스트가 조회됩니다.")
    @ApiResponse(responseCode = "200",description = "해당 학생의 수강후기입니다.")
    @ApiResponse(responseCode = "202", description = "수강후기가 존재하지 않습니다.")
    public Result myReview(@RequestParam("userNumber") int userNumber){
        List<ClassReview> response = reviewService.findMyReview(userNumber);
        return new Result(200, response, "해당 학생의 수강후기입니다.");
    }

}