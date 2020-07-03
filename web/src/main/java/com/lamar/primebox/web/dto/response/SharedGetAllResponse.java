package com.lamar.primebox.web.dto.response;

import com.lamar.primebox.web.dto.model.SharedFileDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SharedGetAllResponse {

    private List<SharedFileDto> sharedFiles;

}
