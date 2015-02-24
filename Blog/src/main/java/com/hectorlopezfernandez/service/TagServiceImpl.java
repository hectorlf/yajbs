package com.hectorlopezfernandez.service;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.TagDao;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.model.Tag;

public class TagServiceImpl implements TagService {

	private final static Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

	private final TagDao tagDao;
	
	/* Constructores */
	
	@Inject
	public TagServiceImpl(TagDao tagDao) {
		if (tagDao == null) throw new IllegalArgumentException("El parametro tagDao no puede ser nulo.");
		this.tagDao = tagDao;
	}
	
	/* Metodos */

	@Override
	public List<Tag> getPopularTags(int count) {
		if (count < 1) throw new IllegalArgumentException("El número de etiquetas a recuperar no puede ser menor que 1.");
		logger.debug("Recuperando las {} etiquetas más populares.", count);
		List<Tag> results = tagDao.findMostPopularTagsForFooter(count);
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}

	@Override
	public PaginationInfo computePaginationOfTags(Integer page) {
		Long tagCount = tagDao.countAllTags();
		int total = tagCount == null ? 0 : tagCount.intValue();
		int currentPage = page == null ? 0 : page.intValue();
		int itemsPerPage = 10; // TODO esto quizás debería salir de alguna preferencia global
		PaginationInfo pi = new PaginationInfo(currentPage, itemsPerPage, total);
		return pi;
	}
	@Override
	public List<Tag> getAllTags(PaginationInfo pi) {
		if (pi == null) throw new IllegalArgumentException("El objeto de paginación no puede ser nulo.");
		logger.debug("Recuperando todos los tags del sistema, con paginación. Página solicitada: {}", pi.getCurrentPage());
		List<Tag> tags = tagDao.getAllTags(pi.getFirstItem(), pi.getItemsPerPage());
		return tags;
	}
	@Override
	public List<Tag> getAllTags() {
		logger.debug("Recuperando todas las etiquetas.");
		List<Tag> results = tagDao.getAllTags();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}

	@Override
	public Tag getTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del tag a recuperar no puede ser nulo.");
		logger.debug("Recuperando tag por id: {}", id);
		Tag t = tagDao.getTag(id);
		return t;
	}

	@Override
	public Long findTagId(String nameUrl) {
		if (nameUrl == null || nameUrl.length() == 0) throw new IllegalArgumentException("El nombre del tag a buscar no puede ser nulo.");
		logger.debug("Buscando id de tag por nombre url: {}", nameUrl);
		Long id = tagDao.findTagId(nameUrl);
		return id;
	}


	@Override
	public void saveTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El tag a guardar no puede ser nulo.");
		if (tag.getId() != null) throw new IllegalArgumentException("El id de un tag nuevo debe ser nulo, y este no lo es: " + tag.getId().toString());
		logger.debug("Guardando nuevo tag con nombre: {}", tag.getName());
		tagDao.saveTag(tag);
	}

	@Override
	public void modifyTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El tag a modificar no puede ser nulo.");
		if (tag.getId() == null) throw new IllegalArgumentException("El id de un tag modificado no puede ser nulo.");
		logger.debug("Modificando tag con nombre: {}", tag.getName());
		tagDao.modifyTag(tag);
	}

	@Override
	public void deleteTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del tag a borrar no puede ser nulo.");
		logger.debug("Borrando tag con id: {}", id);
		tagDao.deleteTag(id);
	}

}