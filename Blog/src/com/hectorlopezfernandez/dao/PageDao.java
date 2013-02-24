package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Page;

public interface PageDao {

	// recupera una p�gina por id
	public Page getPage(Long id);
	
	// recupera el id de una p�gina por el t�tulo adaptado a url
	public Long findPageId(String titleUrl);

	// cuenta el n�mero total de p�ginas en el sistema
	public Long countAllPages();
	// recupera todas las p�ginas del sistema con paginaci�n, sin ning�n eager fecth y ordenadas por id descendente
	public List<Page> getAllPages(int firstResult, int maxResults);
	// recupera todas las p�ginas del sistema
	public List<Page> getAllPages();
	
	
	// persiste una p�gina
	public void savePage(Page p);

	// modifica una p�gina
	public void modifyPage(Page p);

	// borra una p�gina
	public void deletePage(Long id);

}
