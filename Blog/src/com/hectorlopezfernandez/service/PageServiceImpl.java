package com.hectorlopezfernandez.service;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.BlogDao;
import com.hectorlopezfernandez.dao.PageDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Page;

public class PageServiceImpl implements PageService {

	private final static Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

	private final PageDao pageDao;
	private final BlogDao blogDao;
	private final SearchService searchService;
	
	/* Constructores */
	
	@Inject
	public PageServiceImpl(PageDao pageDao, BlogDao blogDao, SearchService searchService) {
		if (pageDao == null) throw new IllegalArgumentException("El parametro pageDao no puede ser nulo.");
		if (blogDao == null) throw new IllegalArgumentException("El parametro blogDao no puede ser nulo.");
		if (searchService == null) throw new IllegalArgumentException("El parametro searchService no puede ser nulo.");
		this.pageDao = pageDao;
		this.blogDao = blogDao;
		this.searchService = searchService;
	}
	
	/* Metodos */

	@Override
	public Page getPage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la página a recuperar no puede ser nulo.");
		logger.debug("Recuperando página por id: {}", id);
		Page p = pageDao.getPage(id);
		return p;
	}

	@Override
	public Long findPageId(String titleUrl) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El título de la página a buscar no puede ser nulo.");
		logger.debug("Buscando id de página por título url: {}", titleUrl);
		Long id = pageDao.findPageId(titleUrl);
		return id;
	}

	@Override
	public PaginationInfo computePaginationOfPages(Integer page) {
		Long pageCount = pageDao.countAllPages();
		int total = pageCount == null ? 0 : pageCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quizás debería salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Page> getAllPages(PaginationInfo pi) {
		logger.debug("Recuperando todas las páginas del sistema, con paginación. Página solicitada: {}", pi.getCurrentPage());
		List<Page> pages = pageDao.getAllPages(pi.getFirstItem(), pi.getItemsPerPage());
		return pages;
	}
	@Override
	public List<Page> getAllPages() {
		logger.debug("Recuperando todas las páginas del sistema");
		List<Page> pages = pageDao.getAllPages();
		return pages;
	}

	
	@Override
	public void savePage(Page page, Long ownerHostId) {
		if (page == null) throw new IllegalArgumentException("La página a guardar no puede ser nula.");
		if (page.getId() != null) throw new IllegalArgumentException("El id de una página nueva debe ser nulo, y este no lo es: " + page.getId().toString());
		if (ownerHostId == null) throw new IllegalArgumentException("El id del Host asociado a la página no puede ser nulo.");
		logger.debug("Guardando nueva página con título: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		page.setPublicationDate(now);
		Host ownerHost = blogDao.getHost(ownerHostId);
		page.setHost(ownerHost);
		Long nId = pageDao.savePage(page);
		// se indexa la pagina creada
		Page np = pageDao.getPage(nId);
		searchService.addPageToIndex(np);
	}

	@Override
	public void modifyPage(Page page, Long ownerHostId) {
		if (page == null) throw new IllegalArgumentException("La página a guardar no puede ser nula.");
		if (page.getId() == null) throw new IllegalArgumentException("El id de una página modificada no puede ser nulo.");
		if (ownerHostId == null) throw new IllegalArgumentException("El id del Host asociado a la página no puede ser nulo.");
		logger.debug("Modificando página con título: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		Host ownerHost = blogDao.getHost(ownerHostId);
		page.setHost(ownerHost);
		pageDao.modifyPage(page);
		// se indexa la pagina modificada
		Page mp = pageDao.getPage(page.getId());
		searchService.addPageToIndex(mp);
	}

	@Override
	public void deletePage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la página a borrar no puede ser nulo.");
		logger.debug("Borrando página con id: {}", id);
		pageDao.deletePage(id);
		// se borra del índice la pagina
		searchService.removePageFromIndex(id);
	}

}