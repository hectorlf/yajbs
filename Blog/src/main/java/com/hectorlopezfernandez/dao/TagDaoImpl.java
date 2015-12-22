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
import com.hectorlopezfernandez.model.Tag;

public class TagDaoImpl extends BaseDaoImpl implements TagDao {

	private final static Logger logger = LoggerFactory.getLogger(TagDaoImpl.class);

	/* Constructores */
	
	@Inject
	public TagDaoImpl(EntityManager em) {
		super(em);
	}
	
	/* Metodos */

	// recupera una lista de tags populares, para presentar en el footer
	@Override
	public List<Tag> findMostPopularTagsForFooter(int numTags) {
		if (numTags < 1) throw new IllegalArgumentException("El parametro numTags debe ser mayor que 0.");
		logger.debug("Recuperando las {} etiquetas mas populares.", numTags);
		List<Tag> tags = find("select t from Tag t order by t.count desc", null, Tag.class, 0, numTags);
		if (tags.size() == 0) return Collections.emptyList();
		return tags;
	}

	// recupera un tag por id
	public Tag getTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Recuperando tag con id: {}", id);
		Tag t = get(id, Tag.class);
		return t;
	}

	// recupera el id de un tag por el nombre adaptado a url
	public Long findTagId(String nameUrl) {
		if (nameUrl == null || nameUrl.length() == 0) throw new IllegalArgumentException("El parametro nameUrl no puede ser nulo ni vacio.");
		logger.debug("Recuperando tag con nombre {}", nameUrl);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("nameUrl", nameUrl);
		List<Long> ids = listIds("select t.id from Tag t where t.nameUrl = :nameUrl", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varios tags para el nombre especificado. La columna de base de datos deberia tener una restriccion de unicidad que no lo habria permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		return id;
	}

	// cuenta el numero total de tags del sistema
	@Override
	public Long countAllTags() {
		String q = "select count(t.id) from Tag t";
		Long count = count(q, null);
		return count;
	}
	// recupera todos los tags del sistema con paginacion, ordenados por id descendentemente
	@Override
	public List<Tag> getAllTags(int firstResult, int maxResults) {
		logger.debug("Recuperando {} elementos de todos los tags del sistema. Primer elemento: {}", maxResults, firstResult);
		List<Tag> tags = find("select t from Tag t order by t.id desc", null, Tag.class, firstResult, maxResults);
		if (tags.size() == 0) return Collections.emptyList();
		return tags;
	}
	// recupera todos los tags del sistema
	@Override
	public List<Tag> getAllTags() {
		logger.debug("Recuperando todos los tags del sistema");
		List<Tag> tags = find("select t from Tag t order by t.id desc", null, Tag.class);
		if (tags.size() == 0) return Collections.emptyList();
		return tags;
	}


	// inserta un tag en la base de datos
	@Override
	public void saveTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El objeto tag a persistir no puede ser nulo.");
		logger.debug("Insertando tag con nombre '{}' en base de datos", tag.getName());
		save(tag);
	}

	// modifica un tag en la base de datos
	@Override
	public void modifyTag(Tag tag) {
		if (tag == null) throw new IllegalArgumentException("El objeto tag a persistir no puede ser nulo.");
		logger.debug("Modificando tag con nombre '{}' en base de datos", tag.getName());
		Tag dbt = getTag(tag.getId());
		dbt.setName(tag.getName());
		dbt.setNameUrl(tag.getNameUrl());
//		flush(); // este flush deberia ir en un interceptor de AOP asociado a los servicios o a los actions
	}

	// borra un tag de la base de datos
	@Override
	public void deleteTag(Long id) {
		if (id == null) throw new IllegalArgumentException("El id del tag a borrar no puede ser nulo.");
		logger.debug("Borrando tag con id {} de la base de datos", id);
		Tag t = getReference(id, Tag.class);
		delete(t);
	}



	// actualiza el contador de referencias de cada Tag
	public void updateTagRefCounts() {
		logger.debug("Actualizando los campos count de cada objeto Tag.");
		List<Tag> tags = find("select t from Tag t", null, Tag.class);
		for (Tag tag : tags) {
//			String q = "select count(p.id) from Post p join p.tags t where t.id = :id";
			String q = "select count(p.id) from Post p where :tag member of p.tags";
			Map<String,Object> params = new HashMap<String,Object>(1);
//			params.put("id", tag.getId());
			params.put("tag", tag);
			Long count = count(q, params);
			tag.setCount(count.intValue());
		}
	}

}