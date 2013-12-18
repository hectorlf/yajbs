package com.hectorlopezfernandez.integration;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.StandardCacheManager;
import org.thymeleaf.stripes.StripesTemplateEngine;
import org.thymeleaf.stripes.context.StripesWebContext;
import org.thymeleaf.stripes.messageresolver.PropertyResourceBundleMessageResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet(urlPatterns={"*.html"},loadOnStartup=4)
public class ThymeleafServlet implements Servlet {

	private final static Logger logger = LoggerFactory.getLogger(ThymeleafServlet.class);

	private ServletConfig servletConfig;
	private TemplateEngine templateEngine;

	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
		// Engine creation
        templateEngine = new StripesTemplateEngine();
		// Template resolver for typical WEB-INF resources
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("HTML5"); // HTML5 output mode
        templateEngine.setTemplateResolver(templateResolver);
        // Cache config
        StandardCacheManager cacheManager = new StandardCacheManager();
        cacheManager.setTemplateCacheValidityChecker(null);
        cacheManager.setExpressionCacheMaxSize(300);
        cacheManager.setFragmentCacheMaxSize(50);
        cacheManager.setMessageCacheMaxSize(0);
        cacheManager.setTemplateCacheMaxSize(20);
        templateEngine.setCacheManager(cacheManager);
        // Configured PropertyResourceBundles
        PropertyResourceBundleMessageResolver messageResolver = new PropertyResourceBundleMessageResolver();
        messageResolver.addBundleFromProperties("StripesResources");
        templateEngine.addMessageResolver(messageResolver);
	}

	@Override
	public void destroy() {
		if (templateEngine != null && templateEngine.isInitialized() && templateEngine.getCacheManager() != null) {
			templateEngine.getCacheManager().clearAllCaches();
		}
	}

	
	@Override
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	@Override
	public String getServletInfo() {
		return "ThymeleafServlet";
	}


	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		logger.debug("Entrando a ThymeleafServlet.service");
		
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		
        /*
         * Write the response headers
         */
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        
        StripesWebContext ctx = new StripesWebContext(request, response, request.getServletContext(), request.getLocale());
        templateEngine.process(request.getServletPath(), ctx, response.getWriter());
	}

}