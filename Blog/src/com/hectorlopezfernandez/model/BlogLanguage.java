package com.hectorlopezfernandez.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="languages")
public class BlogLanguage extends Language {

	@Basic(optional=false)
	@Column(name="name",length=25)
	private String name;

	@Basic(optional=true)
	@Column(name="flag_image_url",length=100)
	private String flagImageUrl;

	// getters & setters
	
	public String getFlagImageUrl() {
		return flagImageUrl;
	}
	public void setFlagImageUrl(String flagImageUrl) {
		this.flagImageUrl = flagImageUrl;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}