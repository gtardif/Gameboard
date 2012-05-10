package gtardif.web;

import gtardif.sample.ChatServlet;

import java.util.EnumSet;
import java.util.logging.LogManager;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

public class GameWebServer extends AbstractIdleService {
	static {
		try {
			Preconditions.checkArgument(GameWebServer.class.getResourceAsStream("/log4j.properties") != null);
			LogManager.getLogManager().readConfiguration(GameWebServer.class.getResourceAsStream("/log4j.properties"));
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	private final int port;
	private Server server;

	public GameWebServer(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public static void main(String[] args) {
		GameWebServer gameServer = new GameWebServer(1080);
		gameServer.startAndWait();
	}

	@Override
	protected void startUp() throws Exception {
		server = new Server(port);
		Injector injector = Guice.createInjector(new GameServerModule());

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setResourceBase("src/main/webapp/");
		webapp.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		webapp.addServlet(ChatServlet.class, "/chat");
		webapp.addServlet(new ServletHolder(injector.getInstance(GameWebSocketServlet.class)), "/gameMessage");
		webapp.addEventListener(injector.getInstance(LimeServletListener.class));

		server.setHandler(webapp);
		server.setStopAtShutdown(true);
		server.start();
	}

	@Override
	protected void shutDown() throws Exception {
		server.stop();
	}

	private static final class GameServerModule extends AbstractModule {
		@Override
		protected void configure() {
		}
	}
}
