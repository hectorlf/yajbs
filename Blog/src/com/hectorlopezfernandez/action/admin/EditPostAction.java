package com.hectorlopezfernandez.action.admin;

import java.util.ArrayList;
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
import com.hectorlopezfernandez.model.Author;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.service.UserService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/editPost.action")
public class EditPostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(EditPostAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	@Inject private UserService userService;
	
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
	private Long hostId; // current host
	private Long authorId; // current author
	private List<Long> selectedTagsIds; // currently selected tags, if any
	private List<Host> hosts;
	private List<Author> authors;
	private List<Tag> tags;
	private boolean editing = true; // es modificacion
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a EditPostAction.execute");
		Post p = postService.getPost(id);
		title = p.getTitle();
		titleUrl = p.getTitleUrl();
		metaDescription = p.getMetaDescription();
		excerpt = p.getExcerpt();
		content = p.getContent();
		headerImageUrl = p.getHeaderImageUrl();
		sideImageUrl = p.getSideImageUrl();
		commentsAllowed = !p.isCommentsClosed();
		hostId = getContext().getAlias().getHost().getId();
		authorId = getContext().getLoggedUser().getId();
		selectedTagsIds = new ArrayList<Long>();
		if (p.getTags() != null && p.getTags().size() > 0) {
			for (Tag t : p.getTags()) {
				selectedTagsIds.add(t.getId());
			}
		}
		hosts = blogService.getAllHosts();
		authors = userService.getAllAuthors();
		tags = postService.getAllTags();
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

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getHostId() {
		return hostId;
	}

	public List<Host> getHosts() {
		return hosts;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public boolean isEditing() {
		return editing;
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

	public String getExcerpt() {
		return excerpt;
	}

	public String getContent() {
		return content;
	}

	public String getHeaderImageUrl() {
		return headerImageUrl;
	}

	public String getSideImageUrl() {
		return sideImageUrl;
	}

	public boolean isCommentsAllowed() {
		return commentsAllowed;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public List<Long> getSelectedTagsIds() {
		return selectedTagsIds;
	}

}