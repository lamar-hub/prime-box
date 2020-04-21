package com.lamar.primebox.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserDeleteResponse implements Serializable {

    private String userID;
    private String email;
    private String name;
    private String surname;
    private long stored;
    private long limit;
    
}
