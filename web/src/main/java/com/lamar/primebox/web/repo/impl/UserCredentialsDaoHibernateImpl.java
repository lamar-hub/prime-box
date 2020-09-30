package com.lamar.primebox.web.repo.impl;

import com.lamar.primebox.web.model.UserCredentials;
import com.lamar.primebox.web.repo.UserCredentialsDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
@Slf4j
public class UserCredentialsDaoHibernateImpl implements UserCredentialsDao {

    private final EntityManager entityManager;

    public UserCredentialsDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserCredentials getByUsername(String username) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<UserCredentials> criteriaQuery = builder.createQuery(UserCredentials.class);
        final Root<UserCredentials> root = criteriaQuery.from(UserCredentials.class);
        final Predicate predicate = builder.equal(root.get("username"), username);
        criteriaQuery.select(root).where(predicate);
        final Query<UserCredentials> query = session.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

}
