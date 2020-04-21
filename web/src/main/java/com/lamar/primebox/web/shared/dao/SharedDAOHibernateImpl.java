package com.lamar.primebox.web.shared.dao;

import com.lamar.primebox.web.shared.entity.Shared;
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
    public List<Shared> getAllShared(String userID) {
        Session session = entityManager.unwrap(Session.class);
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("FROM Shared s WHERE s.sharedUser.userID=:userID");
        query.setParameter("userID", userID);
        @SuppressWarnings("unchecked")
        List<Shared> list = query.list();
        return list;
    }

    @Override
    public Shared saveShared(Shared shared) {
        Session session = entityManager.unwrap(Session.class);
        session.save(shared);
        return shared;
    }

    @Override
    public int deleteShared(String fileID, String email) {
        Session session = entityManager.unwrap(Session.class);
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("DELETE FROM Shared s WHERE s.sharedUser.userID=:userID AND s.sharedFile.fileID=:fileID");
        query.setParameter("userID", email);
        query.setParameter("fileID", fileID);
        return query.executeUpdate();
    }

}
