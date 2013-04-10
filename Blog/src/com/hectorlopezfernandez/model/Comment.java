package com.hectorlopezfernandez.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

@Entity
@Access(AccessType.FIELD)
@Table(name="comments")
public class Comment extends PersistentObject {

	@Basic(optional=false)
	@Column(name="text",length=250)
	private String content;

	@Basic(optional=false)
	@Column(name="visible")
	private boolean visible;
	
	// la configuracion de jpa va en el get
	private long publicationDateAsLong;
	@Transient
	private DateTime publicationDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id",unique=true,nullable=false)
	private Post post;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="author_id",unique=true,nullable=false)
	private Author author;

	// getters y setters sinteticos
	
	public void setPublicationDateAsLong(long publicationDateAsLong) {
		this.publicationDateAsLong = publicationDateAsLong;
		this.publicationDate = new DateTime(publicationDateAsLong);
	}
	public void setPublicationDate(DateTime publicationDate) {
		if (publicationDate == null) setPublicationDateAsLong(0);
		else setPublicationDateAsLong(publicationDate.getMillis());
	}

	// getters & setters
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}

	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}

	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Basic(optional=false)
	@Access(AccessType.PROPERTY)
	@Column(name="publication_date")
	public long getPublicationDateAsLong() {
		return publicationDateAsLong;
	}

	public DateTime getPublicationDate() {
		return publicationDate;
	}

}