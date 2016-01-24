package com.hectorlopezfernandez.action;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.dto.SearchResult;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Preferences;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.SearchService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

@UrlBinding("/search.action")
public class SearchAction implements ActionBean, ValidationErrorHandler {

	private final static Logger logger = LoggerFactory.getLogger(SearchAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private SearchService searchService;
	
	// campos que guarda el actionbean
	
	private String q;
	private Integer page;
	private List<SearchResult> results;
	private PaginationInfo paginationInfo;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SearchAction.execute");
		// se cargan las preferencias
		Preferences prefs = blogService.getPreferences();
		ctx.setAttribute("preferences", prefs);
		paginationInfo = searchService.computePagination(page);
		// si el texto de busqueda es vacio, no llamamos al servicio de busqueda
		if (q == null || q.trim().length() == 0) {
			results = Collections.emptyList();
		} else {
			// se buscan las coincidencias y se recupera el listado de elementos coincidentes
			q = q.trim();
			results = searchService.search(q);
		}
		return new ForwardResolution("/WEB-INF/pebble/search-results.pebble");
	}

	@Override
	public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
		// La pagina puede generar un error de validacion, se controla para no devolver un 500
		errors.remove("page");
		return null;
	}

	// Getters y setters

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public List<SearchResult> getResults() {
		return results;
	}

	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
	}

	@Override
	public BlogActionBeanContext getContext() {
		return ctx;
	}
	@Override
	public void setContext(ActionBeanContext ctx) {
		this.ctx = (BlogActionBeanContext)ctx;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

}