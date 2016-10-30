package it.manza.websocket;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

@Component(immediate = true)
public class WebSocketServer {

	@Activate
	protected void activate(final BundleContext bundleContext) {
		_serviceTracker = ServiceTrackerFactory.open(
				bundleContext, WebSocketApplication.class,
				new ServiceTrackerCustomizer<WebSocketApplication, WebSocketApplication>() {

					@Override
					public WebSocketApplication addingService(ServiceReference<WebSocketApplication> serviceReference) {
						String applicationName = GetterUtil.getString(serviceReference.getProperty("application.name"));

						WebSocketApplication webSocketApplication =	bundleContext.getService(serviceReference);
						
						WebSocketEngine.getEngine().register(CONTEXT_PATH, applicationName, webSocketApplication);

						return webSocketApplication;
					}

					@Override
					public void modifiedService(ServiceReference<WebSocketApplication> serviceReference, WebSocketApplication serviceRegistration) {
						removedService(serviceReference, serviceRegistration);

						addingService(serviceReference);
					}

					@Override
					public void removedService(ServiceReference<WebSocketApplication> serviceReference, WebSocketApplication serviceRegistration) {
						
						bundleContext.ungetService(serviceReference);
					}

				});
		runServer();
	}
	
	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
		stopServer();
	}
	
	private void runServer() {
		server = HttpServer.createSimpleServer(CONTEXT_PATH, SERVER_PORT);
		final WebSocketAddOn addon = new WebSocketAddOn();
		_log.info("WebSocket server started on port " + SERVER_PORT + ".");

		for (NetworkListener listener : server.getListeners()) {
			listener.registerAddOn(addon);
		}
		
		try {
			server.start();
		} catch (IOException e) {
			_log.error(e);
		}

	}

	private void stopServer() {
		if (server != null) {
			server.shutdownNow();
		}
	}
	
	private HttpServer server;
	private static final String CONTEXT_PATH = "/remoteagent";
  private static final int SERVER_PORT = 9000;

	private ServiceTracker<WebSocketApplication, WebSocketApplication> _serviceTracker;
	
	private static final Log _log = LogFactoryUtil.getLog(WebSocketServer.class);
}
