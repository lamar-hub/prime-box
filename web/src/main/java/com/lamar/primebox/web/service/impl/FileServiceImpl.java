package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.FileDownloadDto;
import com.lamar.primebox.web.dto.model.FileDto;
import com.lamar.primebox.web.dto.model.FileSaveDeleteDto;
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
        final List<File> files = fileDao.getAllUserFiles(username);
        return files.stream().map(file -> modelMapper.map(file, FileDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FileSaveDeleteDto saveFileDatabase(MultipartFile multipartFile, String username) throws Exception {
        final User user = userDao.getUser(username);

        if (!hasSpace(user, multipartFile.getSize())) {
            throw new Exception("There are no space anymore!");
        }

        final File file = new File()
                .setFilename(multipartFile.getOriginalFilename())
                .setType(multipartFile.getContentType())
                .setSize((int) multipartFile.getSize())
                .setLastModified(new Date().getTime())
                .setUser(user);

        fileDao.saveFile(file);

        saveFileDisk(multipartFile, file);

        user.setStored(user.getStored() + file.getSize());
        return modelMapper.map(file, FileSaveDeleteDto.class).setUserStored(user.getStored());
    }

    private boolean hasSpace(User user, Long size) {
        if (user.getStored() + size > user.getLimit()) {
            return false;
        }
        return true;
    }

    private void saveFileDisk(MultipartFile multipartFile, File file) throws IOException {
        final Path filePath = Path.of(storageProperties.getPath(), file.getFileId());
        Files.copy(multipartFile.getInputStream(), filePath);
    }

    @Override
    @Transactional
    public FileDto updateFile(FileUpdateDto fileUpdateDto) throws Exception {
        final File file = fileDao.getFile(fileUpdateDto.getFileId());
        if (file != null) {
            file.setFilename(fileUpdateDto.getFilename());
            return modelMapper.map(file, FileDto.class);
        }
        throw new Exception("Exception! File doesn't exist!");
    }

    @Override
    @Transactional
    public FileDownloadDto getFile(String fileID) throws Exception {
        final File file = fileDao.getFile(fileID);
        if (file != null) {
            final byte[] fileFromDisk = getFileFromDisk(file.getFileId());
            return modelMapper.map(file, FileDownloadDto.class).setFile(fileFromDisk);
        }
        throw new Exception("Exception! File doesn't exist!");
    }

    private byte[] getFileFromDisk(String fileId) throws IOException {
        final Path filePath = Path.of(storageProperties.getPath(), fileId);
        return Files.readAllBytes(filePath);
    }

    @Override
    @Transactional
    public FileSaveDeleteDto deleteFile(String userId) throws Exception {
        final File file = fileDao.getFile(userId);
        if (file != null) {
            fileDao.deleteFile(file);
            file.getUser().setStored(file.getUser().getStored() - file.getSize());

            deleteFileFromDisk(file.getFileId());
            return modelMapper.map(file, FileSaveDeleteDto.class).setUserStored(file.getUser().getStored());
        }
        throw new Exception("Exception! File doesn't exist!");
    }

    private void deleteFileFromDisk(String fileId) throws IOException {
        final Path filePath = Path.of(storageProperties.getPath(), fileId);
        Files.delete(filePath);
    }

}
