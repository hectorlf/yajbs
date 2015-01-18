package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPage;
import com.hectorlopezfernandez.model.Page;

public interface PageService {

	// recupera una pagina por id
	public Page getPage(Long id);

	// recupera el id de una pagina por el título adaptado a url
	public Long findPageId(String titleUrl);

	// calcula los valores de paginacion para todas las páginas del sistema
	public PaginationInfo computePaginationOfPages(Integer page);
	// recupera todas las paginas del sistema usando paginación, ordenadas por id descendentemente
	public List<Page> getAllPages(PaginationInfo pi);
	// recupera todas las paginas del sistema
	public List<Page> getAllPages();

	
	// guarda una nueva pagina
	public void savePage(Page p, Long ownerHostId);

	// modifica una pagina existente
	public void modifyPage(Page p, Long ownerHostId);

	// borra una pagina
	public void deletePage(Long id);


	// recupera los datos necesarios para el sitemap
	public List<SimplifiedPage> getPagesForSitemap();

}