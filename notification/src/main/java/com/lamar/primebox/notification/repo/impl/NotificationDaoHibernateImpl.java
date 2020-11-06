package com.lamar.primebox.notification.repo.impl;

import com.lamar.primebox.notification.model.Notification;
import com.lamar.primebox.notification.model.NotificationState;
import com.lamar.primebox.notification.repo.NotificationDao;
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
public class NotificationDaoHibernateImpl implements NotificationDao {

    private final EntityManager entityManager;

    public NotificationDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveNotification(Notification notification) {
        final Session session = entityManager.unwrap(Session.class);

        session.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByState(NotificationState notificationState) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<Notification> criteriaQuery = builder.createQuery(Notification.class);
        final Root<Notification> root = criteriaQuery.from(Notification.class);

        criteriaQuery.select(root).where(builder.equal(root.get("notificationState"), notificationState));

        final Query<Notification> query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public Notification getNotificationByTransactionId(String transactionId) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<Notification> criteriaQuery = builder.createQuery(Notification.class);
        final Root<Notification> root = criteriaQuery.from(Notification.class);
        final Predicate predicateTransactionId = builder.equal(root.get("notificationTransactionId"), transactionId);
        criteriaQuery
                .select(root)
                .where(predicateTransactionId);

        final Query<Notification> query = session.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    @Override
    public void updateNotification(Notification notification) {
        final Session session = entityManager.unwrap(Session.class);

        session.update(notification);
    }

}
