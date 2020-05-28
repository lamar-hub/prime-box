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
import com.lamar.primebox.web.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SharedServiceImpl implements SharedFileService {

    private final SharedFileDao sharedFileDao;
    private final UserDao userDao;
    private final FileDao fileDao;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public SharedServiceImpl(SharedFileDao sharedFileDao, UserDao userDao, FileDao fileDao, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.sharedFileDao = sharedFileDao;
        this.userDao = userDao;
        this.fileDao = fileDao;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public List<SharedFileDto> getAllUserSharedFiles(String username) {
        List<SharedFile> sharedFileList = sharedFileDao.getAllShared(username);
        return sharedFileList.stream().map(sharedFile -> modelMapper.map(sharedFile, SharedFileDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SharedFileDto share(SharedFileShareDto sharedFileShareDto) throws Exception {
        File file = fileDao.getFile(sharedFileShareDto.getFileId());
        User user = userDao.getByUsername(sharedFileShareDto.getSharedUserUsername());
        if (file != null && user != null) {
            SharedFile sharedFile = new SharedFile()
                    .setSharedFile(file)
                    .setSharedUser(user)
                    .setDate(sharedFileShareDto.getSharedTime())
                    .setMessage(sharedFileShareDto.getMessage());
            sharedFileDao.saveShared(sharedFile);
            return modelMapper.map(sharedFile, SharedFileDto.class);
        }
        throw new Exception("Exception! File is not shared.");
    }

    @Override
    @Transactional
    public SharedFileDto unshare(SharedFileUnshareDto sharedFileUnshareDto) throws Exception {
        SharedFile sharedFile = sharedFileDao.getShared(sharedFileUnshareDto.getFileId(), sharedFileUnshareDto.getUsername());
        if (sharedFile != null) {
            sharedFileDao.deleteShared(sharedFile);
        }
        throw new Exception("Exception! File is not shared.");
    }

}
