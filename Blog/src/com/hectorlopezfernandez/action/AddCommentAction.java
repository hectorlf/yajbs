package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.service.PostService;
import com.hectorlopezfernandez.utils.OWASPUtils;

@UrlBinding("/addComment.action")
public class AddCommentAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(AddCommentAction.class);

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private Long postId;
	private String author;
	private String email;
	private String url;
	private String commentText;
	private String recaptcha_challenge_field;
	private String recaptcha_response_field;

	
	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a AddCommentAction.execute");
		// si no se recibe el id de post, no podemos hacer absolutamente nada
		if (postId == null) return new ForwardResolution(Error404Action.class);
		Post p = postService.getPost(postId);
		if (recaptcha_challenge_field == null || recaptcha_challenge_field.length() == 0 || recaptcha_response_field == null || recaptcha_response_field.length() == 0) {
			ctx.setFlashAttribute("author", author);
			return new RedirectResolution(ViewPostAction.class).addParameter(ViewPostAction.PARAM_ID, p.getId());
		}
		// se cargan las preferencias
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		// se comprueba el captcha
		String remoteAddr = ctx.getRemoteAddress();
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey(prefs.getReCaptchaPrivateKey());
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, recaptcha_challenge_field, recaptcha_response_field);
		if (reCaptchaResponse.isValid()) {
			System.out.println("Answer was entered correctly!");
		} else {
			System.out.println("Answer is wrong");
		}
		try {
			commentText = commentText + url + email + author + postId;
			commentText = OWASPUtils.parsePostComment(commentText).getCleanHTML();
		} catch(Exception e) {
			logger.error("Ocurrió un error parseando el comentario de entrada: {} - {}", e.getClass().getName(),e.getMessage());
		}
		return new RedirectResolution(ViewPostAction.class).addParameter(ViewPostAction.PARAM_ID, p.getId());
	}
	
	// Getters y setters

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
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