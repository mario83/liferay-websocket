package it.manza.websocket.logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Component(
		immediate = true,
		service = WebSocketAppender.class
)
public class WebSocketAppender extends AppenderSkeleton {
	
	@Activate
	protected void activate(BundleContext bundleContext) {
		_log.info("Activate WebSocketAppender");
		_webSocketAppender = new WebSocketAppender();
		_webSocketAppender.setLayout(new PatternLayout(PATTERN_LAYOUT));
		Logger.getRootLogger().addAppender(_webSocketAppender);
	}

	@Deactivate
	protected void deactivate() {
		Logger.getRootLogger().removeAppender(_webSocketAppender);
	}
	
	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		WebSocketLoggerApplication.broadcast(this.layout.format(event));
	}
	
	private WebSocketAppender _webSocketAppender;
	
	private static final String PATTERN_LAYOUT = "%d{ABSOLUTE} %-5p [%c{1}:%L] %m%n";
	
	private static final Log _log = LogFactoryUtil.getLog(WebSocketAppender.class);
}
