package com.hectorlopezfernandez.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
//@PrimaryKeyJoinColumn
public class Author extends User {

	@Basic(optional=false)
	@Column(name="name",length=50)
	private String displayName;

	@Basic(optional=true)
	@Column(name="about",length=250)
	private String about;

	@Basic(optional=true)
	@Column(name="relatedUrl",length=100)
	private String relatedUrl;

	@OneToMany(mappedBy="author")
	private List<Post> posts;

	@OneToMany(mappedBy="author")
	private List<Comment> comments;

	@ManyToMany
	@JoinTable(name="authors_hosts",joinColumns=@JoinColumn(name="author_id", referencedColumnName="id"),inverseJoinColumns=@JoinColumn(name="host_id", referencedColumnName="id"))
	private Collection<Host> hosts;


	// getters & setters
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}

	public String getRelatedUrl() {
		return relatedUrl;
	}
	public void setRelatedUrl(String relatedUrl) {
		this.relatedUrl = relatedUrl;
	}

}