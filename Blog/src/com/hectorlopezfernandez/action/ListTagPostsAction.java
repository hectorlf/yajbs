package com.hectorlopezfernandez.action;

import java.util.Collections;
import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.lang.StringUtils;
import org.owasp.reform.Reform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;
import com.hectorlopezfernandez.dto.PaginationInfo;

public class ListTagPostsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListTagPostsAction.class);
	public final static String PARAM_NAME = "name";

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean

	private String name;
	private Integer page;
	private String tagName;
	private List<Post> posts;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListTagPostsAction.execute");
		if (name == null || name.length() == 0) return new ForwardResolution(Error404Action.class);
		// se cargan las preferencias
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		// se busca el tag por nombre y, si existe, los post asociados
		//TODO REVISAR ESTO
		Long tagId = postService.findTagId(name);
		if (tagId != null) {
			Tag tag = postService.getTag(tagId);
			tagName = tag.getName();
			PaginationInfo pi = postService.computePaginationOfPostsForTag(tagId, page, prefs);
			posts = postService.listPostsForTag(tagId, pi);
		} else {
			// si el tag no existe, se procesa la cadena de entrada, por si los hackers
			tagName = Reform.HtmlEncode(StringUtils.abbreviate(name, 20));
			posts = Collections.emptyList();
		}
		return new ForwardResolution("/WEB-INF/jsp/tag-posts-list.jsp");
	}
	
	// Getters y setters

	public void setName(String name) {
		this.name = name;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getTagName() {
		return tagName;
	}

	public List<Post> getPosts() {
		return posts;
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