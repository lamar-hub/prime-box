package com.lamar.primebox.web.file.dto;

import com.lamar.primebox.web.auth.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class FileDTO implements Serializable {

    private static final long serialVersionUID = -7982422061633140519L;

    private String fileID;
    private String filename;
    private String type;
    private long size;
    private long lastModified;
    private User user;

}
