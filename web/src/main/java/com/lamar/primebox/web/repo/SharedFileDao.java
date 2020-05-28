package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.SharedFile;

import java.util.List;

public interface SharedFileDao {

    List<SharedFile> getAllShared(String username);

    SharedFile getShared(String fileId, String username);

    SharedFile saveShared(SharedFile sharedFile);

    void deleteShared(SharedFile sharedFile);

}
