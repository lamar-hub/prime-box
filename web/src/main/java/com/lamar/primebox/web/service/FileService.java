package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.FileDownloadDto;
import com.lamar.primebox.web.dto.model.FileDto;
import com.lamar.primebox.web.dto.model.FileSaveDto;
import com.lamar.primebox.web.dto.model.FileUpdateDto;
import com.lamar.primebox.web.model.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileDto> getAllUserFiles(String username);

    FileSaveDto saveFileDatabase(MultipartFile multipartFile, String username) throws Exception;

    FileDto updateFile(FileUpdateDto fileUpdateDto) throws Exception;

    FileDownloadDto getFile(String fileID) throws Exception;

    FileSaveDto deleteFile(String fileId) throws Exception;

}
