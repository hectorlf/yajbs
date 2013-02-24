package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Page;

public interface PageDao {

	// recupera una página por id
	public Page getPage(Long id);
	
	// recupera el id de una página por el título adaptado a url
	public Long findPageId(String titleUrl);

	// cuenta el número total de páginas en el sistema
	public Long countAllPages();
	// recupera todas las páginas del sistema con paginación, sin ningún eager fecth y ordenadas por id descendente
	public List<Page> getAllPages(int firstResult, int maxResults);
	// recupera todas las páginas del sistema
	public List<Page> getAllPages();
	
	
	// persiste una página
	public void savePage(Page p);

	// modifica una página
	public void modifyPage(Page p);

	// borra una página
	public void deletePage(Long id);

}
