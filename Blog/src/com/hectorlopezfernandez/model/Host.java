package com.hectorlopezfernandez.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="hosts")
public class Host extends PersistentObject {

	@OneToMany(mappedBy="host")
	private Set<Alias> aliases;
	
	@OneToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="active_theme_id",nullable=false)
	private Theme activeTheme;
	
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
	@Column(name="recent_comments_per_index_page")
	private Integer recentCommentsPerIndexPage;

	// getters & setters
	
	public Set<Alias> getAliases() {
		return aliases;
	}
	public void setAliases(Set<Alias> aliases) {
		this.aliases = aliases;
	}

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

	public Theme getActiveTheme() {
		return activeTheme;
	}
	public void setActiveTheme(Theme activeTheme) {
		this.activeTheme = activeTheme;
	}

	public Integer getArchiveEntriesPerIndexPage() {
		return archiveEntriesPerIndexPage;
	}
	public void setArchiveEntriesPerIndexPage(Integer archiveEntriesPerIndexPage) {
		this.archiveEntriesPerIndexPage = archiveEntriesPerIndexPage;
	}

	public Integer getPopularTagsPerIndexPage() {
		return popularTagsPerIndexPage;
	}
	public void setPopularTagsPerIndexPage(Integer popularTagsPerIndexPage) {
		this.popularTagsPerIndexPage = popularTagsPerIndexPage;
	}

	public Integer getRecentCommentsPerIndexPage() {
		return recentCommentsPerIndexPage;
	}
	public void setRecentCommentsPerIndexPage(Integer recentCommentsPerIndexPage) {
		this.recentCommentsPerIndexPage = recentCommentsPerIndexPage;
	}

}