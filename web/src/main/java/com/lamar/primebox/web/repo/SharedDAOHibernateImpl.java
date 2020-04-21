package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.Shared;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class SharedDAOHibernateImpl implements SharedDAO {

    private final EntityManager entityManager;

    public SharedDAOHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Shared> getAllShared(String username) {
        Session session = entityManager.unwrap(Session.class);
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("FROM Shared s WHERE s.sharedUser.email=:username");
        query.setParameter("username", username);
        @SuppressWarnings("unchecked")
        List<Shared> list = query.list();
        return list;
    }

    @Override
    public Shared getShared(String fileId, String username) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("SELECT s FROM Shared s WHERE s.sharedUser.email=:username AND s.sharedFile.fileId=:fileId");
        query.setParameter("username", username);
        query.setParameter("fileId", fileId);
        return (Shared) query.getSingleResult();
    }

    @Override
    public Shared saveShared(Shared shared) {
        Session session = entityManager.unwrap(Session.class);
        session.save(shared);
        return shared;
    }

    @Override
    public void deleteShared(Shared shared) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(shared);
    }

}
