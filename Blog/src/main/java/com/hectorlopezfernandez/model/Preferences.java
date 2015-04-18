package com.hectorlopezfernandez.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="preferences")
public class Preferences extends PersistentObject {
	
	public static final Long ID = Long.valueOf(1);

	@Basic(optional=false)
	@Column(name="title",length=50,nullable=false)
	private String title;

	@Basic
	@Column(name="tagline",length=150)
	private String tagline;

	@Basic
	@Column(name="paginate_index_page")
	private Boolean paginateIndexPage;

	@Basic
	@Column(name="posts_per_index_page")
	private Integer postsPerIndexPage;

	@Basic
	@Column(name="archive_items_per_index_page")
	private Integer archiveEntriesPerIndexPage;

	@Basic
	@Column(name="popular_tags_per_index_page")
	private Integer popularTagsPerIndexPage;

	@Basic
	@Column(name="feeds_max_post_age_in_days")
	private Integer maxPostAgeInDaysForFeeds;


	// getters & setters
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public Boolean getPaginateIndexPage() {
		return paginateIndexPage;
	}
	public void setPaginateIndexPage(Boolean paginateIndexPage) {
		this.paginateIndexPage = paginateIndexPage;
	}

	public Integer getPostsPerIndexPage() {
		return postsPerIndexPage;
	}
	public void setPostsPerIndexPage(Integer postsPerIndexPage) {
		this.postsPerIndexPage = postsPerIndexPage;
	}

	public Integer getMaxPostAgeInDaysForFeeds() {
		return maxPostAgeInDaysForFeeds;
	}
	public void setMaxPostAgeInDaysForFeeds(Integer maxPostAgeInDaysForFeeds) {
		this.maxPostAgeInDaysForFeeds = maxPostAgeInDaysForFeeds;
	}

}