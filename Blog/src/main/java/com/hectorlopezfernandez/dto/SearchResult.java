package com.hectorlopezfernandez.dto;

import org.joda.time.DateTime;

/**
 * Almacena la informacion necesaria para mostrar un resultado de busqueda
 */
public final class SearchResult {

	public static final String POST_TYPE = "Post";
	public static final String PAGE_TYPE = "Page";

	private String type;
	private String title;
	private String titleUrl;
	private String content;
	private DateTime publicationDate;


	// constructores

	public SearchResult(String type, String title, String titleUrl, String content, DateTime publicationDate) {
		this.type = type;
		this.title = title;
		this.titleUrl = titleUrl;
		this.content = content;
		this.publicationDate = publicationDate;
	}

	// getters sinteticos
	
	public int getYear() {
		return publicationDate.getYear();
	}
	public int getMonth() {
		return publicationDate.getMonthOfYear();
	}

	// getters
	
	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public String getContent() {
		return content;
	}

	public DateTime getPublicationDate() {
		return publicationDate;
	}

}