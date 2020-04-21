package com.lamar.primebox.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserUpdateRequest implements Serializable {

    private String password;
    private String name;
    private String surname;
    
}
