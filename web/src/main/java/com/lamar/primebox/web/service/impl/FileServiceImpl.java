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
    public FileSaveDeleteDto saveFile(MultipartFile multipartFile, String username) throws Exception {
        final User user = userDao.getByUsername(username);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        final long storedBeforeSave = user.getStored();
        final long userLimit = user.getLimit();
        final long fileSize = multipartFile.getSize();

        if (!hasSpace(storedBeforeSave, userLimit, fileSize)) {
            log.error("user have no space anymore");
            throw new Exception("user have no space anymore");
        }

        final File file = new File()
                .setFilename(multipartFile.getOriginalFilename())
                .setType(multipartFile.getContentType())
                .setSize((int) multipartFile.getSize())
                .setLastModified(new Date().getTime())
                .setUser(user);
        fileDao.saveFile(file);

        final long storedAfterSave = storedBeforeSave + file.getSize();
        user.setStored(storedAfterSave);

        final boolean sendNotification = sendNotification(storedBeforeSave, storedAfterSave, userLimit);

        return modelMapper.map(file, FileSaveDeleteDto.class)
                .setUserStored(user.getStored())
                .setSendNotification(sendNotification);
    }

    private boolean sendNotification(long storedBeforeSave, long storedAfterSave, long userLimit) {
        final boolean under = storedBeforeSave < userLimit / 2;
        final boolean over = storedAfterSave > userLimit / 2;

        return under && over;
    }

    private boolean hasSpace(long storedBeforeSave, long userLimit, long fileSize) {
        return storedBeforeSave + fileSize <= userLimit;
    }

    @Override
    @Transactional
    public FileDto updateFile(FileUpdateDto fileUpdateDto) throws Exception {
        final File file = fileDao.getFile(fileUpdateDto.getFileId());

        if (file == null) {
            log.error("file not found");
            throw new Exception("file not found");
        }

        file.setFilename(fileUpdateDto.getFilename());
        return modelMapper.map(file, FileDto.class);
    }

    @Override
    @Transactional
    public FileDownloadDto getFile(String fileID) throws Exception {
        final File file = fileDao.getFile(fileID);

        if (file == null) {
            log.error("file not found");
            throw new Exception("file not found");
        }

        final byte[] fileFromDisk = getFileFromDisk(file.getFileId());
        return modelMapper.map(file, FileDownloadDto.class).setFile(fileFromDisk);
    }

    private byte[] getFileFromDisk(String fileId) throws IOException {
        final Path filePath = Path.of(storageProperties.getPath(), fileId);

        return Files.readAllBytes(filePath);
    }

    @Override
    @Transactional
    public FileSaveDeleteDto deleteFile(String userId) throws Exception {
        final File file = fileDao.getFile(userId);

        if (file == null) {
            log.error("file not found");
            throw new Exception("file not found");
        }

        fileDao.deleteFile(file);
        file.getUser().setStored(file.getUser().getStored() - file.getSize());
        return modelMapper.map(file, FileSaveDeleteDto.class).setUserStored(file.getUser().getStored());
    }

}
