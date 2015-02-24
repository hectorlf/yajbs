package com.hectorlopezfernandez.integration;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.DefaultActionBeanContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GuiceActionBeanContextFactory extends DefaultActionBeanContextFactory {

    private static final Logger logger = LoggerFactory.getLogger(GuiceActionBeanContextFactory.class);

    private final Injector theInjector;
    private Class<? extends ActionBeanContext> theContextClass;

    @Inject
    public GuiceActionBeanContextFactory(Injector anInjector) {
        theInjector = anInjector;
    }

    @Override
    public void init(Configuration aConfiguration) throws Exception {
        setConfiguration(aConfiguration);
        final Class<? extends ActionBeanContext> myClass = getActionBeanClass(aConfiguration);
        logger.info("Setting up Stripes with ActionBeanContext subclass: {}", myClass.getClass().getSimpleName());
        this.theContextClass = myClass;
    }

    private Class<? extends ActionBeanContext> getActionBeanClass(Configuration aConfiguration) {
        final Class<? extends ActionBeanContext> myClass = aConfiguration.getBootstrapPropertyResolver().getClassProperty(CONTEXT_CLASS_NAME, ActionBeanContext.class);
        return (myClass == null) ? ActionBeanContext.class : myClass;
    }

    @Override
    public ActionBeanContext getContextInstance(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        final ActionBeanContext myContext = theInjector.getInstance(theContextClass);
        myContext.setRequest(aRequest);
        myContext.setResponse(aResponse);
        return myContext;
    }

}