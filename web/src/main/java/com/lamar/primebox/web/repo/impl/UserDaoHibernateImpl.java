package com.lamar.primebox.web.repo.impl;

import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.repo.UserDao;
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
public class UserDaoHibernateImpl implements UserDao {

    private final EntityManager entityManager;

    public UserDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveUser(User user) {
        final Session session = entityManager.unwrap(Session.class);

        session.save(user);
    }

    @Override
    public User getUser(String id) {
        final Session session = entityManager.unwrap(Session.class);
        
        return session.get(User.class, id);
    }

    @Override
    public User getByUsername(String username) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        final Root<User> root = criteriaQuery.from(User.class);
        final Predicate predicate = builder.equal(root.get("email"), username);
        criteriaQuery.select(root).where(predicate);
        final Query<User> query = session.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

}
