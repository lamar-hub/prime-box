package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.SharedFileDto;
import com.lamar.primebox.web.dto.model.SharedFileShareDto;
import com.lamar.primebox.web.dto.model.SharedFileUnshareDto;
import com.lamar.primebox.web.model.File;
import com.lamar.primebox.web.model.SharedFile;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.FileDao;
import com.lamar.primebox.web.repo.SharedFileDao;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.service.SharedFileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SharedServiceImpl implements SharedFileService {

    private final SharedFileDao sharedFileDao;
    private final UserDao userDao;
    private final FileDao fileDao;
    private final ModelMapper modelMapper;

    public SharedServiceImpl(SharedFileDao sharedFileDao, UserDao userDao, FileDao fileDao, ModelMapper modelMapper) {
        this.sharedFileDao = sharedFileDao;
        this.userDao = userDao;
        this.fileDao = fileDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public List<SharedFileDto> getAllUserSharedFiles(String username) {
        final List<SharedFile> sharedFileList = sharedFileDao.getAllSharedFiles(username);

        return sharedFileList.stream().map(sharedFile -> modelMapper.map(sharedFile, SharedFileDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SharedFileDto share(SharedFileShareDto sharedFileShareDto) throws Exception {
        final File file = fileDao.getFile(sharedFileShareDto.getFileId());
        final User user = userDao.getByUsername(sharedFileShareDto.getSharedUserUsername());

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        if (!user.isEnabled()) {
            log.error("user not active");

            throw new Exception("user not active");
        }

        if (file == null) {
            log.error("file not found");
            throw new Exception("file not found");
        }

        final SharedFile sharedFile = new SharedFile()
                .setSharedFile(file)
                .setSharedUser(user)
                .setDate(new Date().getTime())
                .setMessage(sharedFileShareDto.getMessage());

        sharedFileDao.saveSharedFile(sharedFile);
        return modelMapper.map(sharedFile, SharedFileDto.class);
    }

    @Override
    @Transactional
    public SharedFileDto unshare(SharedFileUnshareDto sharedFileUnshareDto) throws Exception {
        final SharedFile sharedFile = sharedFileDao.getSharedFile(sharedFileUnshareDto.getFileId(), sharedFileUnshareDto.getUsername());

        if (sharedFile == null) {
            log.error("file not found");
            throw new Exception("file not found");
        }

        sharedFileDao.deleteSharedFile(sharedFile);
        return modelMapper.map(sharedFile, SharedFileDto.class);
    }

}
