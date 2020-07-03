package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.SharedFile;

import java.util.List;

public interface SharedFileDao {

    List<SharedFile> getAllSharedFiles(String username);

    SharedFile getSharedFile(String fileId, String username);

    void saveSharedFile(SharedFile sharedFile);

    void deleteSharedFile(SharedFile sharedFile);

}
