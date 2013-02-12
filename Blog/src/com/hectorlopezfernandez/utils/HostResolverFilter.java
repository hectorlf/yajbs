package com.hectorlopezfernandez.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.service.BlogService;

public class HostResolverFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(HostResolverFilter.class);

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Entrando en HostResolverFilter.doFilter");
		// se comprueba si existe un Host asociado a la request, lo cual indicaría que ya se ha entrado antes al filtro
		Object host = request.getAttribute(Constants.ALIAS_REQUEST_ATTRIBUTE_NAME);
		if (host == null) doFirstTimeFilter(request, response, filterChain);
		else doRecurringFilter(request, response, filterChain);
		logger.debug("Saliendo de HostResolverFilter.doFilter");
	}

	private void doFirstTimeFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("No se ha detectado un Host para esta request. Suponemos que es la primera vez que se pasa por el filtro.");
		// se obtiene el BlogService y se intenta recuperar el Alias del Host que debe atender a esta peticion
		Injector i = (Injector)request.getServletContext().getAttribute(Constants.ROOT_GUICE_INJECTOR_CONTEXT_ATTRIBUTE_NAME);
		if (i == null) throw new RuntimeException("No se ha encontrado el inyector de Guice en el contexto de servlet. Esto indica un fallo de configuracion y debe revisarse el log de arranque y el AppInitializerContextListener.");
		BlogService bs = i.getInstance(BlogService.class);
		// se usa el nombre completo del servidor que figura en la cabecera http para identificar el host
		logger.debug("Nombre del servidor: {}", request.getServerName());
		String hostname = request.getServerName();
		Long hostId = bs.getAliasIdByName(hostname);
		if (hostId == null) {
			logger.info("Se ha intentado acceder al blog en el Host {}, pero ese nombre de host no existe en la tabla de aliases.", hostname);
			((HttpServletResponse)response).sendError(404);
			return;
		}
		logger.debug("Seleccionado host {} como responsable de la peticion.",hostname);
		Alias a = bs.getAlias(hostId);
		request.setAttribute(Constants.ALIAS_REQUEST_ATTRIBUTE_NAME, a);
		// una vez obtenido el host, se continua con la cadena de filtros
		filterChain.doFilter(request, response);
		// para terminar la peticion, se realiza limpieza
		request.removeAttribute(Constants.ALIAS_REQUEST_ATTRIBUTE_NAME);
	}

	private void doRecurringFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Se ha detectado un Host para esta request. El filtro ya ha sido inicializado.");
		filterChain.doFilter(request, response);
	}

}