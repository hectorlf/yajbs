package com.hectorlopezfernandez.integration;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PersistenceThreadLocalHelper {

	final private static Logger logger = LoggerFactory.getLogger(PersistenceThreadLocalHelper.class);

	final private static ThreadLocal<EntityManager> tl = new ThreadLocal<EntityManager>();
	
	// no instanciable
	private PersistenceThreadLocalHelper() {};

	public static EntityManager get() {
		EntityManager em = tl.get();
		logger.debug("Recuperado el EntityManager {} de la variable ThreadLocal", em);
		return em;
	}
	
	public static void set(EntityManager em) {
		logger.debug("Guardando el EntityManager {} en la variable ThreadLocal", em);
		tl.set(em);
	}
	
	public static void cleanUp() {
		logger.debug("Borrando el contenido de la variable ThreadLocal");
		tl.remove();
	}

}
