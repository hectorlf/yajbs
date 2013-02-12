package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Page;

public interface PageDao {

	// recupera una página por id
	public Page getPage(Long id);
	
	// recupera el id de una página por el título adaptado a url
	public Long findPageId(String titleUrl);

	// recupera todas las páginas del sistema
	public List<Page> getAllPages();
	
	
	// persiste una página
	public void savePage(Page p);

}
