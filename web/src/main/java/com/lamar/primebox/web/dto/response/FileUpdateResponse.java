package com.lamar.primebox.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileUpdateResponse {

    private String fileID;
    private String filename;
    private String type;
    private long size;
    private long lastModified;

}
