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
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/admin/listTags.action")
public class ListTagsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListTagsAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean

	private Integer page; // current page
	private PaginationInfo paginationInfo;
	private List<Tag> tags;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListTagsAction.execute");
		paginationInfo = postService.computePaginationOfTags(page);
		tags = postService.getAllTags(paginationInfo);
		return new ForwardResolution("/WEB-INF/jsp/admin/tag-list.jsp");
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

	public void setPostService(PostService postService) {
		this.postService = postService;
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

	public List<Tag> getTags() {
		return tags;
	}

}