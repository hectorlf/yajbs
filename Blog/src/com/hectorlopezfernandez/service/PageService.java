package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.model.Page;

public interface PageService {

	// recupera una pagina por id
	public Page getPage(Long id);

	// recupera el id de una pagina por el título adaptado a url
	public Long findPageId(String titleUrl);

	// recupera todas las paginas del sistema
	public List<Page> getAllPages();

}