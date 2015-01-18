package com.hectorlopezfernandez.dto;

import org.joda.time.DateTime;

/**
 * Almacena la información básica de un post
 */
public final class SimplifiedPost {

	private Long id;
	private String title;
	private String titleUrl;
	private String excerpt;
	private String content;
	private DateTime publicationDate;
	private DateTime lastModificationDate;
	private String authorName;


	// constructores

	public SimplifiedPost(Long id, String title, String titleUrl, String excerpt, String content, DateTime publicationDate, String authorName) {
		this.id = id;
		this.title = title;
		this.titleUrl = titleUrl;
		this.excerpt = excerpt;
		this.content = content;
		this.publicationDate = publicationDate;
		this.authorName = authorName;
	}

	public SimplifiedPost(Long id, String title, String titleUrl, String excerpt, String content, DateTime publicationDate, DateTime lastModificationDate, String authorName) {
		this(id, title, titleUrl, excerpt, content, publicationDate, authorName);
		this.lastModificationDate = lastModificationDate;
	}

	// getters sintéticos
	
	public int getYear() {
		return publicationDate.getYear();
	}
	public int getMonth() {
		return publicationDate.getMonthOfYear();
	}

	// getters
	
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

	public DateTime getLastModificationDate() {
		return lastModificationDate;
	}

	public Long getId() {
		return id;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public String getAuthorName() {
		return authorName;
	}

}