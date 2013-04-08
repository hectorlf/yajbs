package com.hectorlopezfernandez.integration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.action.Error500Action;

public class StripesExceptionHandler extends DefaultExceptionHandler {

	private final static Logger logger = LoggerFactory.getLogger(StripesExceptionHandler.class);

	public Resolution handleGeneric(Exception e, HttpServletRequest request, HttpServletResponse response) {
		// general exception handling
		logger.error("Ha ocurrido una excepción no controlada, se muestra la pagina de error 500: {} - {}", e.getClass().getName(), e.getMessage());
		e.printStackTrace();
		// se marca la transacción para rollback
		EntityManager em = PersistenceThreadLocalHelper.get();
		if (em != null) {
			EntityTransaction et = em.getTransaction();
			if (et != null && et.isActive()) et.setRollbackOnly();
		}
		// se muestra el error 500
		return new ForwardResolution(Error500Action.class);
	}

}