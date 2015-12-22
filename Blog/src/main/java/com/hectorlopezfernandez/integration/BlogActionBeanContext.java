package com.hectorlopezfernandez.integration;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.FlashScope;

import com.hectorlopezfernandez.model.User;
import com.hectorlopezfernandez.utils.Constants;

public class BlogActionBeanContext extends ActionBeanContext {

	// recupera el usuario logueado para la peticion (en caso de que no se haya autenticado,
	// se rellena con el usuario anonimo)
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
	
	// atributos del flash scope (esto permitira que la sesion dure una request mas)
	public void setFlashAttribute(String key, Object value) {
		FlashScope.getCurrent(getRequest(), true).put(key, value);
		getRequest().setAttribute(Constants.STRIPES_FLASH_SCOPE_MARKER_REQUEST_ATTRIBUTE_NAME, Boolean.TRUE);
	}
	
	// atributos de sesion
	public void setSessionAttribute(String key, Object value) {
		getRequest().getSession().setAttribute(key, value);
	}
	public Object getSessionAttribute(String key) {
		return getRequest().getSession().getAttribute(key);
	}

}