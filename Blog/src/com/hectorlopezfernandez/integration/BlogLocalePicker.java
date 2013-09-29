package com.hectorlopezfernandez.integration;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.localization.LocalePicker;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.hectorlopezfernandez.model.BlogLanguage;
import com.hectorlopezfernandez.model.User;
import com.hectorlopezfernandez.service.BlogService;
import com.hectorlopezfernandez.utils.Constants;

/**
 * <p>Default locale picker that uses a comma separated list of locales in the servlet init
 * parameters to determine the set of locales that are supported by the application.  Then at
 * request time matches the user's preference order list as specified by the headers included
 * in the request until it finds one of those locales in the system list.  If a match cannot be
 * found, the first locale in the system list will be picked.  If there is no list of configured
 * locales then the picker will default the list to a one entry list containing the system locale.</p>
 *
 * <p>Locales are hierarchical, with up to three levels designating language, country and
 * variant.  Only the first level (language) is required.  To provide the best match possible the
 * DefaultLocalePicker tracks the one-level matches, two-level matches and three-level matches. If
 * a three level match is found, it will be returned.  If not the first two-level match will be
 * returned if one was found.  If not, the first one-level match will be returned.  If not even a
 * one-level match is found, the first locale supported by the system is returned.</p>
 *
 * @author Tim Fennell
 */
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
     * Uses a preference matching algorithm to pick a Locale for the user's request.  Iterates
     * through the user's acceptable list of Locales, matching them against the system list. On the
     * way through the list records the first Locale to match on Language, and the first locale to
     * match on both Language and Country.  If a match is found for all three, Language, Country
     * and Variant, it will be returned.  If no three-way match is found the first two-way match
     * found will be returned.  If no two-way match way found the first one-way match found will
     * be returned.  If no one way match was found, the default system locale will be returned.
     *
     * @param request the request being processed
     * @return a Locale to use in processing the request
     */
	public Locale pickLocale(HttpServletRequest request) {
    	// si existe un usuario autenticado en el sistema, se usa su lenguaje preferido
    	if (SecurityUtils.getSubject().isAuthenticated()) {
    		logger.debug("Existe un usuario autenticado, se utiliza el locale que tiene configurado.");
    		User u = (User)request.getAttribute(Constants.LOGGED_USER_REQUEST_ATTRIBUTE_NAME);
    		Locale l = u.getLanguage().toLocale();
    		return l;
    	}
    	
    	// si el usuario ha seleccionado un lenguaje con una cookie o un parametro de url, se utiliza
    	
    	// si no hay ningun idioma preseleccionado, se utiliza el lenguaje del navegador
    	BlogService bs = theInjector.getInstance(BlogService.class);
    	List<BlogLanguage> systemLanguages = bs.getAllLanguages();
    	
        Locale oneWayMatch = null;
        Locale twoWayMatch= null;

        Enumeration<Locale> preferredLocales = request.getLocales();
        while (preferredLocales.hasMoreElements()) {
        	Locale preferredLocale = preferredLocales.nextElement();
            for (BlogLanguage systemLanguage : systemLanguages) {
/*
                if ( systemLocale.getLanguage().equals(preferredLocale.getLanguage()) ) {

                    // We have a language match, let's go for two!
                    oneWayMatch = (oneWayMatch == null ? systemLocale : oneWayMatch);
                    String systemCountry = systemLocale.getCountry();
                    String preferredCountry = preferredLocale.getCountry();

                    if ( (systemCountry == null && preferredCountry == null) ||
                         (systemCountry != null && systemCountry.equals(preferredCountry)) ) {

                        // Ooh, we have a two way match, can we make three?
                        twoWayMatch = (twoWayMatch == null ? systemLocale : twoWayMatch);
                        String systemVariant = systemLocale.getVariant();
                        String preferredVariant = preferredLocale.getVariant();

                        if ( (systemVariant == null && preferredVariant == null) ||
                                (systemVariant != null && systemVariant.equals(preferredVariant)) ) {
                            // Bingo!  You sunk my battleship!
                            return systemLocale;
                        }
                    }
                }
*/
            }
        }

        // We didn't get a match complete match, maybe partial will do
        if (twoWayMatch != null) {
            return twoWayMatch;
        }
        else if (oneWayMatch != null) {
            return oneWayMatch;
        }
        else {
        	//TODO insertar un atributo en la request para que el actioncontext pueda saber que el locale se seleccionó por defecto
            return systemLanguages.get(0).toLocale();
        }
    }

	/**
	 * Siempre se usará UTF-8
	 */
	public String pickCharacterEncoding(HttpServletRequest request, Locale locale) {
		return "utf-8";
	}

}