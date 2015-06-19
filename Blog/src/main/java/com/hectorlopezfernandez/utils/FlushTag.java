package com.hectorlopezfernandez.utils;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class FlushTag implements Tag {

	private PageContext pc;

	@Override
	public void setPageContext(PageContext pc) {
		this.pc = pc;
	}

	@Override
	public void setParent(Tag t) {
	}

	@Override
	public Tag getParent() {
		return null;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			pc.getOut().flush();
		} catch(IOException ioe) {
			throw new JspException(ioe);
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		this.pc = null;
	}

}