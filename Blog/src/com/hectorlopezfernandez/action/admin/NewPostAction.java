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
import com.hectorlopezfernandez.model.Tag;
import com.hectorlopezfernandez.model.User;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.service.UserService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;

@UrlBinding("/admin/newPost.action")
public class NewPostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(NewPostAction.class);

	private BlogActionBeanContext ctx;
	@Inject private BlogService blogService;
	@Inject private PostService postService;
	@Inject private UserService userService;
	
	// campos que guarda el actionbean
	
	private List<Host> hosts;
	private Long hostId; // current host
	private List<Author> authors;
	private List<Tag> tags;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a NewPostAction.execute");
		hosts = blogService.getAllHosts();
		hostId = getContext().getAlias().getHost().getId();
		User loggedUser = getContext().getLoggedUser();
		authors = new ArrayList<Author>(1);
		authors.add(userService.getAuthorById(loggedUser.getId()));
		tags = postService.getAllTags();
		return new ForwardResolution("/WEB-INF/jsp/admin/new-post.jsp");
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

	public List<Author> getAuthors() {
		return authors;
	}

	public List<Tag> getTags() {
		return tags;
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

	public Long getHostId() {
		return hostId;
	}

}