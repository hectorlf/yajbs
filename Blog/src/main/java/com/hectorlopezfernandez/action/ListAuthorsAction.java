package com.hectorlopezfernandez.action;

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.service.UserService;

public class ListAuthorsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListAuthorsAction.class);

	private BlogActionBeanContext ctx;
	@Inject private UserService userService;
	
	// campos que guarda el actionbean
	
	private List<Author> authors;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListAuthorsAction.execute");
		// se cargan las preferencias
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		// se recupera la lista completa de tags
		authors = userService.getAllAuthors();
		return new ForwardResolution("/WEB-INF/jsp/author-list.jsp");
	}
	
	// Getters y setters

	public List<Author> getAuthors() {
		return authors;
	}

	
	// contexto y servicios

	@Override
	public BlogActionBeanContext getContext() {
		return ctx;
	}
	@Override
	public void setContext(ActionBeanContext ctx) {
		this.ctx = (BlogActionBeanContext)ctx;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}