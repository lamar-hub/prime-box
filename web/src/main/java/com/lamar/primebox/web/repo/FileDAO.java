package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.File;

import java.util.List;

public interface FileDAO {

    List<File> getAllUserFiles(String username);

    void saveFile(File file);

    File getFile(String fileID);

    File updateFile(File file);

    void deleteFile(File file);

}
