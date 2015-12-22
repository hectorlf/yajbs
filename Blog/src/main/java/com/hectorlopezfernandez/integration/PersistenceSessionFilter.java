package com.hectorlopezfernandez.integration;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.utils.Constants;

public class PersistenceSessionFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(PersistenceSessionFilter.class);

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Entrando en PersistenceSessionFilter.doFilter");
		// se comprueba si existe un EntityManager asociado a la request, lo cual indicaria que ya se ha entrado antes al filtro
		//IMPORTANTE: en el fondo, este atributo de request solo sirve para demarcar, ya que el EntityManager se guarda y recupera de un ThreadLocal
		Object em = request.getAttribute(Constants.JPA_ENTITY_MANAGER_REQUEST_ATTRIBUTE_NAME);
		if (em == null) doFirstTimeFilter(request, response, filterChain);
		else doRecurringFilter(request, response, filterChain);
		logger.debug("Saliendo de PersistenceSessionFilter.doFilter");
	}

	private void doFirstTimeFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("No se ha detectado un EntityManager para esta request. Suponemos que es la primera vez que se pasa por el filtro, e inicializamos Guice y JPA.");
		// se crea el EntityManager para esta peticion y se guarda asociado a la request
		EntityManagerFactory emf = (EntityManagerFactory)request.getServletContext().getAttribute(Constants.JPA_ENTITY_MANAGER_FACTORY_CONTEXT_ATTRIBUTE_NAME);
		if (emf == null) throw new RuntimeException("No se ha encontrado el EntityManagerFactory en el contexto de servlet o no esta abierto. Esto indica un fallo de configuracion y debe revisarse el log de arranque y el AppInitializerContextListener.");
		EntityManager em = emf.createEntityManager();
		PersistenceThreadLocalHelper.set(em);
		//IMPORTANTE: en el fondo, este atributo de request solo sirve para demarcar, ya que el EntityManager se guarda y recupera de un ThreadLocal
		request.setAttribute(Constants.JPA_ENTITY_MANAGER_REQUEST_ATTRIBUTE_NAME, em);
		// una vez inicializado, se continua con la cadena de filtros
		try {
			filterChain.doFilter(request, response);
		} catch(Exception e) {
			logger.error("Se ha capturado una excepcion al nivel mas alto que soporta este filtro. Esto podria deberse a un error de configuracion. Excepcion anidada: {} - {}", e.getClass().getName(), e.getMessage());
			logger.error("Se procede a realizar limpieza en la transaccion, si no estuviera hecha ya.");
		} finally {
			// se hace un control paranoico de las transacciones
			// en este punto de la cadena de filtros NO deberia haber una transaccion activa, si la hay se considera un error y se hace rollback
			if (em != null && em.getTransaction() != null && em.getTransaction().isActive()) {
				logger.error("Se ha encontrado una transaccion activa al final de una request sin que se haya controlado en las capas adecuadas. Se hace rollback de la transaccion por seguridad.");
				try {
					em.getTransaction().rollback();
				} catch (Exception e) {
					logger.error("Ha ocurrido una excepcion haciendo rollback de la transaccion conflictiva. Mensaje anidado: {} - {}", e.getClass().getName(), e.getMessage());
				}
			}
		}
		// para terminar la peticion, se cierra el EntityManager y se desasocia de la request
		PersistenceThreadLocalHelper.cleanUp();
		request.removeAttribute(Constants.JPA_ENTITY_MANAGER_REQUEST_ATTRIBUTE_NAME);
		if (em != null) em.close();
	}

	private void doRecurringFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Se ha detectado un EntityManager para esta request. El filtro ya ha sido inicializado.");
		filterChain.doFilter(request, response);
	}

}