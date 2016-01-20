package com.hectorlopezfernandez.integration;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hectorlopezfernandez.pebblejodatime.JodaExtension;
import com.hectorlopezfernandez.pebbleshiro.ShiroExtension;
import com.hectorlopezfernandez.pebblestripes.PebbleServlet;
import com.hectorlopezfernandez.utils.Constants;
import com.mitchellbosecke.pebble.PebbleEngine;

public class CustomPebbleServlet extends PebbleServlet {

	@Override
	protected void configureEngine(PebbleEngine.Builder builder, ServletConfig servletConfig) {
		ShiroExtension se = new ShiroExtension();
		JodaExtension je = new JodaExtension();
		builder.extension(se,je);
	}

	@Override
	protected void configureEvaluationContext(Map<String, Object> context, HttpServletRequest request,
			HttpServletResponse response) {
		context.put("APPLICATION_TAG", Constants.APPLICATION_TAG);
	}

}