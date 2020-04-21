package com.lamar.primebox.web.auth.dao;

import com.lamar.primebox.web.auth.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserDAOHibernateImpl implements UserDAO {

    private final EntityManager entityManager;

    public UserDAOHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User saveUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.save(user);
        return user;
    }

    @Override
    public User getUser(String id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(User.class, id);
    }

    @Override
    public User getByUsername(String username) {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.email=:email", User.class);
        query.setParameter("email", username);
        return query.getSingleResult();
    }

    @Override
    public User updateUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.update(user);
        return user;
    }

    @Override
    public int deleteUser(String userID) {
        Session session = entityManager.unwrap(Session.class);
        @SuppressWarnings("rawtypes")
        Query query = session.createQuery("DELETE FROM User u WHERE u.userID=:userID");
        query.setParameter("userID", userID);
        return query.executeUpdate();
    }

    @Override
    public User existUser(String email, String password) {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.email=:email AND u.password=:password", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.getSingleResult();
    }

}
