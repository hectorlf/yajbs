package com.hectorlopezfernandez.action.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.dao.PostDao;
import com.hectorlopezfernandez.model.ArchiveEntry;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.service.UserService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/savePost.action")
public class SavePostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(SavePostAction.class);

	private BlogActionBeanContext ctx;
	
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	@Inject private UserService userService;

	@Inject private PostDao postDao;

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
		Host h = blogService.getHost(hostId);
		Author a = userService.getAuthorById(authorId);
		if (tagIds == null) tagIds = Collections.emptySet();
		List<Tag> tl = new ArrayList<Tag>(tagIds.size());
		for (Long tagId : tagIds) {
			Tag t = postService.getTag(tagId);
			tl.add(t);
		}
		DateTime now = new DateTime();
		ArchiveEntry ae = postDao.findArchiveEntryCreateIfNotExists(now.getYear(), now.getMonthOfYear());
		Post p = new Post();
		p.setArchiveEntry(ae);
		p.setAuthor(a);
		p.setCommentsClosed(!commentsAllowed);
		p.setContent(content);
		p.setExcerpt(excerpt);
		p.setHeaderImageUrl(headerImageUrl);
		p.setHost(h);
		p.setId(id);
		p.setLastModificationDate(now);
		p.setMetaDescription(metaDescription);
		p.setPublicationDate(now);
		p.setSideImageUrl(sideImageUrl);
		p.setTags(tl);
		p.setTitle(title);
		p.setTitleUrl(titleUrl);
		postDao.savePost(p);
		return new RedirectResolution(IndexAction.class);
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