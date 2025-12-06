package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.domain.lecture.ImageUrl;
import org.classreviewsite.domain.lecture.ImageUrlDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ImageUrlService {

    private final ImageUrlDataRepository imageUrlDataRepository;

    public ImageUrl findById(Long id){
        return imageUrlDataRepository.findById(id).get();
    }
}
