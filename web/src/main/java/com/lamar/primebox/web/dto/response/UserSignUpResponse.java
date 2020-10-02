package com.lamar.primebox.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserSignUpResponse implements Serializable {

    private String userId;
    private String email;
    private String name;
    private String surname;
    private long stored;
    private long limit;
    private boolean active;
    private boolean twoFactorVerification;

}
