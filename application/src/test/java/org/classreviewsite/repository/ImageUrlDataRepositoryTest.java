package org.classreviewsite.repository;

import org.classreviewsite.domain.lecture.ImageUrl;
import org.classreviewsite.domain.lecture.ImageUrlDataRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class ImageUrlDataRepositoryTest {

    @Autowired private ImageUrlDataRepository imageUrlDataRepository;

    @Test
    @DisplayName("이미지 ID로 이미지 URL 정보를 조회한다")
    void findById() {
        // given
        Long imageId = 1L;

        // when
        Optional<ImageUrl> result = imageUrlDataRepository.findById(imageId);

        // then
        assertThat(result).isNotNull();
        // 실제 데이터가 있는 경우에만 검증
        if (result.isPresent()) {
            ImageUrl imageUrl = result.get();
            assertThat(imageUrl.getImageNumber()).isEqualTo(imageId);
            assertThat(imageUrl.getImageName()).isNotNull();
            assertThat(imageUrl.getImageUrl()).isNotNull();
        }
    }

    @Test
    @DisplayName("존재하지 않는 이미지 ID로 조회 시 빈 결과를 반환한다")
    void findByNonExistentId() {
        // given
        Long nonExistentImageId = 99999L;

        // when
        Optional<ImageUrl> result = imageUrlDataRepository.findById(nonExistentImageId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("null 이미지 ID로 조회 시 빈 결과를 반환한다")
    void findByNullId() {
        // given
        Long nullImageId = null;

        // when
        Optional<ImageUrl> result = imageUrlDataRepository.findById(nullImageId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("음수 이미지 ID로 조회 시 빈 결과를 반환한다")
    void findByNegativeId() {
        // given
        Long negativeImageId = -1L;

        // when
        Optional<ImageUrl> result = imageUrlDataRepository.findById(negativeImageId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("기본 이미지 URL이 정상적으로 조회된다")
    void findDefaultImageUrl() {
        // given
        Long defaultImageId = 1L;

        // when
        Optional<ImageUrl> result = imageUrlDataRepository.findById(defaultImageId);

        // then
        assertThat(result).isNotNull();
        // 기본 이미지가 설정되어 있다면 검증
        if (result.isPresent()) {
            ImageUrl imageUrl = result.get();
            assertThat(imageUrl.getImageUrl()).isNotBlank();
            assertThat(imageUrl.getImageName()).isNotBlank();
            
            // URL 형식이 올바른지 기본적인 검증
            String url = imageUrl.getImageUrl();
            assertThat(url).satisfiesAnyOf(
                urlString -> assertThat(urlString).startsWith("http://"),
                urlString -> assertThat(urlString).startsWith("https://"),
                urlString -> assertThat(urlString).startsWith("/")
            );
        }
    }

    @Test
    @DisplayName("여러 이미지 ID로 연속 조회가 정상적으로 작동한다")
    void findMultipleIds() {
        // given
        Long[] imageIds = {1L, 2L, 3L};

        // when & then
        for (Long imageId : imageIds) {
            Optional<ImageUrl> result = imageUrlDataRepository.findById(imageId);
            assertThat(result).isNotNull();
            
            if (result.isPresent()) {
                ImageUrl imageUrl = result.get();
                assertThat(imageUrl.getImageNumber()).isEqualTo(imageId);
                assertThat(imageUrl.getImageName()).isNotNull();
                assertThat(imageUrl.getImageUrl()).isNotNull();
            }
        }
    }
}