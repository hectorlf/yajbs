package com.hectorlopezfernandez.integration;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.utils.Constants;

// Filtro que se encarga de abrir transacciones, hacer commit y rollback según el
// caso. Equivale al patrón opensessioninview, a falta de implementar cosas más
// sofisticadas mediante aspectos de Guice.
public class TransactionManagerFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(TransactionManagerFilter.class);

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Entrando en TransactionManagerFilter.doFilter");
		// se comprueba si existe la marca de este filtro asociado a la request, lo cual indicaria que ya se ha entrado antes
		Object filterToken = request.getAttribute(Constants.JPA_TRANSACTION_MANAGER_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME);
		if (filterToken == null) doFirstTimeFilter(request, response, filterChain);
		else doRecurringFilter(request, response, filterChain);
		logger.debug("Saliendo de TransactionManagerFilter.doFilter");
	}

	private void doFirstTimeFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("No se ha detectado el token del TransactionManagerFilter para esta request. Suponemos que es la primera vez que se pasa por el filtro y controlamos la transacción.");
		// se recupera el EntityManager para esta request y se inicia la transaccion
		EntityManager em = PersistenceThreadLocalHelper.get();
		if (em == null) throw new RuntimeException("No se ha encontrado un EntityManager para esta request. Esto indica un fallo de configuracion y debe revisarse el log de arranque y el AppInitializerContextListener.");
		if (em.getTransaction() != null && em.getTransaction().isActive()) throw new RuntimeException("Ya existe una transacción activa para esta request. Esto debería significar un error, ya que supuestamente estamos inicializando la transacción...");
		EntityTransaction et = em.getTransaction();
		logger.debug("Comenzando la transacción");
		et.begin();
		// se marca la request con el token del filtro
		request.setAttribute(Constants.JPA_TRANSACTION_MANAGER_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME, Constants.JPA_TRANSACTION_MANAGER_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME);
		// una vez inicializado, se continua con la cadena de filtros
		// IMPORTANTE: este filtro no captura excepciones, ya que DEBERÍAN estar capturadas en la capa del dispatcher de Stripes. Una excepción a este nivel indicaría un fallo de configuración.
		try {
			filterChain.doFilter(request, response);
		} finally {
			// la transacción debería estar activa, ya que no se debe hacer commit o rollback en ningún otro punto de la aplicación
			if (!et.isActive()) throw new RuntimeException("La transacción correspondiente a esta request no está activa. Esto indica que se ha hecho un mal uso de la gestión de transacciones.");
			// si la transacción está marcada para rollback, se hace rollback
			if (et.getRollbackOnly()) {
				logger.debug("Realizando rollback() de la transacción");
				et.rollback();
			} else {
				// si la transacción no está marcada para rollback, se intenta hacer commit
				try {
					logger.debug("Realizando commit() de la transacción");
					et.commit();
				} catch(RollbackException re) {
					// si ocurre una excepción al hacer commit, debemos hacer rollback de los cambios y registrar el error
					logger.error("Ha ocurrido una excepción al hacer commit de la transacción. Se procede a hacer rollback. Mensaje de la excepción: ", re.getClass().getName(), re.getMessage());
					et.rollback();
				}
			}
		}
		// para terminar la peticion, se elimina la marca del filtro de la request
		request.removeAttribute(Constants.JPA_TRANSACTION_MANAGER_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME);
	}

	private void doRecurringFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.debug("Se ha detectado la marca del TransactionManagerFilter para esta request. El filtro ya ha sido inicializado.");
		filterChain.doFilter(request, response);
	}

}