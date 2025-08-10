package org.classreviewsite.lecture.controller;

import lombok.RequiredArgsConstructor;
import org.classreviewsite.review.controller.data.Response.BannerResponse;
import org.classreviewsite.review.service.ImageUrlService;
import org.classreviewsite.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageUrlController {

    private final ImageUrlService imageUrlService;

    @GetMapping("/banner")
    public Result BannerRender(){
        List<BannerResponse> response = imageUrlService.imageRender();
        return new Result(200, response, "배너 이미지 리스트 입니다.");
    }

}
