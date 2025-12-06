package org.classreviewsite.domain;

import org.classreviewsite.domain.lecture.ImageUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageUrlTest {

    @Nested
    @DisplayName("ImageUrl 엔티티 생성 테스트")
    class createImageUrlTest {
        
        @Test
        @DisplayName("모든 필드를 포함하여 이미지 URL을 생성한다")
        void createImageUrlWithAllFields() {
            // given
            Long imageNumber = 1L;
            String imageName = "프로필이미지";
            String imageUrl = "https://example.com/profile.jpg";
            
            // when
            ImageUrl image = new ImageUrl(imageNumber, imageName, imageUrl);
            
            // then
            assertThat(image).isNotNull();
            assertThat(image.getImageNumber()).isEqualTo(imageNumber);
            assertThat(image.getImageName()).isEqualTo(imageName);
            assertThat(image.getImageUrl()).isEqualTo(imageUrl);
        }
        
        @Test
        @DisplayName("빌더를 사용하여 이미지 URL을 생성한다")
        void createImageUrlWithBuilder() {
            // given & when
            ImageUrl image = ImageUrl.builder()
                    .imageNumber(2L)
                    .imageName("배경이미지")
                    .imageUrl("https://example.com/background.png")
                    .build();
            
            // then
            assertThat(image.getImageNumber()).isEqualTo(2L);
            assertThat(image.getImageName()).isEqualTo("배경이미지");
            assertThat(image.getImageUrl()).isEqualTo("https://example.com/background.png");
        }
        
        @Test
        @DisplayName("기본 생성자로 이미지 URL을 생성한다")
        void createImageUrlWithNoArgsConstructor() {
            // when
            ImageUrl image = new ImageUrl();
            
            // then
            assertThat(image).isNotNull();
            assertThat(image.getImageNumber()).isNull();
            assertThat(image.getImageName()).isNull();
            assertThat(image.getImageUrl()).isNull();
        }
        
        @Test
        @DisplayName("JPG 이미지 URL을 생성한다")
        void createJpgImageUrl() {
            // given & when
            ImageUrl image = new ImageUrl(1L, "JPG이미지", "https://example.com/image.jpg");
            
            // then
            assertThat(image.getImageUrl()).endsWith(".jpg");
            assertThat(image.getImageName()).isEqualTo("JPG이미지");
        }
        
        @Test
        @DisplayName("PNG 이미지 URL을 생성한다")
        void createPngImageUrl() {
            // given & when
            ImageUrl image = new ImageUrl(2L, "PNG이미지", "https://example.com/image.png");
            
            // then
            assertThat(image.getImageUrl()).endsWith(".png");
            assertThat(image.getImageName()).isEqualTo("PNG이미지");
        }
        
        @Test
        @DisplayName("GIF 이미지 URL을 생성한다")
        void createGifImageUrl() {
            // given & when
            ImageUrl image = new ImageUrl(3L, "GIF이미지", "https://example.com/image.gif");
            
            // then
            assertThat(image.getImageUrl()).endsWith(".gif");
            assertThat(image.getImageName()).isEqualTo("GIF이미지");
        }
        
        @Test
        @DisplayName("다양한 도메인의 이미지 URL을 생성한다")
        void createImageUrlsFromVariousDomains() {
            // given
            String[] domains = {
                "https://cdn.example.com/image1.jpg",
                "https://storage.amazonaws.com/bucket/image2.png",
                "https://imgur.com/image3.gif",
                "https://drive.google.com/image4.jpg",
                "https://dropbox.com/image5.png"
            };
            
            for (int i = 0; i < domains.length; i++) {
                // when
                ImageUrl image = new ImageUrl((long) (i + 1), "이미지" + (i + 1), domains[i]);
                
                // then
                assertThat(image.getImageUrl()).isEqualTo(domains[i]);
                assertThat(image.getImageUrl()).startsWith("https://");
                assertThat(image.getImageName()).isEqualTo("이미지" + (i + 1));
            }
        }
        
        @Test
        @DisplayName("상대 경로 이미지 URL을 생성한다")
        void createRelativePathImageUrl() {
            // given & when
            ImageUrl image = new ImageUrl(1L, "로컬이미지", "/static/images/local.jpg");
            
            // then
            assertThat(image.getImageUrl()).startsWith("/");
            assertThat(image.getImageUrl()).contains("static");
            assertThat(image.getImageName()).isEqualTo("로컬이미지");
        }
        
        @Test
        @DisplayName("기본 프로필 이미지 URL을 생성한다")
        void createDefaultProfileImageUrl() {
            // given & when
            ImageUrl image = new ImageUrl(1L, "기본프로필", "https://example.com/default-profile.jpg");
            
            // then
            assertThat(image.getImageName()).contains("기본");
            assertThat(image.getImageUrl()).contains("default");
            assertThat(image.getImageUrl()).contains("profile");
        }
        
        @Test
        @DisplayName("null 값으로 이미지 URL을 생성한다")
        void createImageUrlWithNullValues() {
            // given & when
            ImageUrl image = new ImageUrl(1L, null, null);
            
            // then
            assertThat(image.getImageNumber()).isEqualTo(1L);
            assertThat(image.getImageName()).isNull();
            assertThat(image.getImageUrl()).isNull();
        }
        
        @Test
        @DisplayName("빈 문자열로 이미지 URL을 생성한다")
        void createImageUrlWithEmptyStrings() {
            // given & when
            ImageUrl image = new ImageUrl(1L, "", "");
            
            // then
            assertThat(image.getImageNumber()).isEqualTo(1L);
            assertThat(image.getImageName()).isEqualTo("");
            assertThat(image.getImageUrl()).isEqualTo("");
        }
        
        @Test
        @DisplayName("긴 이름과 URL로 이미지를 생성한다")
        void createImageUrlWithLongValues() {
            // given
            String longImageName = "매우_긴_이미지_이름_테스트용_이미지_파일_이름입니다";
            String longImageUrl = "https://very-long-domain-name-for-testing.example.com/very/long/path/to/image/file/with/very/long/filename.jpg";
            
            // when
            ImageUrl image = new ImageUrl(1L, longImageName, longImageUrl);
            
            // then
            assertThat(image.getImageName()).isEqualTo(longImageName);
            assertThat(image.getImageUrl()).isEqualTo(longImageUrl);
            assertThat(image.getImageName().length()).isLessThanOrEqualTo(255); // DB 제약사항 확인
            assertThat(image.getImageUrl().length()).isLessThanOrEqualTo(255); // DB 제약사항 확인
        }
        
        @Test
        @DisplayName("숫자로만 구성된 이미지 이름을 생성한다")
        void createImageUrlWithNumericName() {
            // given & when
            ImageUrl image = new ImageUrl(1L, "123456789", "https://example.com/123456789.jpg");
            
            // then
            assertThat(image.getImageName()).isEqualTo("123456789");
            assertThat(image.getImageUrl()).contains("123456789");
        }
        
        @Test
        @DisplayName("특수문자가 포함된 이미지 이름을 생성한다")
        void createImageUrlWithSpecialCharacters() {
            // given & when
            ImageUrl image = new ImageUrl(1L, "이미지_파일-2023.jpg", "https://example.com/이미지_파일-2023.jpg");
            
            // then
            assertThat(image.getImageName()).contains("_");
            assertThat(image.getImageName()).contains("-");
            assertThat(image.getImageName()).contains(".");
        }
    }
}