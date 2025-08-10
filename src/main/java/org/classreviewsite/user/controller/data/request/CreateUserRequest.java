package org.classreviewsite.user.controller.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

    private int userNumber;

    private String password;

    private String userName;

    private String department;

    private String nickname;

    private String userType;


}
