
package it.manza.websocket;

import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.liferay.portal.kernel.util.GetterUtil;

public class WebSocketServiceTrackerCustomizer implements
	ServiceTrackerCustomizer<WebSocketApplication, WebSocketApplication> {

	public WebSocketServiceTrackerCustomizer(
		BundleContext bundleContext, String contexPath) {
		_bundleContext = bundleContext;
		_contextPath = contexPath;
	}

	@Override
	public WebSocketApplication addingService(
		ServiceReference<WebSocketApplication> serviceReference) {

		String applicationName = GetterUtil.getString(
			serviceReference.getProperty("application.name"));

		WebSocketApplication webSocketApplication =
			_bundleContext.getService(serviceReference);

		WebSocketEngine.getEngine().register(
			_contextPath, applicationName, webSocketApplication);

		return webSocketApplication;
	}

	@Override
	public void modifiedService(
		ServiceReference<WebSocketApplication> serviceReference,
		WebSocketApplication serviceRegistration) {

		removedService(serviceReference, serviceRegistration);
		addingService(serviceReference);
	}

	@Override
	public void removedService(
		ServiceReference<WebSocketApplication> serviceReference,
		WebSocketApplication serviceRegistration) {

		WebSocketApplication webSocketApplication =
						_bundleContext.getService(serviceReference);
		
		WebSocketEngine.getEngine().unregister(webSocketApplication);
		_bundleContext.ungetService(serviceReference);
	}

	private final BundleContext _bundleContext;
	private final String _contextPath;
}
