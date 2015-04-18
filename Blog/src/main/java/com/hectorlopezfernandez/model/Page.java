package com.hectorlopezfernandez.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

/**
 * IMPORTANTE: JPA accede directamente a todos los campos excepto a las fechas, que se establecen por setter para rellenar los DateTime
 */

@Entity
@Access(AccessType.FIELD)
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
	@Column(name="content",length=4000)
	private String content;

	// la configuracion de jpa va en el get
	private long publicationDateAsLong;
	@Transient
	private DateTime publicationDate;

	// la configuracion de jpa va en el get
	private long lastModificationDateAsLong;
	@Transient
	private DateTime lastModificationDate;


	// getters & setters sint�ticos

	public void setPublicationDateAsLong(long publicationDateAsLong) {
		this.publicationDateAsLong = publicationDateAsLong;
		this.publicationDate = new DateTime(publicationDateAsLong);
	}
	public void setPublicationDate(DateTime publicationDate) {
		if (publicationDate == null) setPublicationDateAsLong(0);
		else setPublicationDateAsLong(publicationDate.getMillis());
	}
	
	public void setLastModificationDateAsLong(long lastModificationDateAsLong) {
		this.lastModificationDateAsLong = lastModificationDateAsLong;
		this.lastModificationDate = new DateTime(lastModificationDateAsLong);
	}
	public void setLastModificationDate(DateTime lastModificationDate) {
		if (lastModificationDate == null) setLastModificationDateAsLong(0);
		else setLastModificationDateAsLong(lastModificationDate.getMillis());
	}
	
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
		return publicationDate;
	}

	public DateTime getLastModificationDate() {
		return lastModificationDate;
	}

	@Basic(optional=false)
	@Access(AccessType.PROPERTY)
	@Column(name="publication_date")
	public long getPublicationDateAsLong() {
		return publicationDateAsLong;
	}

	@Basic(optional=false)
	@Access(AccessType.PROPERTY)
	@Column(name="last_modification_date")
	public long getLastModificationDateAsLong() {
		return lastModificationDateAsLong;
	}

	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

}