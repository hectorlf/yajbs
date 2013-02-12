package com.hectorlopezfernandez.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name="pages")
public class Page extends PersistentObject {

	@Basic(optional=false)
	@Column(name="title",length=100)
	private String title;
	
	@Basic(optional=false)
	@Column(name="url_handler",length=50)
	private String titleUrl;

	@Basic(optional=true)
	@Column(name="meta_description",length=160)
	private String metaDescription;

	@Basic(optional=false)
	@Column(name="content",length=3000)
	private String content;

	@Basic(optional=false)
	@Column(name="publication_date")
	private long publicationDateAsLong;

	@Basic(optional=false)
	@Column(name="last_modification_date")
	private long lastModificationDateAsLong;

	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="host_id",nullable=false)
	private Host host;

	
	// getters & setters
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleUrl() {
		return titleUrl;
	}
	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public DateTime getPublicationDate() {
		DateTime pd = new DateTime(publicationDateAsLong);
		return pd;
	}
	public void setPublicationDate(DateTime publicationDate) {
		if (publicationDate == null) this.publicationDateAsLong = 0;
		else this.publicationDateAsLong = publicationDate.getMillis();
	}
	public long getPublicationDateAsLong() {
		return publicationDateAsLong;
	}

	public DateTime getLastModificationDate() {
		DateTime pd = new DateTime(lastModificationDateAsLong);
		return pd;
	}
	public void setLastModificationDate(DateTime lastModificationDate) {
		if (lastModificationDate == null) this.lastModificationDateAsLong = 0;
		else this.lastModificationDateAsLong = lastModificationDate.getMillis();
	}
	public long getLastModificationDateAsLong() {
		return lastModificationDateAsLong;
	}

	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}

}