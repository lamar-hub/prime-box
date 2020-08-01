package com.lamar.primebox.email.repo.impl;

import com.lamar.primebox.email.model.Notification;
import com.lamar.primebox.email.repo.NotificationDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Slf4j
public class NotificationDaoHibernateImpl implements NotificationDao {

    private final EntityManager entityManager;

    public NotificationDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveNotification(Notification notification) {
        final Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate(notification);
    }

    @Override
    public List<Notification> getNotificationChunk(Integer chunk) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<Notification> criteriaQuery = builder.createQuery(Notification.class);
        final Root<Notification> root = criteriaQuery.from(Notification.class);

        criteriaQuery.select(root);

        final Query<Notification> query = session.createQuery(criteriaQuery);
        query.setFirstResult(0);
        query.setMaxResults(chunk);

        return query.getResultList();
    }

    @Override
    public void deleteNotification(Notification notification) {
        final Session session = entityManager.unwrap(Session.class);

        session.delete(notification);
    }

    @Override
    public void updateNotification(Notification notification) {
        final Session session = entityManager.unwrap(Session.class);

        session.update(notification);
    }

}
