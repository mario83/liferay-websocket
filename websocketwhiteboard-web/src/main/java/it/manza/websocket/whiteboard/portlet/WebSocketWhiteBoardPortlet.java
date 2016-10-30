package it.manza.websocket.whiteboard.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.websocket",
		"com.liferay.portlet.instanceable=true",
		"com.liferay.portlet.css-class-wrapper=websocket-whiteboard-portlet",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.header-portlet-javascript=/js/paper-core.min.js",
		"com.liferay.portlet.header-portlet-javascript=/js/main.js",
		"javax.portlet.display-name=websocket-whiteboard-portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class WebSocketWhiteBoardPortlet extends MVCPortlet {
}