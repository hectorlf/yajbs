package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.BlogActionBeanContext;
import com.hectorlopezfernandez.utils.OWASPUtils;

@UrlBinding("/addComment.action")
public class AddCommentAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(AddCommentAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private Integer postId;
	private Integer registeredUserId;
	private String name;
	private String email;
	private String url;
	private String commentText;
	private String captchaText;
	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a AddCommentAction.execute");
		try {
			commentText = OWASPUtils.parsePostComment(commentText).getCleanHTML();
		} catch(Exception e) {
			logger.error("Ocurrió un error parseando el comentario de entrada: {} - {}", e.getClass().getName(),e.getMessage());
		}
		return new ForwardResolution("/WEB-INF/jsp/comment.jsp");
	}
	
	// Getters y setters

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public String getCommentText() {
		return commentText;
	}

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

}