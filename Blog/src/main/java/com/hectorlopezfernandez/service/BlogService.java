package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.model.Language;
import com.hectorlopezfernandez.model.Preferences;

public interface BlogService {

	// recupera las preferencias
	public Preferences getPreferences();

	// recupera la lista de idiomas
	public List<Language> getAllLanguages();

}