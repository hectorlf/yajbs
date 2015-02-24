package com.hectorlopezfernandez.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.UserDao;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.User;
import com.hectorlopezfernandez.utils.Constants;

public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserDao userDao;
	
	/* Constructores */
	
	@Inject
	public UserServiceImpl(UserDao userDao) {
		if (userDao == null) throw new IllegalArgumentException("El parametro userDao no puede ser nulo.");
		this.userDao = userDao;
	}
	
	/* Metodos */

	@Override
	public User getAdmin() {
		logger.debug("Recuperando el usuario administrador");
		User u = userDao.getUserById(Constants.ADMIN_USER_ID);
		return u;
	}

	@Override
	public User getVisitor() {
		logger.debug("Recuperando el usuario visitante anonimo");
		User u = userDao.getUserById(Constants.VISITOR_USER_ID);
		return u;
	}

	@Override
	public List<Author> getAllAuthors() {
		logger.debug("Recuperando todos los autores del sistema");
		List<Author> results = userDao.getAllAuthors();
		return results;
	}

	@Override
	public Author getAuthorById(Long id) {
		logger.debug("Recuperando el autor con id: {}", id);
		Author a = userDao.getAuthorById(id);
		return a;
	}

	@Override
	public Long findAuthorId(String username) {
		logger.debug("Buscando id de autor por nombre de usuario: {}", username);
		Long id = userDao.findAuthorId(username);
		return id;
	}

}