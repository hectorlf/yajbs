package com.hectorlopezfernandez.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.BlogDao;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.BlogLanguage;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Theme;

public class BlogServiceImpl implements BlogService {

	private final static Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

	private final BlogDao blogDao;
	
	/* Constructores */
	
	@Inject
	public BlogServiceImpl(BlogDao blogDao) {
		if (blogDao == null) throw new IllegalArgumentException("El parametro blogDao no puede ser nulo.");
		this.blogDao = blogDao;
	}
	
	/* Metodos */

	@Override
	public Alias getAlias(Long id) {
		logger.debug("Recuperando alias por id");
		Alias a = blogDao.getAlias(id);
		return a;
	}

	@Override
	public Alias getAliasByName(String hostname) {
		logger.debug("Recuperando alias por nombre");
		Alias a = blogDao.getAliasByName(hostname);
		return a;
	}
	
	@Override
	public Long getAliasIdByName(String hostname) {
		logger.debug("Recuperando id de alias por nombre");
		Long id = blogDao.getAliasIdByName(hostname);
		return id;
	}

	@Override
	public Host getHost(Long id) {
		logger.debug("Recuperando host por id");
		Host h = blogDao.getHost(id);
		return h;
	}
	
	@Override
	public List<Host> getAllHosts() {
		logger.debug("Recuperando todos los host del sistema");
		List<Host> hosts = blogDao.getAllHosts();
		return hosts;
	}

	@Override
	public Theme getTheme(Long id) {
		logger.debug("Recuperando theme por id");
		Theme t = blogDao.getTheme(id);
		return t;
	}

	@Override
	public List<BlogLanguage> getAllLanguages() {
		logger.debug("Recuperando todos los idiomas del sistema");
		List<BlogLanguage> languages = blogDao.getAllLanguages();
		return languages;
	}

}