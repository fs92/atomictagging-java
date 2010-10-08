package org.atomictagging.core.internal;

import org.atomictagging.core.accessors.DB;
import org.atomictagging.core.configuration.Configuration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Initializes configuration and database.
 * 
 * @author Stephan Mann
 */
public class Activator implements BundleActivator {

	private static BundleContext	context;


	/**
	 * @return the context
	 */
	public static BundleContext getContext() {
		return context;
	}


	@Override
	public void start( final BundleContext bundleContext ) throws Exception {
		Activator.context = bundleContext;
		Configuration.init();
		DB.init();
	}


	@Override
	public void stop( final BundleContext bundleContext ) throws Exception {
		Activator.context = null;
	}

}
