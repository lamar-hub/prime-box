package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.Shared;

import java.util.List;

public interface SharedDAO {

    List<Shared> getAllShared(String username);

    Shared getShared(String fileId, String username);

    Shared saveShared(Shared shared);

    void deleteShared(Shared shared);

}
