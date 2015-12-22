package com.hectorlopezfernandez.service;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PageDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SimplifiedPage;
import com.hectorlopezfernandez.model.Page;

public class PageServiceImpl implements PageService {

	private final static Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

	private final PageDao pageDao;
	private final SearchService searchService;
	
	/* Constructores */
	
	@Inject
	public PageServiceImpl(PageDao pageDao, SearchService searchService) {
		if (pageDao == null) throw new IllegalArgumentException("El parametro pageDao no puede ser nulo.");
		if (searchService == null) throw new IllegalArgumentException("El parametro searchService no puede ser nulo.");
		this.pageDao = pageDao;
		this.searchService = searchService;
	}
	
	/* Metodos */

	@Override
	public Page getPage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la pagina a recuperar no puede ser nulo.");
		logger.debug("Recuperando pagina por id: {}", id);
		Page p = pageDao.getPage(id);
		return p;
	}

	@Override
	public Long findPageId(String titleUrl) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El titulo de la pagina a buscar no puede ser nulo.");
		logger.debug("Buscando id de pagina por titulo url: {}", titleUrl);
		Long id = pageDao.findPageId(titleUrl);
		return id;
	}

	@Override
	public PaginationInfo computePaginationOfPages(Integer page) {
		Long pageCount = pageDao.countAllPages();
		int total = pageCount == null ? 0 : pageCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quizas deberia salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Page> getAllPages(PaginationInfo pi) {
		logger.debug("Recuperando todas las paginas del sistema, con paginacion. Pagina solicitada: {}", pi.getCurrentPage());
		List<Page> pages = pageDao.getAllPages(pi.getFirstItem(), pi.getItemsPerPage());
		return pages;
	}
	@Override
	public List<Page> getAllPages() {
		logger.debug("Recuperando todas las paginas del sistema");
		List<Page> pages = pageDao.getAllPages();
		return pages;
	}

	
	@Override
	public void savePage(Page page) {
		if (page == null) throw new IllegalArgumentException("La pagina a guardar no puede ser nula.");
		if (page.getId() != null) throw new IllegalArgumentException("El id de una pagina nueva debe ser nulo, y este no lo es: " + page.getId().toString());
		logger.debug("Guardando nueva pagina con titulo: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		page.setPublicationDate(now);
		Long nId = pageDao.savePage(page);
		// se indexa la pagina creada
		Page np = pageDao.getPage(nId);
		searchService.addPageToIndex(np);
	}

	@Override
	public void modifyPage(Page page) {
		if (page == null) throw new IllegalArgumentException("La pagina a guardar no puede ser nula.");
		if (page.getId() == null) throw new IllegalArgumentException("El id de una pagina modificada no puede ser nulo.");
		logger.debug("Modificando pagina con titulo: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		pageDao.modifyPage(page);
		// se indexa la pagina modificada
		Page mp = pageDao.getPage(page.getId());
		searchService.addPageToIndex(mp);
	}

	@Override
	public void deletePage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la pagina a borrar no puede ser nulo.");
		logger.debug("Borrando pagina con id: {}", id);
		pageDao.deletePage(id);
		// se borra del indice la pagina
		searchService.removePageFromIndex(id);
	}

	
	@Override
	public List<SimplifiedPage> getPagesForSitemap() {
		List<SimplifiedPage> pages = pageDao.getPagesForSitemap();
		return pages;
	}

}