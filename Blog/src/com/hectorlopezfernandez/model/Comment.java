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
@Table(name="comments")
public class Comment extends PersistentObject {

	@Basic(optional=false)
	@Column(name="text",length=250)
	private String content;

	@Basic(optional=false)
	@Column(name="visible")
	private boolean visible;
	
	@Basic(optional=false)
	@Column(name="publication_date")
	private long publicationDateAsLong;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="post_id",unique=true,nullable=false)
	private Post post;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="author_id",unique=true,nullable=false)
	private Author author;

	// getters y setters sinteticos
	
	public DateTime getPublicationDate() {
		DateTime pd = new DateTime(publicationDateAsLong);
		return pd;
	}
	public void setPublicationDate(DateTime publicationDate) {
		if (publicationDate == null) return;
		this.publicationDateAsLong = publicationDate.getMillis();
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

}