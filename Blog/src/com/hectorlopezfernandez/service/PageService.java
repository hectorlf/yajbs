package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.model.Page;

public interface PageService {

	// recupera una pagina por id
	public Page getPage(Long id);

	// recupera el id de una pagina por el t�tulo adaptado a url
	public Long findPageId(String titleUrl);

	// recupera todas las paginas del sistema
	public List<Page> getAllPages();

	
	// guarda una nueva pagina
	public void savePage(Page p, Long ownerHostId);

	// modifica una pagina existente
	public void modifyPage(Page p, Long ownerHostId);

}