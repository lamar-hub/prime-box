package com.lamar.primebox.web.repo.impl;

import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.model.VerificationCode;
import com.lamar.primebox.web.repo.VerificationCodeDao;
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
public class VerificationCodeDaoHibernateImpl implements VerificationCodeDao {

    private final EntityManager entityManager;

    public VerificationCodeDaoHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<VerificationCode> getVerificationCodeByEmail(String email) {
        final Session session = entityManager.unwrap(Session.class);
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<VerificationCode> criteriaQuery = builder.createQuery(VerificationCode.class);
        final Root<VerificationCode> root = criteriaQuery.from(VerificationCode.class);
        final Predicate predicate = builder.equal(root.get("user").get("email"), email);
        criteriaQuery.select(root).where(predicate);
        final Query<VerificationCode> query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public void saveVerificationCode(VerificationCode verificationCode) {
        final Session session = entityManager.unwrap(Session.class);

        session.save(verificationCode);
    }
}
