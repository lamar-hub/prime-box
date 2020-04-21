package com.lamar.primebox.web.dto.response;

import com.lamar.primebox.web.dto.model.FileDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class FileGetAllResponse implements Serializable {
    
    private List<FileDto> files;
    
}
