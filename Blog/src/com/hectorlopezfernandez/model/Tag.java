package com.hectorlopezfernandez.model;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="tags")
public class Tag extends PersistentObject {

	@Basic(optional=false)
	@Column(name="name",length=100)
	private String name;
	
	@Basic(optional=false)
	@Column(name="url_handler",length=50)
	private String nameUrl;

	@Basic(optional=false)
	@Column(name="ref_count")
	private int count;

	@ManyToMany(mappedBy="tags",fetch=FetchType.LAZY)
	private Collection<Post> posts;


	// utility methods
	
	public float computeRelativeFrecuency(int min, int max) {
		if (count < min) return 0;
		return (count - min) / (max - min);
	}
	
	// getters & setters
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getNameUrl() {
		return nameUrl;
	}
	public void setNameUrl(String nameUrl) {
		this.nameUrl = nameUrl;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}