package com.hectorlopezfernandez.integration;

import javax.inject.Inject;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.ActionResolver;
import net.sourceforge.stripes.controller.NameBasedActionResolver;

import com.google.inject.Injector;

public class GuiceActionResolver extends NameBasedActionResolver implements ActionResolver {
    
    private final Injector theInjector;

    @Inject
    public GuiceActionResolver(Injector anInjector) {
        theInjector = anInjector;
    }

    @Override
    protected ActionBean makeNewActionBean(Class<? extends ActionBean> aType, ActionBeanContext aContext) {
        return theInjector.getInstance(aType);
    }

}