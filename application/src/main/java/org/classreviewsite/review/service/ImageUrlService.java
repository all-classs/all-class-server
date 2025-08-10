package org.classreviewsite.review.service;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.lecture.infrastructure.ImageUrl;
import org.classreviewsite.review.controller.data.Response.BannerResponse;
import org.classreviewsite.review.infrastructure.ImageUrlDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ImageUrlService {

    private final ImageUrlDataRepository imageUrlDataRepository;

    public List<BannerResponse> imageRender(){
        List<ImageUrl> list = imageUrlDataRepository.findByImageNameOrImageName("banner", "main-banner");
        if(list.isEmpty()){
            throw new NoSuchElementException("앗 배너를 찾지 못했어요.");
        }

        return list.stream().map(BannerResponse::from).toList();
    }

    public ImageUrl findById(Long id){
        return imageUrlDataRepository.findById(id).get();
    }

}
