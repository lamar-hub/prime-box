package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.File;

import java.util.List;

public interface FileDao {

    List<File> getAllUserFiles(String username);

    void saveFile(File file);

    File getFile(String fileId);

    void deleteFile(File file);

}
