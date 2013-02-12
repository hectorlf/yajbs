package com.hectorlopezfernandez.action;

import javax.inject.Inject;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.service.UserService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/authors/{name}/{overhead}")
public class AuthorsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(AuthorsAction.class);

	private BlogActionBeanContext ctx;
	@Inject	private UserService userService;
	
	// campos que guarda el actionbean
	
	private String name;
	private String overhead;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a AuthorsAction.execute");
		logger.debug("name: {}", name);
		logger.debug("overhead: {}", overhead);
		// si overhead contiene algo, la url no puede ser válida y se manda un 404
		if (overhead != null && overhead.length() > 0) return new ForwardResolution(Error404Action.class);
		// si no hay nombre de autor, se muestra la lista de autores del sistema
		if (name == null || name.length() == 0) return new ForwardResolution(ListAuthorsAction.class);
		// se busca el id del autor por nombre y, si no existe, se envía un 404
		Long id = userService.findAuthorId(name);
		if (id == null) return new ForwardResolution(Error404Action.class);
		// se envia al detalle del autor
		ForwardResolution fr = new ForwardResolution(ViewAuthorAction.class);
		fr.addParameter(ViewAuthorAction.PARAM_ID, id);
		return fr;
	}
	
	// Getters y setters

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public void setName(String name) {
		this.name = name;
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