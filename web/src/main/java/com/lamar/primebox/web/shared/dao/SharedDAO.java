package com.lamar.primebox.web.shared.dao;

import com.lamar.primebox.web.shared.entity.Shared;

import java.util.List;

public interface SharedDAO {

    List<Shared> getAllShared(String userID);

    Shared saveShared(Shared shared);

    int deleteShared(String fileID, String email);

}
