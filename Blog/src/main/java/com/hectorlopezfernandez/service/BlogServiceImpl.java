package com.hectorlopezfernandez.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.dao.BlogDao;
import com.hectorlopezfernandez.model.Language;
import com.hectorlopezfernandez.model.Preferences;

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
	public List<Language> getAllLanguages() {
		logger.debug("Recuperando todos los idiomas del sistema");
		List<Language> languages = blogDao.getAllLanguages();
		return languages;
	}

	@Override
	public Preferences getPreferences() {
		logger.debug("Recuperando preferencias");
		Preferences p = blogDao.getPreferences();
		return p;
	}

}