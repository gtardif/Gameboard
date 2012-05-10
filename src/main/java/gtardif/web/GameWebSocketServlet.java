package gtardif.web;

import gtardif.p4.GameRepoListener;
import gtardif.p4.GameRepository;

import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class GameWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	private final Set<GameWebSocket> webSockets = new CopyOnWriteArraySet<GameWebSocket>();
	private final GameRepository gameRepository;

	public GameWebSocketServlet(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		GameWebSocket gameWebSocket = new GameWebSocket();
		gameRepository.addListener(gameWebSocket);
		return gameWebSocket;
	}

	private class GameWebSocket implements WebSocket.OnTextMessage, GameRepoListener {
		volatile Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			webSockets.add(this);
		}

		@Override
		public void onClose(int closeCode, String message) {
			webSockets.remove(this);
		}

		@Override
		public void onMessage(String message) {
			StringTokenizer tokens = new StringTokenizer(message);
			WSCommand wsCommand = WSCommand.valueOf(tokens.nextToken());
			switch (wsCommand) {
			case create:
				gameRepository.create(tokens.nextToken());
				break;
			case join:
				notifyAll(message);
			}
		}

		private void notifyAll(String message) {
			for (GameWebSocket webSocket : webSockets) {
				try {
					webSocket.connection.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void gameCreated(String gameId) {
			notifyAll("new game  : " + gameId);
		}
	}
}