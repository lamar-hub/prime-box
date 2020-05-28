package com.lamar.primebox.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SharedFileShareResponse {

    private String fileId;
    private String filename;
    private long size;
    private long lastModified;
    private String sharedUserUsername;
    private String sharedUserName;
    private String sharedUserSurname;
    private String message;
    private long date;
    
}
