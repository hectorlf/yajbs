package com.hectorlopezfernandez.service;

import java.util.List;

import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.BlogLanguage;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Theme;

public interface BlogService {

	// recupera un objeto alias por id
	public Alias getAlias(Long id);

	// recupera un objeto alias por el nombre único
	public Alias getAliasByName(String hostname);

	// recupera el id de un alias por el nombre único
	public Long getAliasIdByName(String hostname);

	// recupera un objeto host por id
	public Host getHost(Long id);

	// recupera la lista de Hosts
	public List<Host> getAllHosts();

	// recupera un tema por id
	public Theme getTheme(Long id);

	// recupera la lista de idiomas
	public List<BlogLanguage> getAllLanguages();

}