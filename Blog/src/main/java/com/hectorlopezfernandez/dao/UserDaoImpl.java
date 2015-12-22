package com.hectorlopezfernandez.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.exception.DataIntegrityException;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.User;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	private final static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	/* Constructores */
	
	@Inject
	public UserDaoImpl(EntityManager em) {
		super(em);
	}
	
	/* Metodos */
	
	// recupera un usuario a partir del id
	@Override
	public User getUserById(Long id) {
		logger.debug("Recuperando usuario con id {}", id);
		return get(id, User.class);
	}

	// recupera todos los objetos Author
	@Override
	public List<Author> getAllAuthors() {
		logger.debug("Recuperando todos los objetos Author de la base de datos");
		List<Author> results = find("select a from Author a", null, Author.class);
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}

	// recupera un objeto Author a partir del id
	@Override
	public Author getAuthorById(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Recuperando autor con id {}", id);
		return get(id, Author.class);
	}

	// recupera el id de un autor a partir del nombre de usuario
	@Override
	public Long findAuthorId(String username) {
		if (username == null || username.length() == 0) throw new IllegalArgumentException("El parametro username no puede ser nulo ni vacio.");
		logger.debug("Recuperando autor con nombre de usuario: {}", username);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("username", username);
		List<Long> ids = listIds("select a.id from Author a where a.username = :username", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varios autores para el nombre especificado. La columna de base de datos deberia tener una restriccion de unicidad que no lo habria permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		return id;
	}

}