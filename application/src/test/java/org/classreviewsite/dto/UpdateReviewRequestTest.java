package org.classreviewsite.dto;

import org.classreviewsite.review.controller.data.Request.UpdateReviewRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateReviewRequestTest {

    @Nested
    @DisplayName("UpdateReviewRequest 생성 테스트")
    class createUpdateReviewRequestTest {
        
        @Test
        @DisplayName("모든 필드를 포함하여 요청 객체를 생성한다")
        void createWithAllFields() {
            // given
            Long postId = 1L;
            String postTitle = "수정된 제목";
            String postContent = "수정된 내용";
            Double starRating = 4.0;
            Long userNumber = 20230857L;
            
            // when
            UpdateReviewRequest request = new UpdateReviewRequest(
                    postId, postTitle, postContent, starRating, userNumber
            );
            
            // then
            assertThat(request.getPostId()).isEqualTo(postId);
            assertThat(request.getPostTitle()).isEqualTo(postTitle);
            assertThat(request.getPostContent()).isEqualTo(postContent);
            assertThat(request.getStarLating()).isEqualTo(starRating);
            assertThat(request.getUserNumber()).isEqualTo(userNumber);
        }
        
        @Test
        @DisplayName("기본 생성자를 사용하여 빈 요청 객체를 생성한다")
        void createWithNoArgsConstructor() {
            // when
            UpdateReviewRequest request = new UpdateReviewRequest();
            
            // then
            assertThat(request.getPostId()).isNull();
            assertThat(request.getPostTitle()).isNull();
            assertThat(request.getPostContent()).isNull();
            assertThat(request.getStarLating()).isNull();
            assertThat(request.getUserNumber()).isNull();
        }
        
        @Test
        @DisplayName("정적 팩토리 메서드 of()를 사용하여 요청 객체를 생성한다")
        void createWithOfMethod() {
            // given
            Long postId = 2L;
            String postTitle = "팩토리로 생성된 제목";
            String postContent = "팩토리로 생성된 내용";
            Double starRating = 3.5;
            Long userNumber = 20230858L;
            
            // when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    postId, postTitle, postContent, starRating, userNumber
            );
            
            // then
            assertThat(request.getPostId()).isEqualTo(postId);
            assertThat(request.getPostTitle()).isEqualTo(postTitle);
            assertThat(request.getPostContent()).isEqualTo(postContent);
            assertThat(request.getStarLating()).isEqualTo(starRating);
            assertThat(request.getUserNumber()).isEqualTo(userNumber);
        }
        
        @Test
        @DisplayName("평점을 높게 수정하는 요청 객체를 생성한다")
        void createWithHigherRating() {
            // given & when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, "평점 상향 수정", "더 좋아졌습니다", 5.0, 20230857L
            );
            
            // then
            assertThat(request.getStarLating()).isEqualTo(5.0);
            assertThat(request.getPostTitle()).contains("상향");
        }
        
        @Test
        @DisplayName("평점을 낮게 수정하는 요청 객체를 생성한다")
        void createWithLowerRating() {
            // given & when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, "평점 하향 수정", "생각보다 아쉬웠습니다", 2.0, 20230857L
            );
            
            // then
            assertThat(request.getStarLating()).isEqualTo(2.0);
            assertThat(request.getPostTitle()).contains("하향");
        }
        
        @Test
        @DisplayName("제목만 수정하는 요청 객체를 생성한다")
        void createWithTitleOnlyUpdate() {
            // given
            String originalContent = "기존 내용 유지";
            Double originalRating = 4.0;
            
            // when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, "제목만 수정됨", originalContent, originalRating, 20230857L
            );
            
            // then
            assertThat(request.getPostTitle()).isEqualTo("제목만 수정됨");
            assertThat(request.getPostContent()).isEqualTo(originalContent);
            assertThat(request.getStarLating()).isEqualTo(originalRating);
        }
        
        @Test
        @DisplayName("내용만 수정하는 요청 객체를 생성한다")
        void createWithContentOnlyUpdate() {
            // given
            String originalTitle = "기존 제목 유지";
            Double originalRating = 4.0;
            
            // when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, originalTitle, "내용만 새롭게 수정되었습니다", originalRating, 20230857L
            );
            
            // then
            assertThat(request.getPostTitle()).isEqualTo(originalTitle);
            assertThat(request.getPostContent()).isEqualTo("내용만 새롭게 수정되었습니다");
            assertThat(request.getStarLating()).isEqualTo(originalRating);
        }
        
        @Test
        @DisplayName("긴 내용으로 수정하는 요청 객체를 생성한다")
        void createWithLongContentUpdate() {
            // given
            String longContent = "이번 수정에서는 강의에 대한 더 자세한 후기를 작성하고자 합니다. " +
                    "처음 들었을 때는 단순히 좋다고만 생각했지만, 시간이 지나면서 이 강의가 얼마나 " +
                    "체계적이고 실무에 도움이 되는지 깨달았습니다. 특히 프로젝트 과제들이 매우 의미있었고, " +
                    "교수님의 피드백도 구체적이어서 많은 도움이 되었습니다.";
            
            // when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, "상세한 후기로 수정", longContent, 4.8, 20230857L
            );
            
            // then
            assertThat(request.getPostContent()).isEqualTo(longContent);
            assertThat(request.getPostContent().length()).isGreaterThan(200);
        }
        
        @Test
        @DisplayName("다양한 게시글 ID로 수정 요청 객체를 생성한다")
        void createWithVariousPostIds() {
            // given
            Long[] postIds = {1L, 100L, 999L, 1000L};
            Long userNumber = 20230857L;
            
            for (Long postId : postIds) {
                // when
                UpdateReviewRequest request = UpdateReviewRequest.of(
                        postId, "수정된 제목", "수정된 내용", 4.0, userNumber
                );
                
                // then
                assertThat(request.getPostId()).isEqualTo(postId);
                assertThat(request.getUserNumber()).isEqualTo(userNumber);
            }
        }
        
        @Test
        @DisplayName("8자리 학번으로 수정 요청 객체를 생성한다")
        void createWithValidStudentNumber() {
            // given & when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, "수정된 제목", "수정된 내용", 4.0, 20240841L
            );
            
            // then
            assertThat(request.getUserNumber()).isEqualTo(20240841L);
            assertThat(String.valueOf(request.getUserNumber())).hasSize(8);
        }
        
        @Test
        @DisplayName("setter를 사용하여 필드를 수정한다")
        void modifyFieldsWithSetter() {
            // given
            UpdateReviewRequest request = new UpdateReviewRequest();
            
            // when
            request.setPostId(1L);
            request.setPostTitle("setter로 설정된 제목");
            request.setPostContent("setter로 설정된 내용");
            request.setStarLating(3.0);
            request.setUserNumber(20230857L);
            
            // then
            assertThat(request.getPostId()).isEqualTo(1L);
            assertThat(request.getPostTitle()).isEqualTo("setter로 설정된 제목");
            assertThat(request.getPostContent()).isEqualTo("setter로 설정된 내용");
            assertThat(request.getStarLating()).isEqualTo(3.0);
            assertThat(request.getUserNumber()).isEqualTo(20230857L);
        }
        
        @Test
        @DisplayName("부분적으로 null 값을 포함한 수정 요청 객체를 생성한다")
        void createWithPartialNullValues() {
            // given & when
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, null, "내용만 수정", null, 20230857L
            );
            
            // then
            assertThat(request.getPostId()).isEqualTo(1L);
            assertThat(request.getPostTitle()).isNull();
            assertThat(request.getPostContent()).isEqualTo("내용만 수정");
            assertThat(request.getStarLating()).isNull();
            assertThat(request.getUserNumber()).isEqualTo(20230857L);
        }
        
        @Test
        @DisplayName("equals()와 hashCode()가 올바르게 동작한다")
        void testEqualsAndHashCode() {
            // given
            UpdateReviewRequest request1 = UpdateReviewRequest.of(
                    1L, "제목", "내용", 4.0, 20230857L
            );
            UpdateReviewRequest request2 = UpdateReviewRequest.of(
                    1L, "제목", "내용", 4.0, 20230857L
            );
            UpdateReviewRequest request3 = UpdateReviewRequest.of(
                    2L, "제목", "내용", 4.0, 20230857L
            );
            
            // then
            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
            assertThat(request1).isNotEqualTo(request3);
        }
        
        @Test
        @DisplayName("toString()이 올바르게 동작한다")
        void testToString() {
            // given
            UpdateReviewRequest request = UpdateReviewRequest.of(
                    1L, "테스트 제목", "테스트 내용", 4.0, 20230857L
            );
            
            // when
            String toString = request.toString();
            
            // then
            assertThat(toString).contains("1");
            assertThat(toString).contains("테스트 제목");
            assertThat(toString).contains("20230857");
        }
    }
}