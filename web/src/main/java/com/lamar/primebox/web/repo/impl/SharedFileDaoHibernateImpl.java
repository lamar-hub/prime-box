package com.lamar.primebox.web.repo.impl;

import com.lamar.primebox.web.model.SharedFile;
import com.lamar.primebox.web.repo.SharedFileDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Slf4j
public class SharedFileDaoHibernateImpl implements SharedFileDao {

    private final EntityManager entityManager;

    public SharedFileDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<SharedFile> getAllShared(String username) {
        Session session = entityManager.unwrap(Session.class);
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("FROM SharedFile s WHERE s.sharedUser.email=:username");
        query.setParameter("username", username);
        @SuppressWarnings("unchecked")
        List<SharedFile> list = query.list();
        return list;
    }

    @Override
    public SharedFile getShared(String fileId, String username) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("SELECT s FROM SharedFile s WHERE s.sharedUser.email=:username AND s.sharedFile.fileId=:fileId");
        query.setParameter("username", username);
        query.setParameter("fileId", fileId);
        return (SharedFile) query.getSingleResult();
    }

    @Override
    public SharedFile saveShared(SharedFile sharedFile) {
        Session session = entityManager.unwrap(Session.class);
        session.save(sharedFile);
        return sharedFile;
    }

    @Override
    public void deleteShared(SharedFile sharedFile) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(sharedFile);
    }

}
