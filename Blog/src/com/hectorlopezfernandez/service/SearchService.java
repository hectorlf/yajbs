package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.dto.SearchResult;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.model.Post;

public interface SearchService {

	// realiza una busqueda seg�n un texto de entrada y devuelve una lista de resultados
	public List<SearchResult> search(String query);

	
	// a�ade un post al �ndice de b�squedas
	public void addPostToIndex(Post post);
	// borra un post del �ndice de b�squedas
	public void removePostFromIndex(Long id);

	// a�ade una p�gina al �ndice de b�squedas
	public void addPageToIndex(Page page);
	// borra una p�gina del �ndice de b�squedas
	public void removePageFromIndex(Long id);



	/** Metodos de mantenimiento */

	// borra el �ndice y lo vuelve a crear
	public void reindex();

}