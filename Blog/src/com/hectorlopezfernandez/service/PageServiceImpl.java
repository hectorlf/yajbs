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
	
	/* Constructores */
	
	@Inject
	public PageServiceImpl(PageDao pageDao, BlogDao blogDao) {
		if (pageDao == null) throw new IllegalArgumentException("El parametro pageDao no puede ser nulo.");
		if (blogDao == null) throw new IllegalArgumentException("El parametro blogDao no puede ser nulo.");
		this.pageDao = pageDao;
		this.blogDao = blogDao;
	}
	
	/* Metodos */

	@Override
	public Page getPage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la p�gina a recuperar no puede ser nulo.");
		logger.debug("Recuperando p�gina por id: {}", id);
		Page p = pageDao.getPage(id);
		return p;
	}

	@Override
	public Long findPageId(String titleUrl) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El t�tulo de la p�gina a buscar no puede ser nulo.");
		logger.debug("Buscando id de p�gina por t�tulo url: {}", titleUrl);
		Long id = pageDao.findPageId(titleUrl);
		return id;
	}

	@Override
	public PaginationInfo computePaginationOfPages(Integer page) {
		Long pageCount = pageDao.countAllPages();
		int total = pageCount == null ? 0 : pageCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quiz�s deber�a salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Page> getAllPages(PaginationInfo pi) {
		logger.debug("Recuperando todas las p�ginas del sistema, con paginaci�n. P�gina solicitada: {}", pi.getCurrentPage());
		List<Page> pages = pageDao.getAllPages(pi.getFirstItem(), pi.getItemsPerPage());
		return pages;
	}
	@Override
	public List<Page> getAllPages() {
		logger.debug("Recuperando todas las p�ginas del sistema");
		List<Page> pages = pageDao.getAllPages();
		return pages;
	}

	
	@Override
	public void savePage(Page page, Long ownerHostId) {
		if (page == null) throw new IllegalArgumentException("La p�gina a guardar no puede ser nula.");
		if (page.getId() != null) throw new IllegalArgumentException("El id de una p�gina nueva debe ser nulo, y este no lo es: " + page.getId().toString());
		if (ownerHostId == null) throw new IllegalArgumentException("El id del Host asociado a la p�gina no puede ser nulo.");
		logger.debug("Guardando nueva p�gina con t�tulo: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		page.setPublicationDate(now);
		Host ownerHost = blogDao.getHost(ownerHostId);
		page.setHost(ownerHost);
		pageDao.savePage(page);
	}

	@Override
	public void modifyPage(Page page, Long ownerHostId) {
		if (page == null) throw new IllegalArgumentException("La p�gina a guardar no puede ser nula.");
		if (page.getId() == null) throw new IllegalArgumentException("El id de una p�gina modificada no puede ser nulo.");
		if (ownerHostId == null) throw new IllegalArgumentException("El id del Host asociado a la p�gina no puede ser nulo.");
		logger.debug("Modificando p�gina con t�tulo: {}", page.getTitle());
		DateTime now = new DateTime();
		page.setLastModificationDate(now);
		Host ownerHost = blogDao.getHost(ownerHostId);
		page.setHost(ownerHost);
		pageDao.modifyPage(page);
	}

}