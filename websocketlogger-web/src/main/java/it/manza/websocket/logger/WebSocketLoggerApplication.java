package it.manza.websocket.logger;

import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.utils.DataStructures;
import org.glassfish.grizzly.websockets.Broadcaster;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.OptimizedBroadcaster;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		property = {
				"application.name=/logapp"
		},
		service = WebSocketApplication.class
)
public class WebSocketLoggerApplication extends WebSocketApplication{

	@Override
	public void onMessage(WebSocket websocket, String data) {
		
	}

	@Override
	public void onConnect(WebSocket websocket) {
		members.add(websocket);
	}

	@Override
	public void onClose(WebSocket websocket, DataFrame frame) {
		members.remove(websocket);
	}

	
	public static void broadcast(String jsonMessage) {
		broadcaster.broadcast(members, jsonMessage);
	}

	// Logged in members
	private static final Set<WebSocket> members = Collections
			.newSetFromMap(DataStructures.<WebSocket, Boolean>getConcurrentMap());
	
	// initialize optimized broadcaster
	private final static Broadcaster broadcaster = new OptimizedBroadcaster();

	private static final Logger _log = Logger.getLogger(WebSocketLoggerApplication.class);
}