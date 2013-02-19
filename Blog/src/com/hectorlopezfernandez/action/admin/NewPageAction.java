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
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/newPage.action")
public class NewPageAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(NewPageAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	
	// campos que guarda el actionbean
	
	private List<Host> hosts;
	private Long hostId; // current host
	private boolean editing = false; // es nueva página
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a NewPageAction.execute");
		hosts = blogService.getAllHosts();
		hostId = getContext().getAlias().getHost().getId();
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

	public List<Host> getHosts() {
		return hosts;
	}

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	public Long getHostId() {
		return hostId;
	}

	public boolean isEditing() {
		return editing;
	}

	// HACK! getters necesarios para evitar un null pointer en el EL de JSP
	public String getTitle() { return null;	}
	public String getTitleUrl() { return null; }
	public String getMetaDescription() { return null; }
	public String getContent() { return null; }

}