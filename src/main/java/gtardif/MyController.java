package gtardif;

import org.zdevra.guice.mvc.annotations.Controller;
import org.zdevra.guice.mvc.annotations.Model;
import org.zdevra.guice.mvc.annotations.Path;
import org.zdevra.guice.mvc.annotations.View;

@Controller
public class MyController {
	public MyController() {
		System.out.println("controller");
	}

	@Path("/hello.html")
	@Model("msg")
	@View("hello.jsp")
	public String hello() {
		System.out.println("render");
		return "world";
	}
}
