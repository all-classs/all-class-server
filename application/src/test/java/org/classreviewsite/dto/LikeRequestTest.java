package org.classreviewsite.dto;

import org.classreviewsite.review.controller.data.Request.LikeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LikeRequestTest {

    @Nested
    @DisplayName("LikeRequest 생성 테스트")
    class createLikeRequestTest {
        
        @Test
        @DisplayName("모든 필드를 포함하여 요청 객체를 생성한다")
        void createWithAllFields() {
            // given
            int userNumber = 20230857;
            Long postId = 1L;
            
            // when
            LikeRequest request = new LikeRequest(userNumber, postId);
            
            // then
            assertThat(request.getUserNumber()).isEqualTo(userNumber);
            assertThat(request.getPostId()).isEqualTo(postId);
        }
        
        @Test
        @DisplayName("빌더를 사용하여 요청 객체를 생성한다")
        void createWithBuilder() {
            // given & when
            LikeRequest request = LikeRequest.builder()
                    .userNumber(20230858)
                    .postId(2L)
                    .build();
            
            // then
            assertThat(request.getUserNumber()).isEqualTo(20230858);
            assertThat(request.getPostId()).isEqualTo(2L);
        }
        
        @Test
        @DisplayName("기본 생성자를 사용하여 빈 요청 객체를 생성한다")
        void createWithNoArgsConstructor() {
            // when
            LikeRequest request = new LikeRequest();
            
            // then
            assertThat(request.getUserNumber()).isEqualTo(0); // int의 기본값
            assertThat(request.getPostId()).isNull();
        }
        
        @Test
        @DisplayName("8자리 학번으로 요청 객체를 생성한다")
        void createWithValidStudentNumber() {
            // given & when
            LikeRequest request = new LikeRequest(20240841, 1L);
            
            // then
            assertThat(request.getUserNumber()).isEqualTo(20240841);
            assertThat(String.valueOf(request.getUserNumber())).hasSize(8);
        }
        
        @Test
        @DisplayName("다양한 게시글 ID로 요청 객체를 생성한다")
        void createWithVariousPostIds() {
            // given
            Long[] postIds = {1L, 100L, 999L, 1000L};
            int userNumber = 20230857;
            
            for (Long postId : postIds) {
                // when
                LikeRequest request = new LikeRequest(userNumber, postId);
                
                // then
                assertThat(request.getPostId()).isEqualTo(postId);
                assertThat(request.getUserNumber()).isEqualTo(userNumber);
            }
        }
        
        @Test
        @DisplayName("setter를 사용하여 필드를 수정한다")
        void modifyFieldsWithSetter() {
            // given
            LikeRequest request = new LikeRequest(20230857, 1L);
            
            // when
            request.setUserNumber(20230858);
            request.setPostId(2L);
            
            // then
            assertThat(request.getUserNumber()).isEqualTo(20230858);
            assertThat(request.getPostId()).isEqualTo(2L);
        }
        
        @Test
        @DisplayName("동일한 사용자가 여러 게시글에 좋아요 요청을 생성한다")
        void createMultipleRequestsForSameUser() {
            // given
            int userNumber = 20230857;
            Long[] postIds = {1L, 2L, 3L};
            
            for (Long postId : postIds) {
                // when
                LikeRequest request = new LikeRequest(userNumber, postId);
                
                // then
                assertThat(request.getUserNumber()).isEqualTo(userNumber);
                assertThat(request.getPostId()).isEqualTo(postId);
            }
        }
        
        @Test
        @DisplayName("같은 게시글에 여러 사용자가 좋아요 요청을 생성한다")
        void createMultipleRequestsForSamePost() {
            // given
            Long postId = 1L;
            int[] userNumbers = {20230857, 20230858, 20230859};
            
            for (int userNumber : userNumbers) {
                // when
                LikeRequest request = new LikeRequest(userNumber, postId);
                
                // then
                assertThat(request.getPostId()).isEqualTo(postId);
                assertThat(request.getUserNumber()).isEqualTo(userNumber);
            }
        }
        
        @Test
        @DisplayName("큰 게시글 ID로 요청 객체를 생성한다")
        void createWithLargePostId() {
            // given & when
            LikeRequest request = new LikeRequest(20230857, Long.MAX_VALUE);
            
            // then
            assertThat(request.getPostId()).isEqualTo(Long.MAX_VALUE);
        }
        
        @Test
        @DisplayName("equals()와 hashCode()가 올바르게 동작한다")
        void testEqualsAndHashCode() {
            // given
            LikeRequest request1 = new LikeRequest(20230857, 1L);
            LikeRequest request2 = new LikeRequest(20230857, 1L);
            LikeRequest request3 = new LikeRequest(20230858, 1L);
            
            // then
            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
            assertThat(request1).isNotEqualTo(request3);
        }
        
        @Test
        @DisplayName("toString()이 올바르게 동작한다")
        void testToString() {
            // given
            LikeRequest request = new LikeRequest(20230857, 1L);
            
            // when
            String toString = request.toString();
            
            // then
            assertThat(toString).contains("20230857");
            assertThat(toString).contains("1");
        }
    }
}