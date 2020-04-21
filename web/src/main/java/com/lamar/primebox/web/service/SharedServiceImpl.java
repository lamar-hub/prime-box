package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.SharedDto;
import com.lamar.primebox.web.dto.model.SharedShareDto;
import com.lamar.primebox.web.dto.model.SharedUnshareDto;
import com.lamar.primebox.web.model.File;
import com.lamar.primebox.web.model.Shared;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.FileDAO;
import com.lamar.primebox.web.repo.SharedDAO;
import com.lamar.primebox.web.repo.UserDAO;
import com.lamar.primebox.web.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SharedServiceImpl implements SharedService {

    private final SharedDAO sharedDAO;
    private final UserDAO userDAO;
    private final FileDAO fileDAO;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public SharedServiceImpl(SharedDAO sharedDAO, UserDAO userDAO, FileDAO fileDAO, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.sharedDAO = sharedDAO;
        this.userDAO = userDAO;
        this.fileDAO = fileDAO;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public List<SharedDto> getAllUserSharedFiles(String username) {
        List<Shared> sharedList = sharedDAO.getAllShared(username);
        return sharedList.stream().map(shared -> modelMapper.map(shared, SharedDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SharedDto share(SharedShareDto sharedShareDto) throws Exception {
        File file = fileDAO.getFile(sharedShareDto.getFileId());
        User user = userDAO.getByUsername(sharedShareDto.getSharedUserUsername());
        if (file != null && user != null) {
            Shared shared = new Shared()
                    .setSharedFile(file)
                    .setSharedUser(user)
                    .setDate(sharedShareDto.getSharedTime())
                    .setMessage(sharedShareDto.getMessage());
            sharedDAO.saveShared(shared);
            return modelMapper.map(shared, SharedDto.class);
        }
        throw new Exception("Exception! File is not shared.");
    }

    @Override
    @Transactional
    public SharedDto unshare(SharedUnshareDto sharedUnshareDto) throws Exception {
        Shared shared = sharedDAO.getShared(sharedUnshareDto.getFileId(), sharedUnshareDto.getUsername());
        if (shared != null) {
            sharedDAO.deleteShared(shared);
        }
        throw new Exception("Exception! File is not shared.");
    }

}
