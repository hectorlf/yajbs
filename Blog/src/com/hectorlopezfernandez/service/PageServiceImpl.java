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
	public List<Page> getAllPages() {
		logger.debug("Recuperando todas las p�ginas del sistema");
		List<Page> pages = pageDao.getAllPages();
		return pages;
	}

}