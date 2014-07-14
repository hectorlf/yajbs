package com.hectorlopezfernandez.utils;

public final class Constants {

	// constantes de arquitectura
	public static final String ROOT_GUICE_INJECTOR_CONTEXT_ATTRIBUTE_NAME = "root_guice_injector";
//	public static final String GUICE_INJECTOR_REQUEST_ATTRIBUTE_NAME = "guice_injector";
	public static final String JPA_ENTITY_MANAGER_FACTORY_CONTEXT_ATTRIBUTE_NAME = "servlet_context_jpa_entity_manager_factory";
	public static final String JPA_ENTITY_MANAGER_REQUEST_ATTRIBUTE_NAME = "request_context_jpa_entity_manager";
	public static final String JPA_TRANSACTION_MANAGER_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME = "request_context_jpa_transaction_manager_filter";
	public static final String LUCENE_DIRECTORY_CONTEXT_ATTRIBUTE_NAME = "servlet_context_lucene_directory";
	public static final String LUCENE_DIRECTORY_FILE_PATH = "/usr/share/tomcat7/caches/lucene";
	public static final String ALIAS_REQUEST_ATTRIBUTE_NAME = "request_context_current_host";
	public static final String USER_SESSION_FILTER_TOKEN_REQUEST_ATTRIBUTE_NAME = "request_context_user_session_filter_toker";
	public static final String LOGGED_USER_REQUEST_ATTRIBUTE_NAME = "request_context_logged_user";
	public static final String STRIPES_FLASH_SCOPE_MARKER_REQUEST_ATTRIBUTE_NAME = "request_context_flash_scope_in_use";
	public static final String LOCALE_COOKIE_NAME = "user_locale";
	public static final String DEFAULT_LOCALE_SELECTED = "request_context_default_locale_selected";
	
	// constantes sobre la aplicacion
	public static final String APPLICATION_TAG = "YAJBS 0.5.1";

	// objetos de base de datos prefijados
	public static final Long ADMIN_USER_ID = Long.valueOf(1);
	public static final Long VISITOR_USER_ID = Long.valueOf(2);

	
	// no instanciable
	private Constants() {};

}