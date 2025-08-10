package org.classreviewsite.user.controller.data.request;

import lombok.Data;

@Data
public class LoginUserRequest {

    private int userNumber;

    private String password;

}
