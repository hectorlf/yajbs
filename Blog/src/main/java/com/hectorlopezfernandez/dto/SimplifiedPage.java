package com.hectorlopezfernandez.dto;

import org.joda.time.DateTime;

/**
 * Almacena la información básica de una pagina
 */
public final class SimplifiedPage {

	private Long id;
	private String titleUrl;
	private DateTime lastModificationDate;


	// constructores

	public SimplifiedPage(Long id, String titleUrl, DateTime lastModificationDate) {
		this.id = id;
		this.titleUrl = titleUrl;
		this.lastModificationDate = lastModificationDate;
	}

	// getters
	
	public String getTitleUrl() {
		return titleUrl;
	}

	public Long getId() {
		return id;
	}

	public DateTime getLastModificationDate() {
		return lastModificationDate;
	}

}