package com.hectorlopezfernandez.model;

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="languages")
//@MappedSuperclass
public abstract class Language extends PersistentObject {

	@Basic(optional=false)
	@Column(name="language",length=3)
	private String langCode;
	
	@Basic(optional=true)
	@Column(name="region",length=3)
	private String regionCode;

	@Basic(optional=true)
	@Column(name="variant",length=8)
	private String variantCode;

	// getters sinteticos
	
	public Locale toLocale() {
		if (langCode == null || langCode.length() == 0) return null;
		if (regionCode == null || regionCode.length() == 0) return new Locale(langCode);
		if (variantCode == null || variantCode.length() == 0) return new Locale(langCode, regionCode);
		return new Locale(langCode, regionCode, variantCode);
	}
	
	// getters & setters
	
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getVariantCode() {
		return variantCode;
	}
	public void setVariantCode(String variantCode) {
		this.variantCode = variantCode;
	}

}