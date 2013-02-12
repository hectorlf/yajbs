package com.hectorlopezfernandez.dto;

import net.sourceforge.stripes.action.ActionBean;

/**
 * Almacena la información necesaria para mostrar el breadcrum de administración
 */
public final class Breadcrumb {

	private String pageName;
	private Class<ActionBean> action;
	private Breadcrumb child;


	// constructores

	public Breadcrumb(String pageName, Class<ActionBean> action) {
		if (pageName == null || pageName.length() == 0) throw new IllegalArgumentException("El parametro pageName no puede ser nulo ni vacio.");
		if (action == null) throw new IllegalArgumentException("El parametro action no puede ser nulo.");
		this.pageName = pageName;
		this.action = action;
	}

	// setters sinteticos
	
	public void defineChild(Breadcrumb child) {
		this.child = child;
	}

	// getters
	
	public String getPageName() {
		return pageName;
	}

	public Class<ActionBean> getAction() {
		return action;
	}

	public Breadcrumb getChild() {
		return child;
	}

}