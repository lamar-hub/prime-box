package com.lamar.primebox.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class FileUpdateRequest {

    @NotBlank
    private String fileId;

    @NotBlank
    private String filename;

}
