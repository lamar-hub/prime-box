package com.lamar.primebox.web.file.service;

import com.lamar.primebox.web.file.entity.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    String getUserIDFromToken(String token);

    List<File> getAllUserFiles(String userID);

    File saveFileDatabase(MultipartFile multipartFile, String userID) throws Exception;

    void saveFileDisk(MultipartFile multipartFile, File file) throws IOException;

    void updateFile(File file);

    Pair<File, ByteArrayResource> getFile(String fileID) throws IOException;

    File deleteFileDatabase(String fileID) throws Exception;

    void deleteFileDisk(File file) throws IOException;

}
