package com.lamar.box.shared.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.lamar.box.shared.entity.Shared;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class SharedDAOHibernateImpl implements SharedDAO {
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<Shared> getAllShared(String userID) {
		
		Session session = entityManager.unwrap(Session.class);
		
		@SuppressWarnings("rawtypes")
		Query query  = session.createQuery("FROM Shared s WHERE s.sharedUser.userID=:userID");
		query.setParameter("userID", userID);
		
		@SuppressWarnings("unchecked")
		List<Shared> list = query.list();
		
		return list;
	}

	@Override
	public Shared saveShared(Shared shared) {
		
		Session session = entityManager.unwrap(Session.class);
		
		session.save(shared);
		
		return shared;
	}

	@Override
	public int deleteShared(String fileID, String email) {
		
		Session session = entityManager.unwrap(Session.class);
		
		@SuppressWarnings("rawtypes")
		Query query = session.createQuery("DELETE FROM Shared s WHERE s.sharedUser.userID=:userID AND s.sharedFile.fileID=:fileID");
		query.setParameter("userID", email);
		query.setParameter("fileID", fileID);
		
		int deleted = query.executeUpdate();
		
		return deleted;
	}

}
