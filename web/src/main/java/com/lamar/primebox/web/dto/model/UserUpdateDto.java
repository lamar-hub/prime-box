package com.lamar.primebox.web.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserUpdateDto {

    private String username;
    private String phone;
    private String name;
    private String surname;
    private boolean active;
    private boolean twoFactorVerification;
    
}
