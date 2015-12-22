package com.hectorlopezfernandez.integration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.hectorlopezfernandez.model.User;
import com.hectorlopezfernandez.service.UserService;
import com.hectorlopezfernandez.utils.Constants;

public class UserSessionFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(UserSessionFilter.class);

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Entrando en UserSessionFilter.doFilter");
		// se comprueba si ya se ha entrado a este filtro en esta request
		Object token = request.getAttribute(Constants.USER_SESSION_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME);
		if (token == null) doFirstTimeFilter(request, response, filterChain);
		else doRecurringFilter(request, response, filterChain);
		logger.debug("Saliendo de UserSessionFilter.doFilter");
	}

	private void doFirstTimeFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("No se ha detectado el token del filtro para esta request. Suponemos que es la primera vez que se pasa por el filtro.");
		// se marca la request con el token
		request.setAttribute(Constants.USER_SESSION_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME, Constants.USER_SESSION_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME);
		// se comprueba si hay un usuario logado en la aplicacion
		Subject currentSubject = SecurityUtils.getSubject();
		if (currentSubject.isAuthenticated()) {
			// se obtiene el UserService y se recoge la informacion del usuario logado
			Injector i = (Injector)request.getServletContext().getAttribute(Constants.ROOT_GUICE_INJECTOR_CONTEXT_ATTRIBUTE_NAME);
			if (i == null) throw new RuntimeException("No se ha encontrado el inyector de Guice en el contexto de servlet. Esto indica un fallo de configuracion y debe revisarse el log de arranque y el AppInitializerContextListener.");
			Long userId = (Long)currentSubject.getPrincipal();
			logger.debug("Recuperando usuario logado con id: {}", userId);
			UserService us = i.getInstance(UserService.class);
			User u = us.getAuthorById(userId);
			logger.debug("Usuario {} autenticado en el sistema", u.getUsername());
			// se guarda el usuario en la request para que los action puedan recogerlo
			request.setAttribute(Constants.LOGGED_USER_REQUEST_ATTRIBUTE_NAME, u);
		} else {
			// ya que no hay usuario logado, intentamos eliminar las cookies de sesion (para la normativa de mierda esa)
			// esto no deberia afectar al comportamiento del flash scope de stripes
			eraseJsessionIdCookie((HttpServletRequest)request, (HttpServletResponse)response);
		}
		// una vez gestionado el usuario, se continua con la cadena de filtros
		filterChain.doFilter(request, response);
		// una vez temina la peticion, se debe hacer limpieza
		if (currentSubject.isAuthenticated() || request.getAttribute(Constants.STRIPES_FLASH_SCOPE_MARKER_REQUEST_ATTRIBUTE_NAME) != null) {
			// si existe un usuario logado o se ha utilizado un flash scope, solo se elimina el objeto User de la request
			request.removeAttribute(Constants.LOGGED_USER_REQUEST_ATTRIBUTE_NAME);
		} else {
			// si no habia usuario logado ni flash scope, es necesario cerrar la sesion para evitar gastar memoria (al no haber cookie de sesion, cada peticion generaria una)
			currentSubject.logout();
			
			//FIXME
			// intento absolutamente desesperado y paranoico para no dejar la cookie de sesion
			if (!response.isCommitted()) eraseJsessionIdCookie((HttpServletRequest)request, (HttpServletResponse)response);
		}
		request.removeAttribute(Constants.USER_SESSION_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME);
	}

	private void doRecurringFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Se ha detectado el token del filtro para esta request. El filtro ya ha sido inicializado.");
		filterChain.doFilter(request, response);
	}

	
	private void eraseJsessionIdCookie(HttpServletRequest req, HttpServletResponse resp) {
		Cookie c = new Cookie("JSESSIONID", "");
		c.setPath(req.getContextPath());
		c.setMaxAge(0);
		resp.addCookie(c);
	}

}