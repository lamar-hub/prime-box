package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.FileDownloadDto;
import com.lamar.primebox.web.dto.model.FileDto;
import com.lamar.primebox.web.dto.model.FileSaveDto;
import com.lamar.primebox.web.dto.model.FileUpdateDto;
import com.lamar.primebox.web.model.File;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.FileDao;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.service.FileService;
import com.lamar.primebox.web.util.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileDao fileDao;
    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final StorageProperties storageProperties;

    public FileServiceImpl(FileDao fileDao, UserDao userDao, ModelMapper modelMapper, StorageProperties storageProperties) {
        this.fileDao = fileDao;
        this.userDao = userDao;
        this.modelMapper = modelMapper;
        this.storageProperties = storageProperties;
    }

    @Override
    @Transactional
    public List<FileDto> getAllUserFiles(String username) {
        List<File> files = fileDao.getAllUserFiles(username);
        return files.stream().map(file -> modelMapper.map(file, FileDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FileSaveDto saveFileDatabase(MultipartFile multipartFile, String username) throws Exception {
        File file = new File(multipartFile.getOriginalFilename(), multipartFile.getContentType(), (int) multipartFile.getSize(), new Date().getTime());
        User user = userDao.getUser(username);
        if (user.getStored() + file.getSize() > user.getLimit()) {
            throw new Exception("There are no space anymore!");
        }
        file.setUser(user);
        fileDao.saveFile(file);
        saveFileDisk(multipartFile, file);
        user.setStored(user.getStored() + file.getSize());
        modelMapper.map(file, FileSaveDto.class).setUserStored(user.getStored());
        return modelMapper.map(file, FileSaveDto.class).setUserStored(user.getStored());
    }

    private void saveFileDisk(MultipartFile multipartFile, File file) throws IOException {
        Path filePath = Path.of(storageProperties.getPath(), file.getFileId());
        Files.copy(multipartFile.getInputStream(), filePath);
    }

    @Override
    @Transactional
    public FileDto updateFile(FileUpdateDto fileUpdateDto) throws Exception {
        File file = fileDao.getFile(fileUpdateDto.getFileID());
        if (file != null) {
            file.setFilename(fileUpdateDto.getFilename());
            return modelMapper.map(file, FileDto.class);
        }
        throw new Exception("Exception! File doesn't exist!");
    }

    @Override
    @Transactional
    public FileDownloadDto getFile(String fileID) throws Exception {
        File file = fileDao.getFile(fileID);
        if (file != null) {
            return modelMapper.map(file, FileDownloadDto.class).setFile(getFileFromDisk(file.getFileId()));
        }
        throw new Exception("Exception! File doesn't exist!");
    }

    private byte[] getFileFromDisk(String fileId) throws IOException {
        Path filePath = Path.of(storageProperties.getPath(), fileId);
        return Files.readAllBytes(filePath);
    }

    @Override
    @Transactional
    public FileSaveDto deleteFile(String userId) throws Exception {
        File file = fileDao.getFile(userId);
        if (file != null) {
            file.getUser().setStored(file.getUser().getStored() - file.getSize());
            fileDao.deleteFile(file);
            deleteFileFromDisk(file.getFileId());
            return modelMapper.map(file, FileSaveDto.class).setUserStored(file.getUser().getStored());
        }
        throw new Exception("Exception! File doesn't exist!");
    }

    private void deleteFileFromDisk(String fileId) throws IOException {
        Path filePath = Path.of(storageProperties.getPath(), fileId);
        Files.delete(filePath);
    }

}
