package com.lamar.primebox.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDeleteResponse {

    private String fileId;
    private String filename;
    private String type;
    private long size;
    private long lastModified;
    private long userStored;

}
