package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/tags/{name}/{overhead}")
public class TagsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(TagsAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private String name;
	private String overhead;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a TagsAction.execute");
		logger.debug("name: {}", name);
		logger.debug("overhead: {}", overhead);
		// si overhead contiene algo, la url no puede ser válida y se manda un 404
		if (overhead != null && overhead.length() > 0) return new ForwardResolution(Error404Action.class);
		// si no se ha indicado un nombre de tag, se manda al listado
		if (name == null || name.length() == 0) return new ForwardResolution(ListTagsAction.class);
		// se listan los post con el tag indicado, si existe algo
		ForwardResolution fr = new ForwardResolution(ListTagPostsAction.class);
		fr.addParameter(ListTagPostsAction.PARAM_NAME, name);
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

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

}