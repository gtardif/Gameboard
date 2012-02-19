package gtardif;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.zdevra.guice.mvc.MvcModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ServerMain extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		System.out.println("injector");

		return Guice.createInjector(new MyModule());
	}

	static class MyModule extends MvcModule {
		@Override
		protected void configureControllers() {
			System.out.println("server");
			control("/hello/*").withController(MyController.class);
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(getPort());

		WebAppContext webappContext = new WebAppContext("src/main/webapp", "/");
		webappContext.setParentLoaderPriority(true);
		server.setHandler(webappContext);

		server.start();
		server.join();
	}

	private static int getPort() {
		try {
			return Integer.valueOf(System.getenv("PORT"));
		} catch (Exception e) {
			return 8080;
		}
	}
}
