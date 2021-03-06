package com.hectorlopezfernandez.action.admin;

import java.util.Collections;
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
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.TagService;
import com.hectorlopezfernandez.service.UserService;

@UrlBinding("/admin/newPost.action")
public class NewPostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(NewPostAction.class);

	private BlogActionBeanContext ctx;
	@Inject private TagService tagService;
	@Inject private UserService userService;
	
	// campos que guarda el actionbean

	private boolean commentsAllowed;
	private Long authorId; // current author
	private List<Author> authors;
	private List<Tag> tags;
	private boolean editing = false; // es nuevo post
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a NewPostAction.execute");
		commentsAllowed = false; // TODO esto quizas deberia salir de las preferencias del host
		authorId = getContext().getLoggedUser().getId();
		authors = userService.getAllAuthors();
		tags = tagService.getAllTags();
		return new ForwardResolution("/WEB-INF/jsp/admin/post-form.jsp");
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

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public boolean isCommentsAllowed() {
		return commentsAllowed;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public boolean isEditing() {
		return editing;
	}

	// HACK! getters que hacen falta para evitar un nullpointerexception en el EL del JSP
	
	public String getTitle() { return null; }
	public String getTitleUrl() { return null; }
	public String getMetaDescription() { return null; }
	public String getExcerpt() { return null; }
	public String getContent() { return null; }
	public String getHeaderImageUrl() { return null; }
	public String getSideImageUrl() { return null; }
	public List<Long> getSelectedTagsIds() { return Collections.emptyList(); }
	public String getConcatenatedRelatedPosts() { return null; }

}