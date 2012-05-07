package gtardif.web;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.zdevra.guice.mvc.ViewPoint;

import com.google.common.base.Throwables;
import com.google.gson.Gson;

public class JsonView implements ViewPoint {
	private final Object toRender;

	public JsonView(Object toRender) {
		this.toRender = toRender;
	}

	@Override
	public void render(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("application/json");
			response.setStatus(HttpStatus.OK_200);
			response.getOutputStream().write(new Gson().toJson(toRender).getBytes());
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}
}
