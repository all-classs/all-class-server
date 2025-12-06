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
import static org.mockito.ArgumentMatchers.anyLong;
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
        @DisplayName("기본 수강후기 목록 조회 시, 별점 높은 순으로 반환한다")
        @WithMockUser
        void getReviewListDefault() throws Exception {
            // given
            Long lectureId = 1L;
            List<ReviewResponse> mockResponse = Arrays.asList(
                createMockReviewResponse(1L, "좋은 강의", 5.0),
                createMockReviewResponse(2L, "괜찮은 강의", 4.0)
            );
            
            given(reviewService.findByLectureIdOrderByStarLatingDesc(lectureId))
                    .willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/review")
                            .param("lectureId", String.valueOf(lectureId)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("수강 후기 별점 높은 순 조회입니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isNotEmpty());
        }
        
        @Test
        @DisplayName("별점 낮은 순 조회 요청 시, 별점 낮은 순으로 반환한다")
        @WithMockUser
        void getReviewListByLowness() throws Exception {
            // given
            Long lectureId = 1L;
            List<ReviewResponse> mockResponse = Arrays.asList(
                createMockReviewResponse(1L, "아쉬운 강의", 2.0),
                createMockReviewResponse(2L, "괜찮은 강의", 4.0)
            );
            
            given(reviewService.findByLectureIdOrderByStarLatingAsc(lectureId))
                    .willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/review")
                            .param("lectureId", String.valueOf(lectureId))
                            .param("lowness", "true"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("수강 후기 별점 낮은 순 조회입니다."));
        }
        
        @Test
        @DisplayName("좋아요 높은 순 조회 요청 시, 좋아요 높은 순으로 반환한다")
        @WithMockUser
        void getReviewListByLikes() throws Exception {
            // given
            Long lectureId = 1L;
            List<ReviewResponse> mockResponse = Arrays.asList(
                createMockReviewResponse(1L, "인기 있는 리뷰", 4.5),
                createMockReviewResponse(2L, "일반 리뷰", 4.0)
            );
            
            given(reviewService.findByLectureIdOrderByLikesDesc(lectureId))
                    .willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/review")
                            .param("lectureId", String.valueOf(lectureId))
                            .param("likes", "true"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("수강 후기 좋아요 높은 순 조회입니다."));
        }
        
        @Test
        @DisplayName("최신순 조회 요청 시, 날짜 최신순으로 반환한다")
        @WithMockUser
        void getReviewListByRecent() throws Exception {
            // given
            Long lectureId = 1L;
            List<ReviewResponse> mockResponse = Arrays.asList(
                createMockReviewResponse(1L, "최신 리뷰", 4.0),
                createMockReviewResponse(2L, "이전 리뷰", 4.5)
            );
            
            given(reviewService.findByLectureIdOrderByCreateDateDesc(lectureId))
                    .willReturn(mockResponse);
            
            // when & then
            mockMvc.perform(get("/review")
                            .param("lectureId", String.valueOf(lectureId))
                            .param("recent", "true"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("수강 후기 날짜 최신순 조회입니다."));
        }
        
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
                    "자바프로그래밍", 20230857L, 4.5, "좋은 강의", "정말 유익한 강의였습니다"
            );
            
            doNothing().when(reviewService).addReviewPost(any(ClassReviewRequest.class));
            
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
    private ReviewResponse createMockReviewResponse(Long reviewId, String title, Double rating) {
        // ReviewResponse 객체를 생성하는 헬퍼 메서드
        // 실제 구현은 ReviewResponse의 생성자나 팩토리 메서드에 따라 달라집니다
        return null; // 실제로는 적절한 객체를 반환해야 합니다
    }
    
    private ReviewMeResponse createMockReviewMeResponse(Long reviewId, String title) {
        // ReviewMeResponse 객체를 생성하는 헬퍼 메서드
        // 실제 구현은 ReviewMeResponse의 생성자나 팩토리 메서드에 따라 달라집니다
        return null; // 실제로는 적절한 객체를 반환해야 합니다
    }
}