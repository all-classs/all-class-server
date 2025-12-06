package org.classreviewsite.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.classreviewsite.review.controller.ReviewController;
import org.classreviewsite.review.controller.data.Request.ClassReviewRequest;
import org.classreviewsite.review.controller.data.Request.DeleteReviewRequest;
import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.classreviewsite.review.controller.data.Response.ReviewMeResponse;
import org.classreviewsite.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@DisplayName("ReviewController 엔드포인트 테스트")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @Nested
    @DisplayName("수강후기 목록 조회 테스트")
    class reviewListTest {
        
        @Test
        @DisplayName("필수 파라미터 lectureId 없이 요청 시, Bad Request를 반환한다")
        @WithMockUser
        void getReviewListWithoutLectureId() throws Exception {
            // when & then
            mockMvc.perform(get("/review"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("수강후기 작성 테스트")
    class addReviewTest {
        
        @Test
        @DisplayName("유효한 수강후기 작성 요청 시, 성공 응답을 반환한다")
        @WithMockUser
        void addReviewSuccess() throws Exception {
            // given
            ClassReviewRequest request = new ClassReviewRequest(
                    "자바프로그래밍", 20230857, 4.5, "좋은 강의", "정말 유익한 강의였습니다"
            );
            
            doNothing().when(reviewService).write(any(ClassReviewRequest.class));
            
            // when & then
            mockMvc.perform(post("/review")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("수강후기 작성이 완료되었습니다."));
        }
        
        @Test
        @DisplayName("잘못된 JSON 형식으로 수강후기 작성 요청 시, Bad Request를 반환한다")
        @WithMockUser
        void addReviewWithInvalidJson() throws Exception {
            // given
            String invalidJson = "{ \"userNumber\": \"invalid\", \"lectureName\": }";
            
            // when & then
            mockMvc.perform(post("/review")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("수강후기 수정 테스트")
    class updateReviewTest {
        
        @Test
        @DisplayName("유효한 수강후기 수정 요청 시, 성공 응답을 반환한다")
        @WithMockUser
        void updateReviewSuccess() throws Exception {
            // given
            UpdateReviewRequest request = new UpdateReviewRequest(
                    1L, "수정된 제목", "수정된 내용", 3.5, 20230857L
            );
            
            given(reviewService.updateReviewPost(any(UpdateReviewRequest.class)))
                    .willReturn(1L);
            
            // when & then
            mockMvc.perform(patch("/review")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("수강후기 수정이 완료되었습니다."));
        }
    }

    @Nested
    @DisplayName("수강후기 삭제 테스트")
    class deleteReviewTest {
        
        @Test
        @DisplayName("유효한 수강후기 삭제 요청 시, 성공 응답을 반환한다")
        @WithMockUser
        void deleteReviewSuccess() throws Exception {
            // given
            DeleteReviewRequest request = new DeleteReviewRequest(1L, 20230857);
            
            doNothing().when(reviewService).deleteReviewPost(any(DeleteReviewRequest.class));
            
            // when & then
            mockMvc.perform(delete("/review")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("수강후기 삭제가 완료되었습니다."));
        }
    }

    @Nested
    @DisplayName("수강후기 좋아요 테스트")
    class likeReviewTest {
        
        @Test
        @DisplayName("수강후기 좋아요 추가 시, 성공 응답을 반환한다")
        @WithMockUser
        void likeReviewAdd() throws Exception {
            // given
            LikeRequest request = new LikeRequest(20230857, 1L);
            
            given(reviewService.likeReview(any(LikeRequest.class)))
                    .willReturn("좋아요가 추가되었습니다.");
            
            // when & then
            mockMvc.perform(post("/review/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("좋아요가 추가되었습니다."));
        }
        
        @Test
        @DisplayName("수강후기 좋아요 취소 시, 202 응답을 반환한다")
        @WithMockUser
        void likeReviewCancel() throws Exception {
            // given
            LikeRequest request = new LikeRequest(20230857, 1L);
            
            given(reviewService.likeReview(any(LikeRequest.class)))
                    .willReturn("좋아요가 취소되었습니다.");
            
            // when & then
            mockMvc.perform(post("/review/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(202))
                    .andExpect(jsonPath("$.message").value("좋아요가 취소되었습니다."));
        }
    }

    @Nested
    @DisplayName("내 수강후기 조회 테스트")
    class myReviewTest {
        
        @Test
        @DisplayName("내 수강후기 조회 시, 성공 응답을 반환한다")
        @WithMockUser
        void getMyReviewSuccess() throws Exception {
            // given
            int userNumber = 20230857;
            List<ReviewMeResponse> mockResponse = Arrays.asList(
                createMockReviewMeResponse(1L, "내가 쓴 리뷰 1"),
                createMockReviewMeResponse(2L, "내가 쓴 리뷰 2")
            );
            
            given(reviewService.findMyReview(userNumber))
                    .willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/review/me")
                            .param("userNumber", String.valueOf(userNumber)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("해당 학생의 수강후기입니다."))
                    .andExpect(jsonPath("$.data").isArray());
        }
        
        @Test
        @DisplayName("필수 파라미터 userNumber 없이 요청 시, Bad Request를 반환한다")
        @WithMockUser
        void getMyReviewWithoutUserNumber() throws Exception {
            // when & then
            mockMvc.perform(get("/review/me"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    // 헬퍼 메서드들
    private ReviewMeResponse createMockReviewMeResponse(Long reviewId, String title) {
        return ReviewMeResponse.builder()
                .reviewId(reviewId)
                .postTitle(title)
                .postContent("내용")
                .starLating(4.5)
                .lectureName("강의명")
                .build();
    }
}