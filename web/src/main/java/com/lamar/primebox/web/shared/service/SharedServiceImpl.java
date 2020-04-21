package com.lamar.primebox.web.shared.service;

import com.lamar.primebox.web.auth.dao.UserDAO;
import com.lamar.primebox.web.auth.util.JwtUtil;
import com.lamar.primebox.web.file.dao.FileDAO;
import com.lamar.primebox.web.shared.dao.SharedDAO;
import com.lamar.primebox.web.shared.entity.Shared;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SharedServiceImpl implements SharedService {

    private final SharedDAO sharedDAO;
    private final UserDAO userDAO;
    private final FileDAO fileDAO;
    private final JwtUtil jwtUtil;

    public SharedServiceImpl(SharedDAO sharedDAO, UserDAO userDAO, FileDAO fileDAO, JwtUtil jwtUtil) {
        this.sharedDAO = sharedDAO;
        this.userDAO = userDAO;
        this.fileDAO = fileDAO;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public List<Shared> getAllUserSharedFiles(String userID) {
        return sharedDAO.getAllShared(userID);
    }

    @Override
    @Transactional
    public void share(Shared shared) {
        shared.setSharedFile(fileDAO.getFile(shared.getSharedFile().getFileID()));
        shared.setSharedUser(userDAO.getByUsername(shared.getSharedUser().getEmail()));
        sharedDAO.saveShared(shared);
    }

    @Override
    @Transactional
    public void unshare(String userID, String fileID) throws Exception {
        int deleted = sharedDAO.deleteShared(fileID, userID);
        if (deleted != 1) {
            throw new Exception("Shared is not deleted!");
        }
    }

    @Override
    public String getUserIDFromToken(String token) {
        return this.jwtUtil.getUserIDFromToken(token.split(" ")[1]);
    }

}
