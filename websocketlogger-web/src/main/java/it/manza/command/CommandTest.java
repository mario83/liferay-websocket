package it.manza.command;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Component(
		immediate = true,
		property = {
			"osgi.command.scope=test",
			"osgi.command.function=log"
		},
		service = Object.class
)
public class CommandTest {
	public void log(){
		_log.info("test log");
	}
	
	private static final Log _log = LogFactoryUtil.getLog(CommandTest.class);
}
