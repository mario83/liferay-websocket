package it.manza.websocket.whiteboard;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
				"application.name=/whiteboardapp"
		},
		service = WebSocketApplication.class
)
public class WebSocketWhiteboardApplication extends WebSocketApplication{

	@Override
	public void onMessage(WebSocket websocket, String data) {
		_log.info(data);
		List<WebSocket> collect = 
				members.stream().
					filter(m -> !m.equals(websocket)).
					collect(Collectors.toList());
		broadcaster.broadcast(collect, data);
	}

	@Override
	public void onConnect(WebSocket websocket) {
		members.add(websocket);
	}

	@Override
	public void onClose(WebSocket websocket, DataFrame frame) {
		members.remove(websocket);
	}

	private static final Set<WebSocket> members = Collections
			.newSetFromMap(DataStructures.<WebSocket, Boolean>getConcurrentMap());
	
	private final static Broadcaster broadcaster = new OptimizedBroadcaster();

	private static final Logger _log = Logger.getLogger(WebSocketWhiteboardApplication.class);
}