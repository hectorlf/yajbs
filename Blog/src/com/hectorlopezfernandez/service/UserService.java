package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.User;

public interface UserService {

	// recupera el usuario especial administrador
	public User getAdmin();

	// recupera el usuario especial visitante anonimo
	public User getVisitor();

	
	/** AUTHORS */
	
	// recupera una lista con todos los usuarios registrados como autores
	public List<Author> getAllAuthors();

	// recupera el detalle de un autor
	public Author getAuthorById(Long id);

	// busca un autor por el nombre de usuario
	public Long findAuthorId(String username);

}