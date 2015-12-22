package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.dto.SimplifiedPage;
import com.hectorlopezfernandez.model.Page;

public interface PageDao {

	// recupera una pagina por id
	public Page getPage(Long id);
	
	// recupera el id de una pagina por el titulo adaptado a url
	public Long findPageId(String titleUrl);

	// cuenta el numero total de paginas en el sistema
	public Long countAllPages();
	// recupera todas las paginas del sistema con paginacion, sin ningun eager fecth y ordenadas por id descendente
	public List<Page> getAllPages(int firstResult, int maxResults);
	// recupera todas las paginas del sistema
	public List<Page> getAllPages();
	
	
	// persiste una pagina y devuelve el id generado
	public Long savePage(Page p);

	// modifica una pagina
	public void modifyPage(Page p);

	// borra una pagina
	public void deletePage(Long id);


	// recupera todos los datos necesarios para el sitemap
	List<SimplifiedPage> getPagesForSitemap();

}
