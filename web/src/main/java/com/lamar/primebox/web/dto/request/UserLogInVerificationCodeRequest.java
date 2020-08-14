package com.lamar.primebox.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserLogInVerificationCodeRequest implements Serializable {

    @Email
    @NotBlank
    private String email;

    @Size(min = 6, max = 20)
    @NotBlank
    private String password;

    @Size(min = 4, max = 4)
    @NotBlank
    private String verificationCode;

}
