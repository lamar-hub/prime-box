package com.lamar.primebox.web.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SharedFileShareRequest {

    private String fileId;
    private String sharedUserUsername;
    private long sharedTime;
    private String message;

}
