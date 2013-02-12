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
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Page;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PageService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/editPage.action")
public class EditPageAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(EditPageAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PageService pageService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private String title;
	private String titleUrl;
	private String metaDescription;
	private String content;
	private Long hostId; // current host
	private List<Host> hosts;
	private boolean editing = true; // es modificación
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a EditPageAction.execute");
		Page p = pageService.getPage(id);
		title = p.getTitle();
		titleUrl = p.getTitleUrl();
		metaDescription = p.getMetaDescription();
		content = p.getContent();
		hostId = p.getHost().getId();
		hosts = blogService.getAllHosts();
		return new ForwardResolution("/WEB-INF/jsp/admin/page-form.jsp");
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

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public String getContent() {
		return content;
	}

	public List<Host> getHosts() {
		return hosts;
	}

	public Long getHostId() {
		return hostId;
	}

	public boolean isEditing() {
		return editing;
	}

}