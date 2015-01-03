package com.hectorlopezfernandez.model;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

/**
 * IMPORTANTE: JPA accede directamente a todos los campos excepto a las fechas, que se establecen por setter para rellenar los DateTime
 */

@Entity
@Access(AccessType.FIELD)
@Table(name="posts")
public class Post extends PersistentObject {

	@Basic(optional=false)
	@Column(name="title",length=100)
	private String title;
	
	@Basic(optional=false)
	@Column(name="url_safe_title",length=50)
	private String titleUrl;

	@Basic(optional=true)
	@Column(name="meta_description",length=160)
	private String metaDescription;

	@Basic(optional=false)
	@Column(name="excerpt",length=500)
	private String excerpt;

	@Basic(optional=false)
	@Column(name="content",length=3000)
	private String content;

	// la configuracion de jpa va en el get
	private long creationDateAsLong;
	@Transient
	private DateTime creationDate;

	// la configuracion de jpa va en el get
	private long publicationDateAsLong;
	@Transient
	private DateTime publicationDate;

	// la configuracion de jpa va en el get
	private long lastModificationDateAsLong;
	@Transient
	private DateTime lastModificationDate;

	@Basic(optional=false)
	@Column(name="comments_closed")
	private boolean commentsClosed;

	@Basic(optional=false)
	@Column(name="published")
	private boolean published;

	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="host_id",nullable=false)
	private Host host;
	
	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="author_id",nullable=false)
	private Author author;

	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="archive_entry_id",nullable=false)
	private ArchiveEntry archiveEntry;

	@ManyToMany
	@JoinTable(name="posts_tags",joinColumns=@JoinColumn(name="post_id", referencedColumnName="id"),inverseJoinColumns=@JoinColumn(name="tag_id", referencedColumnName="id"))
	private Collection<Tag> tags;

	// getters y setters sinteticos

	public void setCreationDateAsLong(long creationDateAsLong) {
		this.creationDateAsLong = creationDateAsLong;
		this.creationDate = new DateTime(creationDateAsLong);
	}
	public void setCreationDate(DateTime creationDate) {
		if (creationDate == null) setCreationDateAsLong(0);
		else setCreationDateAsLong(creationDate.getMillis());
	}

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

	public int getYear() {
		return (publicationDate == null ? 0 : publicationDate.getYear());
	}
	public int getMonth() {
		return (publicationDate == null ? 1 : publicationDate.getMonthOfYear());
	}

	// getters & setters
	
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

	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}

	public DateTime getPublicationDate() {
		return publicationDate;
	}

	public DateTime getLastModificationDate() {
		return lastModificationDate;
	}

	@Basic(optional=false)
	@Access(AccessType.PROPERTY)
	@Column(name="creation_date")
	public long getCreationDateAsLong() {
		return creationDateAsLong;
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

	public boolean isCommentsClosed() {
		return commentsClosed;
	}
	public void setCommentsClosed(boolean commentsClosed) {
		this.commentsClosed = commentsClosed;
	}

	public ArchiveEntry getArchiveEntry() {
		return archiveEntry;
	}
	public void setArchiveEntry(ArchiveEntry archiveEntry) {
		this.archiveEntry = archiveEntry;
	}

	public Collection<Tag> getTags() {
		return tags;
	}
	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
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

	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}

}