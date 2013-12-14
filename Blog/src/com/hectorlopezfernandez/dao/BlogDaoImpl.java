package com.hectorlopezfernandez.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.exception.DataIntegrityException;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Language;
import com.hectorlopezfernandez.model.Theme;

public class BlogDaoImpl extends BaseDaoImpl implements BlogDao {

	private final static Logger logger = LoggerFactory.getLogger(BlogDaoImpl.class);

	/* Constructores */
	
	@Inject
	public BlogDaoImpl(EntityManager em) {
		super(em);
	}
	
	/* Metodos */

	// recupera un alias por id
	@Override
	public Alias getAlias(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Recuperando alias con id {}", id);
		Alias a = get(id, Alias.class);
		return a;
	}

	// recupera un alias a partir del nombre
	@Override
	public Alias getAliasByName(String hostname) {
		if (hostname == null || hostname.length() == 0) throw new IllegalArgumentException("El parametro hostname no puede ser nulo ni vacio.");
		logger.debug("Recuperando alias con nombre {}", hostname);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("hostname", hostname);
		List<Long> ids = listIds("select a.id from Alias a where a.name = :hostname", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varios alias para el nombre especificado. La columna de base de datos debería tener una restricción de unicidad que no lo habría permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		Alias host = get(id, Alias.class);
		return host;
	}
	
	// recupera el id de un alias a partir del nombre
	@Override
	public Long getAliasIdByName(String hostname) {
		if (hostname == null || hostname.length() == 0) throw new IllegalArgumentException("El parametro hostname no puede ser nulo ni vacio.");
		logger.debug("Recuperando id de alias con nombre {}", hostname);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("hostname", hostname);
		List<Long> ids = listNamedIds("Alias.aliasIdByName", params);
		if (ids.size() > 1) throw new DataIntegrityException("Se han encontrado varios alias para el nombre especificado. La columna de base de datos debería tener una restricción de unicidad que no lo habría permitido.");
		if (ids.size() == 0) return null;
		Long id = ids.get(0);
		return id;
	}

	@Override
	public Host getHost(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Recuperando host con id {}", id);
		Host h = get(id, Host.class);
		return h;
	}
	
	@Override
	public List<Host> getAllHosts() {
		logger.debug("Recuperando todos los host del sistema");
		List<Host> hosts = find("select h from Host h", null, Host.class);
		if (hosts.size() == 0) return Collections.emptyList();
		return hosts;
	}

	@Override
	public Theme getTheme(Long id) {
		if (id == null) throw new IllegalArgumentException("El parametro id no puede ser nulo.");
		logger.debug("Recuperando theme con id {}", id);
		Theme t = get(id, Theme.class);
		return t;
	}

	@Override
	public List<Language> getAllLanguages() {
		logger.debug("Recuperando todos los idiomas del sistema");
		// Query convertida a NamedQuery
		//List<Language> languages = find("select bl from Language bl", null, Language.class);
		List<Language> languages = findNamed("allLanguages", null, Language.class);
		if (languages.size() == 0) return Collections.emptyList();
		return languages;
	}

}