package com.hectorlopezfernandez.integration;

import java.io.File;
import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hectorlopezfernandez.utils.Constants;

public class AppInitializerContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(AppInitializerContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.debug("Creando contexto de la aplicacion");
		ServletContext sc = sce.getServletContext();
		// se abre el indice de lucene
		// si no se pudiera abrir por algun motivo, se crea un RAMDirectory para que no falle
		Directory dir = null;
		try {
			File ld = new File(Constants.LUCENE_DIRECTORY_FILE_PATH);
			if (!ld.exists()) if (!ld.mkdirs()) throw new IOException("No se ha podido crear la ruta de directorios para el indice de lucene: " + Constants.LUCENE_DIRECTORY_FILE_PATH);
			dir = FSDirectory.open(ld.toPath());
		} catch(IOException ioe) {
			logger.error("Ha ocurrido una IOException creando el indice de lucene: {}", ioe.getMessage());
			dir = new RAMDirectory();
		}
		sc.setAttribute(Constants.LUCENE_DIRECTORY_CONTEXT_ATTRIBUTE_NAME, dir);
		// se crea el inyector de Guice usado para construir los action e inyectar dependencias y se aniade al contexto
		Injector i = Guice.createInjector(new GuiceBindingModule(new GuiceEntityManagerProvider(), dir));
		sc.setAttribute(Constants.ROOT_GUICE_INJECTOR_CONTEXT_ATTRIBUTE_NAME, i);
		// se crea el entitymanager de jpa y se aniade al contexto
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("blogDataPersistence");
		sc.setAttribute(Constants.JPA_ENTITY_MANAGER_FACTORY_CONTEXT_ATTRIBUTE_NAME, emf);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.debug("Destruyendo contexto de la aplicacion");
		ServletContext sc = sce.getServletContext();
		// se elimina el inyector de guice del contexto - modo paranoico
		sc.removeAttribute(Constants.ROOT_GUICE_INJECTOR_CONTEXT_ATTRIBUTE_NAME);
		// se elimina el entitymanager de jpa del contexto y se hace limpieza
		EntityManagerFactory emf = (EntityManagerFactory)sc.getAttribute(Constants.JPA_ENTITY_MANAGER_FACTORY_CONTEXT_ATTRIBUTE_NAME);
		if (emf != null) emf.close();
		sc.removeAttribute(Constants.JPA_ENTITY_MANAGER_FACTORY_CONTEXT_ATTRIBUTE_NAME);
		// se elimina el lucenedirectory
		Directory dir = (Directory)sc.getAttribute(Constants.LUCENE_DIRECTORY_CONTEXT_ATTRIBUTE_NAME);
		if (dir != null) try { dir.close(); } catch(Exception e) { /* NO SIRVE DE NADA CAPTURAR AQUI */ }
		sc.removeAttribute(Constants.LUCENE_DIRECTORY_CONTEXT_ATTRIBUTE_NAME);
	}

}