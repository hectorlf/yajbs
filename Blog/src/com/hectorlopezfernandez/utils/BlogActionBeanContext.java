package com.hectorlopezfernandez.utils;

import net.sourceforge.stripes.action.ActionBeanContext;

import com.hectorlopezfernandez.model.Alias;
import com.hectorlopezfernandez.model.User;

public class BlogActionBeanContext extends ActionBeanContext {

	// recupera el objeto Alias configurado para esta peticion (siempre debe existir)
	public Alias getAlias() {
		Alias a = (Alias)getRequest().getAttribute(Constants.ALIAS_REQUEST_ATTRIBUTE_NAME);
		return a;
	}

	// recupera el usuario logueado para la peticion (en caso de que no se haya autenticado,
	// se rellena con el usuario anónimo)
	public User getLoggedUser() {
		User u = (User)getRequest().getAttribute(Constants.LOGGED_USER_REQUEST_ATTRIBUTE_NAME);
		return u;
	}

	// atributos de request
	public void setAttribute(String key, Object value) {
		getRequest().setAttribute(key, value);
	}
	public Object getAttribute(String key) {
		return getRequest().getAttribute(key);
	}
	
	// atributos de sesion
	public void setSessionAttribute(String key, Object value) {
		getRequest().getSession().setAttribute(key, value);
	}
	public Object getSessionAttribute(String key) {
		return getRequest().getSession().getAttribute(key);
	}

}