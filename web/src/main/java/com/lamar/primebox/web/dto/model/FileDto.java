package com.lamar.primebox.web.dto.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDto {

    private String fileID;
    private String filename;
    private String type;
    private long size;
    private long lastModified;

}
