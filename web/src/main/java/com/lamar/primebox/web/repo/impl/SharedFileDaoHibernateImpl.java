package com.lamar.primebox.web.repo.impl;

import com.lamar.primebox.web.model.SharedFile;
import com.lamar.primebox.web.repo.SharedFileDao;
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
public class SharedFileDaoHibernateImpl implements SharedFileDao {

    private final EntityManager entityManager;

    public SharedFileDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<SharedFile> getAllSharedFiles(String username) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<SharedFile> criteriaQuery = builder.createQuery(SharedFile.class);
        final Root<SharedFile> root = criteriaQuery.from(SharedFile.class);
        final Predicate predicateEmail = builder.equal(root.get("sharedUser").get("email"), username);
        criteriaQuery
                .select(root)
                .where(predicateEmail);
        final Query<SharedFile> query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public SharedFile getSharedFile(String fileId, String username) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<SharedFile> criteriaQuery = builder.createQuery(SharedFile.class);
        final Root<SharedFile> root = criteriaQuery.from(SharedFile.class);
        final Predicate predicateFileId = builder.equal(root.get("sharedFile").get("fileId"), fileId);
        final Predicate predicateEmail = builder.equal(root.get("sharedUser").get("email"), username);
        criteriaQuery
                .select(root)
                .where(predicateFileId)
                .where(predicateEmail);
        final Query<SharedFile> query = session.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    @Override
    public void saveSharedFile(SharedFile sharedFile) {
        final Session session = entityManager.unwrap(Session.class);

        session.save(sharedFile);
    }

    @Override
    public void deleteSharedFile(SharedFile sharedFile) {
        final Session session = entityManager.unwrap(Session.class);

        session.delete(sharedFile);
    }

}
