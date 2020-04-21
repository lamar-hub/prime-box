package com.lamar.primebox.web.shared.service;

import com.lamar.primebox.web.shared.entity.Shared;

import java.util.List;

public interface SharedService {

    String getUserIDFromToken(String token);

    List<Shared> getAllUserSharedFiles(String userID);

    void share(Shared shared);

    void unshare(String userID, String fileID) throws Exception;

}
