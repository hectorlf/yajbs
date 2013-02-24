package com.hectorlopezfernandez.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name="posts")
public class Post extends PersistentObject {

//	@Basic(optional=false)
//	@Column(name="style")
//	private PostStyle style;

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

	@Basic(optional=false)
	@Column(name="publication_date")
	private long publicationDateAsLong;
	
	@Basic(optional=false)
	@Column(name="year")
	private int year;

	@Basic(optional=false)
	@Column(name="month")
	private int month;

	@Basic(optional=false)
	@Column(name="day")
	private int day;
	
	@Basic(optional=false)
	@Column(name="last_modification_date")
	private long lastModificationDateAsLong;

	@Basic(optional=true)
	@Column(name="header_image_url",length=100)
	private String headerImageUrl;

	@Basic(optional=true)
	@Column(name="side_image_url",length=100)
	private String sideImageUrl;
	
	@Basic(optional=false)
	@Column(name="comments_closed")
	private boolean commentsClosed;

	@OneToMany(mappedBy="post",orphanRemoval=true)
	private List<Comment> comments;

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
	
	public DateTime getPublicationDate() {
		DateTime pd = new DateTime(publicationDateAsLong);
		return pd;
	}
	public void setPublicationDate(DateTime publicationDate) {
		if (publicationDate == null) {
			this.publicationDateAsLong = 0;
			this.year = 0;
			this.month = 1;
			this.day = 1;
		} else {
			this.publicationDateAsLong = publicationDate.getMillis();
			this.year = publicationDate.getYear();
			this.month = publicationDate.getMonthOfYear();
			this.day = publicationDate.getDayOfMonth();
		}
	}
	
	public DateTime getLastModificationDate() {
		DateTime pd = new DateTime(lastModificationDateAsLong);
		return pd;
	}
	public void setLastModificationDate(DateTime lastModificationDate) {
		if (lastModificationDate == null) this.lastModificationDateAsLong = 0;
		else this.lastModificationDateAsLong = lastModificationDate.getMillis();
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

	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public long getPublicationDateAsLong() {
		return publicationDateAsLong;
	}

	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	
	public long getLastModificationDateAsLong() {
		return lastModificationDateAsLong;
	}

	public String getHeaderImageUrl() {
		return headerImageUrl;
	}
	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}

	public String getSideImageUrl() {
		return sideImageUrl;
	}
	public void setSideImageUrl(String sideImageUrl) {
		this.sideImageUrl = sideImageUrl;
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

}