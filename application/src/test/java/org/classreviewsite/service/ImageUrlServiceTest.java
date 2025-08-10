package org.classreviewsite.service;

import org.classreviewsite.lecture.infrastructure.ImageUrl;
import org.classreviewsite.review.infrastructure.ImageUrlDataRepository;
import org.classreviewsite.review.service.ImageUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ImageUrlServiceTest {

    @InjectMocks ImageUrlService imageUrlService;
    @Mock ImageUrlDataRepository imageUrlDataRepository;

    @Nested
    @DisplayName("이미지 URL 조회 테스트")
    class findByIdTest {
        
        @Test
        @DisplayName("존재하는 ID로 조회 시, 이미지 URL을 반환한다")
        void findByIdSuccess() {
            // given
            Long imageId = 1L;
            ImageUrl expectedImageUrl = new ImageUrl(imageId, "프로필이미지", "https://example.com/profile.jpg");
            
            given(imageUrlDataRepository.findById(imageId)).willReturn(Optional.of(expectedImageUrl));
            
            // when
            ImageUrl result = imageUrlService.findById(imageId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getImageNumber()).isEqualTo(imageId);
            assertThat(result.getImageName()).isEqualTo("프로필이미지");
            assertThat(result.getImageUrl()).isEqualTo("https://example.com/profile.jpg");
        }
        
        @Test
        @DisplayName("존재하지 않는 ID로 조회 시, NoSuchElementException을 발생한다")
        void findByIdNotFound() {
            // given
            Long imageId = 999L;
            given(imageUrlDataRepository.findById(imageId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> imageUrlService.findById(imageId))
                    .isInstanceOf(NoSuchElementException.class);
        }
        
        @Test
        @DisplayName("null ID로 조회 시, NoSuchElementException을 발생한다")
        void findByIdWithNull() {
            // given
            Long imageId = null;
            given(imageUrlDataRepository.findById(imageId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> imageUrlService.findById(imageId))
                    .isInstanceOf(NoSuchElementException.class);
        }
        
        @Test
        @DisplayName("음수 ID로 조회 시, NoSuchElementException을 발생한다")
        void findByIdWithNegative() {
            // given
            Long imageId = -1L;
            given(imageUrlDataRepository.findById(imageId)).willReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> imageUrlService.findById(imageId))
                    .isInstanceOf(NoSuchElementException.class);
        }
        
        @Test
        @DisplayName("기본 프로필 이미지 조회 시, 정상적으로 반환한다")
        void findDefaultProfileImage() {
            // given
            Long imageId = 1L;
            ImageUrl defaultImage = new ImageUrl(imageId, "기본프로필", "https://example.com/default-profile.jpg");
            
            given(imageUrlDataRepository.findById(imageId)).willReturn(Optional.of(defaultImage));
            
            // when
            ImageUrl result = imageUrlService.findById(imageId);
            
            // then
            assertThat(result).isNotNull();
            assertThat(result.getImageName()).isEqualTo("기본프로필");
            assertThat(result.getImageUrl()).contains("default-profile");
        }
        
        @Test
        @DisplayName("다양한 이미지 타입 조회 시, 모두 정상적으로 반환한다")
        void findVariousImageTypes() {
            // given
            Long jpgImageId = 1L;
            Long pngImageId = 2L;
            Long gifImageId = 3L;
            
            ImageUrl jpgImage = new ImageUrl(jpgImageId, "JPG이미지", "https://example.com/image.jpg");
            ImageUrl pngImage = new ImageUrl(pngImageId, "PNG이미지", "https://example.com/image.png");
            ImageUrl gifImage = new ImageUrl(gifImageId, "GIF이미지", "https://example.com/image.gif");
            
            given(imageUrlDataRepository.findById(jpgImageId)).willReturn(Optional.of(jpgImage));
            given(imageUrlDataRepository.findById(pngImageId)).willReturn(Optional.of(pngImage));
            given(imageUrlDataRepository.findById(gifImageId)).willReturn(Optional.of(gifImage));
            
            // when
            ImageUrl jpgResult = imageUrlService.findById(jpgImageId);
            ImageUrl pngResult = imageUrlService.findById(pngImageId);
            ImageUrl gifResult = imageUrlService.findById(gifImageId);
            
            // then
            assertThat(jpgResult.getImageUrl()).endsWith(".jpg");
            assertThat(pngResult.getImageUrl()).endsWith(".png");
            assertThat(gifResult.getImageUrl()).endsWith(".gif");
        }
    }
}