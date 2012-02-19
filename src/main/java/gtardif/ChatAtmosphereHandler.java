package gtardif;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;

import com.google.gson.Gson;

public class ChatAtmosphereHandler implements AtmosphereHandler<HttpServletRequest, HttpServletResponse> {
	@Override
	public void onRequest(AtmosphereResource<HttpServletRequest, HttpServletResponse> resource) throws IOException {
		HttpServletRequest request = resource.getRequest();
		HttpServletResponse response = resource.getResponse();
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		if ("GET".equals(request.getMethod())) {
			response.setContentType("text/plain");
			resource.suspend();
		} else if ("POST".equals(request.getMethod())) {
			Map<String, String> data = new LinkedHashMap<String, String>();
			data.put("username", request.getParameter("username"));
			data.put("message", request.getParameter("message"));
			resource.getBroadcaster().broadcast(new Gson().toJson(data));
		}
	}

	@Override
	public void onStateChange(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) throws IOException {
		if (event.getMessage() == null) {
			return;
		}
		sendMessage(event.getResource().getResponse().getWriter(), event.getMessage().toString());
	}

	private void sendMessage(PrintWriter writer, String message) {
		writer.print(message.length());
		writer.print(";");
		writer.print(message);
		writer.print(";");
		writer.flush();
	}

	@Override
	public void destroy() {
	}
}
