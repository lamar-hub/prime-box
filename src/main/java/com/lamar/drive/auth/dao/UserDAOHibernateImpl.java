package com.lamar.drive.auth.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lamar.drive.auth.entity.User;

@Repository
public class UserDAOHibernateImpl implements UserDAO {

	@Autowired
	private EntityManager entityManager;

	@Override
	public User saveUser(User user) {

		Session session = entityManager.unwrap(Session.class);

		session.save(user);

		return user;
	}

	@Override
	public User getUser(String id) {

		Session session = entityManager.unwrap(Session.class);

		User user = session.get(User.class, id);

		return user;
	}

	@Override
	public User getByUsername(String username) {
		Session session = entityManager.unwrap(Session.class);

		Query<User> query = session.createQuery("FROM User u WHERE u.email=:email", User.class);
		query.setParameter("email", username);

		User user = query.getSingleResult();

		if (user == null) {
			return null;
		}

		return user;
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

		int broj = query.executeUpdate();

		return broj;
	}

	@Override
	public User existUser(String email, String password) {
		Session session = entityManager.unwrap(Session.class);

		Query<User> query = session.createQuery("FROM User u WHERE u.email=:email AND u.password=:password",
				User.class);
		query.setParameter("email", email);
		query.setParameter("password", password);

		User user = query.getSingleResult();

		if (user == null) {
			return null;
		}

		return user;
	}

}
