package gtardif.web;

import gtardif.p4.GameRepoListener;
import gtardif.p4.GameRepository;
import gtardif.p4.P4Game;

import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import com.google.common.base.Throwables;
import com.google.gson.Gson;

public class GameWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	private final Set<GameWebSocket> webSockets = new CopyOnWriteArraySet<GameWebSocket>();
	private final GameRepository gameRepository;

	@Inject
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
			System.out.println("CLOSING WS " + message);
			gameRepository.removeListener(this);
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
			}
		}

		@Override
		public void gameCreated(P4Game newGame) {
			sendMessage(new NewGameMsg(newGame));
		}

		private void sendMessage(WebSocketMessage message) {
			try {
				connection.sendMessage(new Gson().toJson(message));
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
	}

	private static class NewGameMsg extends WebSocketMessage {
		@SuppressWarnings("unused")
		private final P4Game newGame;

		public NewGameMsg(P4Game newGame) {
			super("new game : " + newGame.getName());
			this.newGame = newGame;
		}
	}

	private static class WebSocketMessage {
		@SuppressWarnings("unused")
		private final String message;

		public WebSocketMessage(String message) {
			this.message = message;
		}
	}
}