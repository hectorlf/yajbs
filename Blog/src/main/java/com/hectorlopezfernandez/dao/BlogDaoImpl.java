package com.hectorlopezfernandez.dao;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.model.Language;
import com.hectorlopezfernandez.model.Preferences;

public class BlogDaoImpl extends BaseDaoImpl implements BlogDao {

	private final static Logger logger = LoggerFactory.getLogger(BlogDaoImpl.class);

	/* Constructores */
	
	@Inject
	public BlogDaoImpl(EntityManager em) {
		super(em);
	}
	
	/* Metodos */

	@Override
	public Preferences getPreferences() {
		logger.debug("Recuperando objeto preferencias con id {}", Preferences.ID);
		Preferences p = get(Preferences.ID, Preferences.class);
		return p;
	}

	@Override
	public void updatePreferences(Preferences prefs) {
		if (prefs == null) throw new IllegalArgumentException("El parametro prefs no puede ser nulo ni vacio.");
		logger.debug("Actualizando objeto preferencias con id {}", Preferences.ID);
		Preferences p = get(Preferences.ID, Preferences.class);
		p.setMaxPostAgeInDaysForFeeds(prefs.getMaxPostAgeInDaysForFeeds());
		p.setPaginateIndexPage(prefs.getPaginateIndexPage());
		p.setPostsPerIndexPage(prefs.getPostsPerIndexPage());
		p.setTagline(prefs.getTagline());
		p.setTitle(prefs.getTitle());
	}

	@Override
	public List<Language> getAllLanguages() {
		logger.debug("Recuperando todos los idiomas del sistema");
		List<Language> languages = find("select bl from Language bl", null, Language.class);
		if (languages.size() == 0) return Collections.emptyList();
		return languages;
	}

}