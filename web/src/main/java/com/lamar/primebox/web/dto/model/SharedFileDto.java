package com.lamar.primebox.web.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SharedFileDto {

    private String sharedFileFileId;
    private String sharedFileFilename;
    private long sharedFileSize;
    private long sharedFileLastModified;
    private String sharedFileUserEmail;
    private String sharedFileUserName;
    private String sharedFileUserSurname;
    private String message;
    private long date;

}
