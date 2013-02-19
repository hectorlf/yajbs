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
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/listPosts.action")
public class ListPostsAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ListPostsAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean

	private Integer page; // current page
	private PaginationInfo paginationInfo;
	private List<Post> posts;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ListPostsAction.execute");
		paginationInfo = postService.computePaginationOfPosts(page);
		posts = postService.getAllPosts(paginationInfo);
		return new ForwardResolution("/WEB-INF/jsp/admin/post-list.jsp");
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

	public List<Post> getPosts() {
		return posts;
	}

}