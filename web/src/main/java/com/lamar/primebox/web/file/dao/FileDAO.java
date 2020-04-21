package com.lamar.primebox.web.file.dao;

import com.lamar.primebox.web.file.entity.File;

import java.util.List;

public interface FileDAO {

    List<File> getAllUserFiles(String userID);

    void saveFile(File file);

    File getFile(String fileID);

    File updateFile(File file);

    void deleteFile(File file);

}
