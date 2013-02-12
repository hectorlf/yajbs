package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.User;

public interface UserDao {

	// recupera un usuario a partir del id
	public User getUserById(Long id);

	// recupera una lista con todos los objetos Author del sistema
	public List<Author> getAllAuthors();

	// recupera un autor a partir del id
	public Author getAuthorById(Long id);

	// recupera el id de un autor a partir del nombre de usuario
	public Long findAuthorId(String username);

}