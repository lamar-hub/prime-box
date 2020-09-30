package com.lamar.primebox.web.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class VerificationCodeDto {
    
    private String codeId;
    private String code;
    private long created;
    private boolean active;

}
