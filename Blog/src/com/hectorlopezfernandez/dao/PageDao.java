package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Page;

public interface PageDao {

	// recupera una p�gina por id
	public Page getPage(Long id);
	
	// recupera el id de una p�gina por el t�tulo adaptado a url
	public Long findPageId(String titleUrl);

	// recupera todas las p�ginas del sistema
	public List<Page> getAllPages();
	
	
	// persiste una p�gina
	public void savePage(Page p);

}
