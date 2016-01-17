package com.hectorlopezfernandez.integration;

import javax.servlet.ServletConfig;

import com.hectorlopezfernandez.pebblestripes.PebbleServlet;
import com.mitchellbosecke.pebble.PebbleEngine;

public class CustomPebbleServlet extends PebbleServlet {

	protected void configureEngine(PebbleEngine.Builder builder, ServletConfig servletConfig) {
		builder.templateCache(null);
	}

}