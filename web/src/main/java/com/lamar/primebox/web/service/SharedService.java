package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.SharedDto;
import com.lamar.primebox.web.dto.model.SharedShareDto;
import com.lamar.primebox.web.dto.model.SharedUnshareDto;

import java.util.List;

public interface SharedService {

    List<SharedDto> getAllUserSharedFiles(String username);

    SharedDto share(SharedShareDto sharedShareDto) throws Exception;

    SharedDto unshare(SharedUnshareDto sharedUnshareDto) throws Exception;

}
