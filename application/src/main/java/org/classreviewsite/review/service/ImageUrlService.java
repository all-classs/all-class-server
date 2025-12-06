package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.lecture.ImageUrl;
import org.classreviewsite.domain.lecture.ImageUrlDataRepository;
import org.springframework.stereotype.Service;

/**
 * @see 현재 문제점.
 * ImageUrl이 사용되지 않고있다. 삭제권장.
 */
@Service
@RequiredArgsConstructor
public class ImageUrlService {

    private final ImageUrlDataRepository imageUrlDataRepository;

    public ImageUrl findById(Long id){
        return imageUrlDataRepository.findById(id).get();
    }
}
