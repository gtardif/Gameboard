package gtardif;

import org.zdevra.guice.mvc.annotations.Controller;
import org.zdevra.guice.mvc.annotations.Model;
import org.zdevra.guice.mvc.annotations.Path;
import org.zdevra.guice.mvc.annotations.View;

@Controller
public class MyController {
	@Path("/hello.html")
	@Model("msg")
	@View("hello.jsp")
	public String hello() {
		return "world";
	}
}
