package com.lamar.primebox.web.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SharedShareDto {

    private String fileId;
    private String sharedUserUsername;
    private long sharedTime;
    private String message;
    
}
