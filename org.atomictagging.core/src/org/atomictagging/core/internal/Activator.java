package org.atomictagging.core.internal;

import org.atomictagging.core.configuration.Configuration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext	context;


	/**
	 * @return the context
	 */
	public static BundleContext getContext() {
		return context;
	}


	@Override
	public void start( BundleContext bundleContext ) throws Exception {
		Activator.context = bundleContext;
		Configuration.init();
	}


	@Override
	public void stop( BundleContext context ) throws Exception {
		Activator.context = null;

	}

}
