package com.lamar.primebox.web.shared.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SharedDTO implements Serializable {

    private static final long serialVersionUID = 7973255189958552242L;

    private String sharedFileFileID;
    private String sharedFileFilename;
    private long sharedFileSize;
    private long sharedFileLastModified;
    private String sharedFileUserEmail;
    private String sharedFileUserName;
    private String sharedFileUserSurname;
    private String message;
    private long date;

}
