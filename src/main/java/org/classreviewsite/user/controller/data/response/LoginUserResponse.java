package org.classreviewsite.user.controller.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserResponse {

    private String name;

    private String token;

    private String userKey;


    public static LoginUserResponse of( String name , String token , String userKey){
        return new LoginUserResponse( name, token, userKey);
    }

}
