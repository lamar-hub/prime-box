package com.lamar.drive.file.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lamar.drive.file.entity.File;

@Repository
public class FileDAOHibernateImpl implements FileDAO {
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<File> getAllUserFiles(String userID) {
		
		Session session = entityManager.unwrap(Session.class);
		
		Query<File> query = session.createQuery("FROM File f WHERE f.user.userID=:userID", File.class);
		query.setParameter("userID", userID);
		
		List<File> files = query.getResultList();
		
		return files;
	}
	
	@Override
	public File saveFile(File file) {
		
		Session session = entityManager.unwrap(Session.class);
		
		session.save(file);
		
		return file;
	}

	@Override
	public File getFile(String fileID) {
		
		Session session = entityManager.unwrap(Session.class);
		
		File file = session.get(File.class, fileID);
		
		return file;
	}

	@Override
	public File updateFile(File file) {
		
		Session session = entityManager.unwrap(Session.class);
		
		session.update(file);
		
		return file;
	}

	@Override
	public void deleteFile(File file) {
		
		Session session = entityManager.unwrap(Session.class);
		
		session.delete(file);
	}

}
