package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.dto.SearchResult;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.model.Post;

public interface SearchService {

	// realiza una busqueda según un texto de entrada y devuelve una lista de resultados
	public List<SearchResult> search(String query);

	
	// añade un post al índice de búsquedas
	public void addPostToIndex(Post post);
	// borra un post del índice de búsquedas
	public void removePostFromIndex(Long id);

	// añade una página al índice de búsquedas
	public void addPageToIndex(Page page);
	// borra una página del índice de búsquedas
	public void removePageFromIndex(Long id);



	/** Metodos de mantenimiento */

	// borra el índice y lo vuelve a crear
	public void reindex();

}