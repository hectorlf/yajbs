package com.hectorlopezfernandez.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.service.UserService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/viewAuthor.action")
public class ViewAuthorAction implements ActionBean, ValidationErrorHandler {

	private final static Logger logger = LoggerFactory.getLogger(ViewAuthorAction.class);
	public final static String PARAM_ID = "id";

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject	private UserService userService;
	@Inject	private PostService postService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private Integer page;
	private Author author;
	private List<Post> posts;
	private PaginationInfo paginationInfo;

	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ViewAuthorAction.execute");
		if (id == null) return new ErrorResolution(404);
		// se cargan las preferencias
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		// se carga el autor
		author = userService.getAuthorById(id);
		if (author == null) return new ErrorResolution(404);
		// se cargan los posts relacionados, si hay
		paginationInfo = postService.computePaginationOfPostsForAuthor(id, page);
		posts = postService.listPostsForAuthor(id, paginationInfo);
		return new ForwardResolution("/WEB-INF/jsp/author.jsp");
	}

	@Override
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		// La pagina puede generar un error de validacion, se controla para no devolver un 500
		errors.remove("page");
		return null;
	}

	// Getters y setters

	public void setId(Long id) {
		this.id = id;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Author getAuthor() {
		return author;
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

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}