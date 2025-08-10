package org.classreviewsite.review.controller.data.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.classreviewsite.lecture.infrastructure.ImageUrl;

@Data
@AllArgsConstructor
public class BannerResponse {

    private String name;
    private String url;


    public static BannerResponse from(ImageUrl imageUrl){
        return new BannerResponse(
                imageUrl.getImageName(),
                imageUrl.getImageUrl()
        );
    }

}
