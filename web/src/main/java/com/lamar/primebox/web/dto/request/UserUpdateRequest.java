package com.lamar.primebox.web.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateRequest implements Serializable {

    @NotBlank
    @Pattern(regexp = "^3816[0-5][0-9]{6,7}$")
    private String phone;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean twoFactorVerification;

}
