package gtardif.web;

import gtardif.p4.GameRepository;
import gtardif.sample.ChatServlet;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.inject.servlet.GuiceFilter;

public class GameWebServer {
	private final int port;
	private Server server;

	public GameWebServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		server = new Server(port);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setResourceBase("src/main/webapp/");
		webapp.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		webapp.addServlet(ChatServlet.class, "/chat");
		GameRepository gameRepository = new GameRepository();
		webapp.addServlet(new ServletHolder(new GameWebSocketServlet(gameRepository)), "/gameMessage");
		webapp.addEventListener(new LimeServletListener(gameRepository));

		server.setHandler(webapp);
		server.setStopAtShutdown(true);
		server.start();
	}

	public void startAndJoin() throws Exception {
		start();
		server.join();
	}

	public boolean isRunning() {
		return server.isRunning();
	}

	public void stop() throws Exception {
		server.stop();
	}

	public static void main(String[] args) throws Exception {
		GameWebServer gameServer = new GameWebServer(1080);
		gameServer.startAndJoin();
	}
}
