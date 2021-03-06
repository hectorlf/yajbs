package com.hectorlopezfernandez.integration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.ActionBeanNotFoundException;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;

public class StripesExceptionHandler extends DefaultExceptionHandler {

	private final static Logger logger = LoggerFactory.getLogger(StripesExceptionHandler.class);

	public Resolution handleActionBeanNotFound(ActionBeanNotFoundException abnfe, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("No se ha encontrado un ActionBean para la url, se muestra la pagina de error 404: {}", abnfe.getMessage());
		// se marca la transaccion para rollback
		EntityManager em = PersistenceThreadLocalHelper.get();
		if (em != null) {
			EntityTransaction et = em.getTransaction();
			if (et != null && et.isActive()) et.setRollbackOnly();
		}
		// se muestra el error 404
		return new ErrorResolution(404);
	}

	public Resolution handleGeneric(Exception e, HttpServletRequest request, HttpServletResponse response) {
		// general exception handling
		logger.error("Ha ocurrido una excepcion no controlada, se muestra la pagina de error 500: {} - {}", e.getClass().getName(), e.getMessage());
		e.printStackTrace();
		// se marca la transaccion para rollback
		EntityManager em = PersistenceThreadLocalHelper.get();
		if (em != null) {
			EntityTransaction et = em.getTransaction();
			if (et != null && et.isActive()) et.setRollbackOnly();
		}
		// se muestra el error 500
		return new ErrorResolution(500);
	}

}