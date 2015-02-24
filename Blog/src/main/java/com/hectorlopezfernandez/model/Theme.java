package com.hectorlopezfernandez.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="themes")
public class Theme extends PersistentObject {

	@Basic(optional=false)
	@Column(name="name",length=50)
	private String name;
	
	// getters & setters especiales
	
	public String getHeaderJsp() {
		if (getId() == null) return "";
		String path = "themes/" + getId().toString() + "/header.jsp";
		return path;
	}
	public String getIndexBodyJsp() {
		if (getId() == null) return "";
		String path = "themes/" + getId().toString() + "/indexBody.jsp";
		return path;
	}
	public String getPostBodyJsp() {
		if (getId() == null) return "";
		String path = "themes/" + getId().toString() + "/postBody.jsp";
		return path;
	}
	public String getPageBodyJsp() {
		if (getId() == null) return "";
		String path = "themes/" + getId().toString() + "/pageBody.jsp";
		return path;
	}
	public String getFooterJsp() {
		if (getId() == null) return "";
		String path = "themes/" + getId().toString() + "/footer.jsp";
		return path;
	}

	// getters & setters
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}