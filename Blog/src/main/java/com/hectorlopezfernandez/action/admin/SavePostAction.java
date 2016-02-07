package com.hectorlopezfernandez.action.admin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dao.TagDao;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.service.AdminPostService;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/admin/savePost.action")
public class SavePostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SavePostAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private AdminPostService postService;
	@Inject private TagDao tagDao;

	// campos que guarda el actionbean
	
	private Long id;
	private String title;
	private String titleUrl;
	private String metaDescription;
	private String excerpt;
	private String content; 
	private boolean commentsAllowed;
	private Long authorId;
	private Set<Long> tagIds;
	private String concatenatedRelatedPosts; 

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a SavePostAction.execute");
		// se guarda el post
		Post p = new Post();
		p.setCommentsClosed(!commentsAllowed);
		p.setContent(content);
		p.setExcerpt(excerpt);
		p.setId(id);
		p.setMetaDescription(metaDescription);
		p.setTitle(title);
		p.setTitleUrl(titleUrl);
		Set<Long> postIds = idStringToLongSet(concatenatedRelatedPosts);
		if (id == null) postService.savePost(p, authorId, tagIds, postIds);
		else postService.modifyPost(p, authorId, tagIds, postIds);
		// TODO encontrar otra forma de actualizar la cuenta de posts etiquetados en un tag
		tagDao.updateTagRefCounts();
		return new RedirectResolution(ListPostsAction.class);
	}

	private Set<Long> idStringToLongSet(String idString) {
		if (idString == null || idString.trim().isEmpty()) return Collections.emptySet();
		String[] ids = idString.split(",");
		Set<Long> postIds = new HashSet<Long>();
		for (String id : ids) {
			postIds.add(Long.valueOf(id));
		}
		return postIds;
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

	public void setPostService(AdminPostService postService) {
		this.postService = postService;
	}

	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
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

	public void setCommentsAllowed(boolean commentsAllowed) {
		this.commentsAllowed = commentsAllowed;
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

	public void setConcatenatedRelatedPosts(String concatenatedRelatedPosts) {
		this.concatenatedRelatedPosts = concatenatedRelatedPosts;
	}

}