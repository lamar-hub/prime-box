package com.lamar.primebox.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SharedFileShareRequest {

    @NotBlank
    private String fileId;

    @NotBlank
    private String sharedUserUsername;

    @NotNull
    private long sharedTime;

    private String message;

}
