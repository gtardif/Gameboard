package gtardif.web;

import gtardif.p4.GameRepository;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

@Singleton
public class GameWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	private final GameRepository gameRepository;
	static final AtomicInteger nextGameId = new AtomicInteger(0);
	private static final AtomicInteger nextUserId = new AtomicInteger(0);

	@Inject
	public GameWebSocketServlet(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		UserWebSocket gameWebSocket = new UserWebSocket(nextUserId.incrementAndGet(), gameRepository);
		return gameWebSocket;
	}

	public void reset() {
		nextGameId.set(0);
	}
}