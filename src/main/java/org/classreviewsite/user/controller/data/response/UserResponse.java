package org.classreviewsite.user.controller.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.classreviewsite.user.infrastructure.User;

@Data
@AllArgsConstructor
public class UserResponse {

    private int userNumber;
    private String username;
    private String profile;

    public static UserResponse from(User user){
        return new UserResponse(
                user.getUserNumber(),
                user.getNickname(),
                "https://raw.githubusercontent.com/UbSE-lab/class-review-site-page/image/profile.png"
        );
    }


}
