package com.hectorlopezfernandez.integration;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.localization.LocalePicker;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.hectorlopezfernandez.model.Language;
import com.hectorlopezfernandez.model.User;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.utils.Constants;

public class BlogLocalePicker implements LocalePicker {

    /** Log instance for use within the class. */
	private final static Logger logger = LoggerFactory.getLogger(BlogLocalePicker.class);

    private final Injector theInjector;
    
    
    /**
     * Constructores
     */
    @Inject
    public BlogLocalePicker(Injector anInjector) {
        theInjector = anInjector;
    }
    
    /**
     * No se configura nada en el inicio
     */
    public void init(Configuration configuration) throws Exception {
    	logger.debug("Inicializando componente BlogLocalePicker");
    }

    /**
     * @param request the request being processed
     * @return a Locale to use in processing the request
     */
	public Locale pickLocale(HttpServletRequest request) {
    	// si existe un usuario autenticado en el sistema, se usa su lenguaje preferido
    	if (SecurityUtils.getSubject().isAuthenticated()) {
    		User u = (User)request.getAttribute(Constants.LOGGED_USER_REQUEST_ATTRIBUTE_NAME);
    		Locale l = u.getLanguage().toLocale();
    		return l;
    	}

    	BlogService bs = theInjector.getInstance(BlogService.class);
    	List<Language> systemLanguages = bs.getAllLanguages();

    	// si el usuario ha seleccionado un lenguaje con una cookie o un parametro de url, se utiliza
    	// comprobando primero que sea un locale soportado por el sistema
    	String localeFromCookie = findLocaleCookie(request);
    	if (localeFromCookie != null && localeFromCookie.length() > 0) {
    		Locale loc = Locale.forLanguageTag(localeFromCookie);
    		for (Language lang : systemLanguages) {
    			if (lang.toLocale().equals(loc)) return loc;
    		}
    	}
    	
    	// si no hay ningun idioma preseleccionado, se utiliza el lenguaje del navegador
    	// se inserta un atributo en la request para que el actioncontext pueda saber que el locale se seleccionó por defecto
    	request.setAttribute(Constants.DEFAULT_LOCALE_SELECTED, Boolean.TRUE);

        Enumeration<Locale> preferredLocales = request.getLocales();
        while (preferredLocales.hasMoreElements()) {
        	Locale preferredLocale = preferredLocales.nextElement();
            for (Language systemLanguage : systemLanguages) {
            	Locale systemLocale = systemLanguage.toLocale();
            	if (systemLocale.equals(preferredLocale)) return systemLocale;
            }
        }
        // We didn't get a match, return something!
        return systemLanguages.get(0).toLocale();
    }

	/**
	 * Siempre se usará UTF-8
	 */
	public String pickCharacterEncoding(HttpServletRequest request, Locale locale) {
		return "utf-8";
	}

	
	/*
	 * Metodos de utilidad
	 */
	
	private String findLocaleCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) return null;
		for (int i = 0; i < cookies.length; i++) {
			if (Constants.LOCALE_COOKIE_NAME.equals(cookies[i].getName())) return cookies[i].getValue();
		}
		return null;
	}

}