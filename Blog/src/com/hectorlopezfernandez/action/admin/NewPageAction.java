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
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a NewPageAction.execute");
		hosts = blogService.getAllHosts();
		hostId = getContext().getAlias().getHost().getId();
		return new ForwardResolution("/WEB-INF/jsp/admin/new-page.jsp");
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

}