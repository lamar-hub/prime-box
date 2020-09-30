package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.FileDownloadDto;
import com.lamar.primebox.web.dto.model.FileDto;
import com.lamar.primebox.web.dto.model.FileSaveDeleteDto;
import com.lamar.primebox.web.dto.model.FileUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<FileDto> getAllUserFiles(String username);

    FileSaveDeleteDto saveFile(MultipartFile multipartFile, String username) throws Exception;

    FileDownloadDto getFile(String fileID) throws Exception;

    FileSaveDeleteDto deleteFile(String fileId) throws Exception;

}
