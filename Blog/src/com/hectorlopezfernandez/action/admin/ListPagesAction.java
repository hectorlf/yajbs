package com.hectorlopezfernandez.action.admin;

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dto.PaginationInfo;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.service.PageService;

@UrlBinding("/admin/listPages.action")
public class ListPagesAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListPagesAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PageService pageService;
	
	// campos que guarda el actionbean

	private Integer page; // current page
	private PaginationInfo paginationInfo;
	private List<Page> pages;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListPagesAction.execute");
		paginationInfo = pageService.computePaginationOfPages(page);
		pages = pageService.getAllPages(paginationInfo);
		return new ForwardResolution("/WEB-INF/jsp/admin/page-list.jsp");
	}
	
	// Getters y setters

	@Override
	public BlogActionBeanContext getContext() {
		return ctx;
	}
	@Override
	public void setContext(ActionBeanContext ctx) {
		this.ctx = (BlogActionBeanContext)ctx;
	}

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}

	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
	}

	public List<Page> getPages() {
		return pages;
	}

}