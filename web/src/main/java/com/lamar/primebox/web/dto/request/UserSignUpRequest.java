package com.lamar.primebox.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserSignUpRequest implements Serializable {

    @Email
    @NotBlank
    private String email;

    @Size(min = 6, max = 20)
    @NotBlank
    private String password;

    @NotBlank
    @Pattern(regexp = "^3816[0-5][0-9]{6,7}$")
    private String phone;
    
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

}
