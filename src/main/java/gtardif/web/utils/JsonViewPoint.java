package gtardif.web.utils;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.zdevra.guice.mvc.ModelMap;
import org.zdevra.guice.mvc.ViewPoint;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

public class JsonViewPoint implements ViewPoint {
	@Override
	public void render(ModelMap model, HttpServlet servlet, HttpServletRequest request, HttpServletResponse response) {
		try {
			Preconditions.checkArgument(model.entrySet().size() == 1, "Controller has not returned any object to serialize to JSON");
			Object objectToJsonify = model.entrySet().iterator().next().getValue();

			response.setContentType("application/json");
			response.setStatus(HttpStatus.OK_200);
			response.getOutputStream().write(new Gson().toJson(objectToJsonify).getBytes());
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}
}
