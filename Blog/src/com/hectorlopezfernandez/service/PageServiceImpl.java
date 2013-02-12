package com.hectorlopezfernandez.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.PageDao;
import com.hectorlopezfernandez.model.Page;

public class PageServiceImpl implements PageService {

	private final static Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

	private final PageDao pageDao;
	
	/* Constructores */
	
	@Inject
	public PageServiceImpl(PageDao pageDao) {
		if (pageDao == null) throw new IllegalArgumentException("El parametro pageDao no puede ser nulo.");
		this.pageDao = pageDao;
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
	public List<Page> getAllPages() {
		logger.debug("Recuperando todas las páginas del sistema");
		List<Page> pages = pageDao.getAllPages();
		return pages;
	}

}