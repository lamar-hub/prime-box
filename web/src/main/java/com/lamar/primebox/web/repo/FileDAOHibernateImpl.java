package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.File;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class FileDAOHibernateImpl implements FileDAO {

    private final EntityManager entityManager;

    public FileDAOHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<File> getAllUserFiles(String username) {
        Session session = entityManager.unwrap(Session.class);
        Query<File> query = session.createQuery("SELECT f FROM File f WHERE f.user.email=:email", File.class);
        query.setParameter("email", username);
        return query.getResultList();
    }

    @Override
    public void saveFile(File file) {
        Session session = entityManager.unwrap(Session.class);
        session.save(file);
    }

    @Override
    public File getFile(String fileID) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(File.class, fileID);
    }

    @Override
    public File updateFile(File file) {
        Session session = entityManager.unwrap(Session.class);
        session.update(file);
        return file;
    }

    @Override
    public void deleteFile(File file) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(file);
    }

}
