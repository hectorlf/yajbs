package com.hectorlopezfernandez.action.admin;

import java.util.Set;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/savePost.action")
public class SavePostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SavePostAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private PostService postService;

	// campos que guarda el actionbean
	
	private Long id;
	private String title;
	private String titleUrl;
	private String metaDescription;
	private String excerpt;
	private String content; 
	private String headerImageUrl;
	private String sideImageUrl;
	private boolean commentsAllowed;
	private Long hostId;
	private Long authorId;
	private Set<Long> tagIds;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SavePostAction.execute");
		Post p = new Post();
		p.setCommentsClosed(!commentsAllowed);
		p.setContent(content);
		p.setExcerpt(excerpt);
		p.setHeaderImageUrl(headerImageUrl);
		p.setId(id);
		p.setMetaDescription(metaDescription);
		p.setSideImageUrl(sideImageUrl);
		p.setTitle(title);
		p.setTitleUrl(titleUrl);
		if (id == null) postService.savePost(p, hostId, authorId, tagIds);
		else postService.modifyPost(p, hostId, authorId, tagIds);
		return new RedirectResolution(ListPostsAction.class);
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}

	public void setSideImageUrl(String sideImageUrl) {
		this.sideImageUrl = sideImageUrl;
	}

	public void setCommentsAllowed(boolean commentsAllowed) {
		this.commentsAllowed = commentsAllowed;
	}

	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public void setTagIds(Set<Long> tagIds) {
		this.tagIds = tagIds;
	}
	public Set<Long> getTagIds() {
		return tagIds;
	}

}