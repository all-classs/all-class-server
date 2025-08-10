package org.classreviewsite.review.controller.data.Request;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRequest {

    private int userNumber;

    private Long postId;

}
