package it.manza.websocket;

import java.io.IOException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Component(immediate = true)
public class WebSocketServer {

	@Activate
	protected void activate(final BundleContext bundleContext) {
		_serviceTracker = ServiceTrackerFactory.open(
				bundleContext, WebSocketApplication.class,
				new WebSocketServiceTrackerCustomizer(bundleContext, CONTEXT_PATH));
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

		for (NetworkListener listener : server.getListeners()) {
			listener.registerAddOn(addon);
		}
		
		try {
			server.start();
			_log.info("WebSocket server started on port " + SERVER_PORT);
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
	private ServiceTracker<WebSocketApplication, WebSocketApplication> _serviceTracker;
	private static final String CONTEXT_PATH = "/remoteagent";
  private static final int SERVER_PORT = 9000;
	private static final Log _log = LogFactoryUtil.getLog(WebSocketServer.class);
}
