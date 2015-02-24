package com.hectorlopezfernandez.integration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.RuntimeConfiguration;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.exception.StripesRuntimeException;

import com.google.inject.Injector;
import com.hectorlopezfernandez.utils.Constants;

public class GuiceRuntimeConfiguration extends RuntimeConfiguration {

	@Override
    protected <T extends ConfigurableComponent> T initializeComponent(Class<T> aComponentType, String aPropertyName) {
        final Class<?> myClass = getBootstrapPropertyResolver().getClassProperty(aPropertyName, aComponentType);
        if (myClass != null) {
            try {
                final Object myInstance = getInjectorFromContext().getInstance(myClass);
                final T myComponent = aComponentType.cast(myInstance);
                myComponent.init(this);
                return myComponent;
            } catch (Exception myException) {
                throw new StripesRuntimeException("Could not instantiate configured " + aComponentType.getSimpleName() + " of type [" + myClass.getSimpleName() + "]. Please check the configuration parameters specified in your web.xml.", myException);
            }
        } else {
            return null;
        }
    }

    @Override
    protected Map<LifecycleStage, Collection<Interceptor>> initInterceptors(@SuppressWarnings("rawtypes") List aClasses) {
    	if (aClasses == null || aClasses.size() == 0) return Collections.emptyMap();
        final Map<LifecycleStage, Collection<Interceptor>> myInterceptors = new HashMap<LifecycleStage, Collection<Interceptor>>(aClasses.size());
        @SuppressWarnings("unchecked")
        final List<Class<Interceptor>> myInterceptorList = (List<Class<Interceptor>>) aClasses;
        for (Class<Interceptor> myType : myInterceptorList) {
            try {
                final Interceptor myInterceptor = getInjectorFromContext().getInstance(myType);
                addInterceptor(myInterceptors, myInterceptor);
            } catch (Exception myException) {
                throw new StripesRuntimeException("Could not instantiate configured Interceptor", myException);
            }
        }
        return myInterceptors;
    }

    private Injector getInjectorFromContext() {
        final ServletContext myServletContext = getServletContext();
        Injector i = (Injector)myServletContext.getAttribute(Constants.ROOT_GUICE_INJECTOR_CONTEXT_ATTRIBUTE_NAME);
        if (i == null) throw new StripesRuntimeException("Could not retrieve guice injector from servletcontext");
        return i;
    }

}