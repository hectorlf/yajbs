package com.hectorlopezfernandez.action;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.owasp.reform.Reform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.service.TagService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

public class ListTagPostsAction implements ActionBean, ValidationErrorHandler {

	private final static Logger logger = LoggerFactory.getLogger(ListTagPostsAction.class);
	public final static String PARAM_NAME = "name";

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	@Inject private TagService tagService;
	
	// campos que guarda el actionbean

	private String name;
	private Integer page;
	private String tagName;
	private List<Post> posts;
	private PaginationInfo paginationInfo;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListTagPostsAction.execute");
		if (name == null || name.length() == 0) return new ErrorResolution(404);
		// se cargan las preferencias
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		// se busca el tag por nombre y, si existe, los post asociados
		//TODO REVISAR ESTO
		Long tagId = tagService.findTagId(name);
		if (tagId != null) {
			Tag tag = tagService.getTag(tagId);
			tagName = tag.getName();
			paginationInfo = postService.computePaginationOfPostsForTag(tagId, page);
			posts = postService.listPostsForTag(tagId, paginationInfo);
		} else {
			// si el tag no existe, se procesa la cadena de entrada, por si los hackers
			tagName = Reform.HtmlEncode(StringUtils.abbreviate(name, 20));
			posts = Collections.emptyList();
		}
		return new ForwardResolution("/WEB-INF/pebble/tag-posts-list.pebble");
	}

	@Override
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		// La pagina puede generar un error de validacion, se controla para no devolver un 500
		errors.remove("page");
		return null;
	}

	// Getters y setters

	public String getName() {
		return name;
	}

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

	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
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

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}