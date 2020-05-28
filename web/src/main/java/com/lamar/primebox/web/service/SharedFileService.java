package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.SharedFileDto;
import com.lamar.primebox.web.dto.model.SharedFileShareDto;
import com.lamar.primebox.web.dto.model.SharedFileUnshareDto;

import java.util.List;

public interface SharedFileService {

    List<SharedFileDto> getAllUserSharedFiles(String username);

    SharedFileDto share(SharedFileShareDto sharedFileShareDto) throws Exception;

    SharedFileDto unshare(SharedFileUnshareDto sharedFileUnshareDto) throws Exception;

}
