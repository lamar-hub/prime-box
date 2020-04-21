package com.lamar.primebox.web.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 3670513215564910224L;

    private String userID;
    private String email;
    private String password;
    private String name;
    private String surname;
    private long stored;
    private long limit;
    private String token;
    private long expireIn;

}
