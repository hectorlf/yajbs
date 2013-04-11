package com.hectorlopezfernandez.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hectorlopezfernandez.integration.BlogActionBeanContext;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.Host;
import com.hectorlopezfernandez.model.Post;
import com.hectorlopezfernandez.service.PostService;

@UrlBinding("/viewPost.action")
public class ViewPostAction implements ActionBean {

	private final static Logger logger = LoggerFactory.getLogger(ViewPostAction.class);
	public final static String PARAM_ID = "id";

	private BlogActionBeanContext ctx;
	@Inject private PostService postService;
	
	// campos que guarda el actionbean
	
	private Long id;
	private Post post;

	@DefaultHandler
	public Resolution execute() {
		logger.debug("Entrando a ViewPostAction.execute");
		if (id == null) return new ForwardResolution(Error404Action.class);
		// se cargan las preferencias
		Alias alias = ctx.getAlias();
		Host prefs = alias.getHost();
		ctx.setAttribute("preferences", prefs);
		// se carga la página a mostrar
		post = postService.getDetailedPost(id);
		// si no existe, 404
		if (post == null) return new ForwardResolution(Error404Action.class);
		// si los comentarios están activados, se inicializa el captcha
		if (!post.isCommentsClosed()) {
			ReCaptcha rc = ReCaptchaFactory.newReCaptcha(prefs.getReCaptchaPublicKey(), prefs.getReCaptchaPrivateKey(), false);
			ctx.setAttribute("reCaptchaHtml", rc.createRecaptchaHtml(null, null));
		}
		return new ForwardResolution("/WEB-INF/jsp/post.jsp");
	}
	
	// Getters y setters

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	
	// contexto y servicios

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