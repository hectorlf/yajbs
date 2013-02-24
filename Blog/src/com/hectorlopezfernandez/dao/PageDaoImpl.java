package com.hectorlopezfernandez.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.exception.DataIntegrityException;
import com.hectorlopezfernandez.model.Page;

public class PageDaoImpl extends BaseDaoImpl implements PageDao {

	private final static Logger logger = LoggerFactory.getLogger(PageDaoImpl.class);

	/* Constructores */
	
	@Inject
	public PageDaoImpl(EntityManager em) {
		super(em);
	}
	
	/* Metodos */
	
	// recupera una pagina por id
	@Override
	public Page getPage(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo ni vacio.");
		logger.debug("Recuperando pagina con id {}", id);
		Page p = get(id, Page.class);
		return p;
	}

	// recupera el id de una pagina a partir del nombre
	@Override
	public Long findPageId(String titleUrl) {
		if (titleUrl == null || titleUrl.length() == 0) throw new IllegalArgumentException("El parametro titleUrl no puede ser nulo ni vacio.");
		logger.debug("Recuperando pagina con nombre {}", titleUrl);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("titleUrl", titleUrl);
		List<Long> ids = listIds("select p.id from Page p where p.titleUrl = :titleUrl", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varias p�ginas para el nombre especificado. La columna de base de datos deber�a tener una restricci�n de unicidad que no lo habr�a permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		return id;
	}

	// cuenta el n�mero total de p�ginas del sistema
	@Override
	public Long countAllPages() {
		String q = "select count(p.id) from Page p";
		Long count = count(q, null);
		return count;
	}
	// recupera todas las p�ginas del sistema con paginaci�n, ordenadas por id descendentemente
	@Override
	public List<Page> getAllPages(int firstResult, int maxResults) {
		logger.debug("Recuperando {} elementos de todas las p�ginas del sistema. Primer elemento: {}", maxResults, firstResult);
		List<Page> pages = find("select p from Page p order by p.id desc", null, Page.class, firstResult, maxResults);
		if (pages.size() == 0) return Collections.emptyList();
		return pages;
	}
	// recupera todas las p�ginas del sistema
	@Override
	public List<Page> getAllPages() {
		logger.debug("Recuperando todas las p�ginas del sistema");
		List<Page> pages = find("select p from Page p order by p.id desc", null, Page.class);
		if (pages.size() == 0) return Collections.emptyList();
		return pages;
	}
	
	
	// inserta una p�gina en la base de datos
	@Override
	public void savePage(Page page) {
		if (page == null) throw new IllegalArgumentException("El objeto page a persistir no puede ser nulo.");
		logger.debug("Insertando page con titulo '{}' en base de datos", page.getTitle());
		save(page);
	}

	// modifica una p�gina en la base de datos
	@Override
	public void modifyPage(Page page) {
		if (page == null) throw new IllegalArgumentException("El objeto page a persistir no puede ser nulo.");
		logger.debug("Modificando page con titulo '{}' en base de datos", page.getTitle());
		Page p = getPage(page.getId());
		p.setContent(page.getContent());
		if (!p.getHost().getId().equals(page.getHost().getId())) p.setHost(page.getHost());
		p.setLastModificationDate(page.getLastModificationDate());
		p.setMetaDescription(page.getMetaDescription());
		p.setTitle(page.getTitle());
		p.setTitleUrl(page.getTitleUrl());
//		flush(); // este flush deber�a ir en un interceptor de AOP asociado a los servicios o a los actions
	}

	// borra una p�gina de la base de datos
	@Override
	public void deletePage(Long id) {
		if (id == null) throw new IllegalArgumentException("El id de la pagina a borrar no puede ser nulo.");
		logger.debug("Borrando page con id {} de la base de datos", id);
		Page p = getReference(id, Page.class);
		delete(p);
	}

}