package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SearchResult;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.model.Post;

public interface SearchService {

	// calcula los valores de paginacion para una busqueda
	public PaginationInfo computePagination(Integer page);
	// realiza una busqueda segun un texto de entrada y devuelve una lista de resultados
	public List<SearchResult> search(String query);

	
	// autocompletado
	public List<SearchResult> autocomplete(String query);

	
	// aniade un post al indice de busquedas
	public void addPostToIndex(Post post);
	// borra un post del indice de busquedas
	public void removePostFromIndex(Long id);

	// aniade una pagina al indice de busquedas
	public void addPageToIndex(Page page);
	// borra una pagina del indice de busquedas
	public void removePageFromIndex(Long id);



	/** Metodos de mantenimiento */

	// borra el indice y lo vuelve a crear
	public void reindex();

}