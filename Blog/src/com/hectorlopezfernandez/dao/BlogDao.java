package com.hectorlopezfernandez.dao;

import java.util.List;

import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.BlogLanguage;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Theme;

public interface BlogDao {

	// recupera un alias por id
	public Alias getAlias(Long id);

	// recupera un alias por nombre
	public Alias getAliasByName(String hostname);

	// recupera el id de un alias por nombre
	public Long getAliasIdByName(String hostname);

	// recupera un objeto Host por id
	public Host getHost(Long id);

	// recupera todos los hosts
	public List<Host> getAllHosts();

	// recupera un objeto tema por id
	public Theme getTheme(Long id);

	// recupera todos los idiomas
	public List<BlogLanguage> getAllLanguages();

}