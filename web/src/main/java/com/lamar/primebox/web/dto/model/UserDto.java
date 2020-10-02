package com.lamar.primebox.web.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserDto {

    private String userId;
    private String email;
    private String password;
    private String name;
    private String surname;
    private long stored;
    private long limit;
    private boolean active;
    private boolean twoFactorVerification;

}
