package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Language;
import com.hectorlopezfernandez.model.Preferences;

public interface BlogDao {

	public Preferences getPreferences();

	public void updatePreferences(Preferences prefs);

	// recupera todos los idiomas
	public List<Language> getAllLanguages();

}