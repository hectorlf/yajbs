package com.hectorlopezfernandez.action;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

public class ListPostsAction implements ActionBean, ValidationErrorHandler {

	private final static Logger logger = LoggerFactory.getLogger(ListPostsAction.class);

	public final static String PARAM_YEAR = "year";
	public final static String PARAM_MONTH = "month";

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private Integer year;
	private Integer month;
	private Integer page;
	private DateTime searchDate;
	private List<Post> posts;
	private PaginationInfo paginationInfo;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListPostsAction.execute");
		if (year == null) return new ErrorResolution(404);
		try {
			// se intenta convertir la fecha a DateTime. Si no existe la fecha, 404.
			int y = year.intValue();
			int m = month == null ? 1 : month.intValue();
			searchDate = new DateTime(y, m, 1, 0, 0);
		} catch(Exception e) {
			return new ErrorResolution(404);
		}
		// se recuperan las preferencias
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		// se realiza la busqueda de posts
		paginationInfo = postService.computePaginationOfPostsForDate(year, month, page);
		posts = postService.listPostsForDate(year, month, paginationInfo);
		return new ForwardResolution("/WEB-INF/pebble/post-list.pebble");
	}

	@Override
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		// La pagina puede generar un error de validacion, se controla para no devolver un 500
		errors.remove("page");
		return null;
	}

	// Getters y setters

	public List<Post> getPosts() {
		return posts;
	}

	public DateTime getSearchDate() {
		return searchDate;
	}

	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
	}

	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setPage(Integer page) {
		this.page = page;
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

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}