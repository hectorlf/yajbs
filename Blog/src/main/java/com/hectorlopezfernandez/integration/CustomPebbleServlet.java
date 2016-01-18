package com.hectorlopezfernandez.integration;

import javax.servlet.ServletConfig;

import com.hectorlopezfernandez.pebblejodatime.JodaExtension;
import com.hectorlopezfernandez.pebbleshiro.ShiroExtension;
import com.hectorlopezfernandez.pebblestripes.PebbleServlet;
import com.mitchellbosecke.pebble.PebbleEngine;

public class CustomPebbleServlet extends PebbleServlet {

	protected void configureEngine(PebbleEngine.Builder builder, ServletConfig servletConfig) {
		ShiroExtension se = new ShiroExtension();
		JodaExtension je = new JodaExtension();
		builder.extension(se,je);
//		builder.templateCache(null);
	}

}