package com.lamar.primebox.web.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class FileDownloadDto {

    private String fileID;
    private String filename;
    private String type;
    private long size;
    private long lastModified;
    private byte[] file;
    
}
