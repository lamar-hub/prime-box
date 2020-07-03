package com.lamar.primebox.web.repo.impl;

import com.lamar.primebox.web.model.File;
import com.lamar.primebox.web.repo.FileDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Slf4j
public class FileDaoHibernateImpl implements FileDao {

    private final EntityManager entityManager;

    public FileDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<File> getAllUserFiles(String username) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<File> criteriaQuery = builder.createQuery(File.class);
        final Root<File> root = criteriaQuery.from(File.class);
        final Predicate predicateEmail = builder.equal(root.get("user").get("email"), username);
        criteriaQuery
                .select(root)
                .where(predicateEmail);
        final Query<File> query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public void saveFile(File file) {
        final Session session = entityManager.unwrap(Session.class);

        session.save(file);
    }

    @Override
    public File getFile(String fileId) {
        final Session session = entityManager.unwrap(Session.class);

        return session.get(File.class, fileId);
    }

    @Override
    public void deleteFile(File file) {
        final Session session = entityManager.unwrap(Session.class);

        session.delete(file);
    }

}
