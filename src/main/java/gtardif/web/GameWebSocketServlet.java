package gtardif.web;

import gtardif.p4.GameRepoListener;
import gtardif.p4.GameRepository;
import gtardif.p4.P4Board;
import gtardif.p4.P4Game;
import gtardif.p4.P4Player;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

public class GameWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	private final Set<GameWebSocket> webSockets = new CopyOnWriteArraySet<GameWebSocket>();
	private final GameRepository gameRepository;
	private static final AtomicInteger nextGameId = new AtomicInteger(0);
	private static final AtomicInteger nextUserId = new AtomicInteger(0);

	@Inject
	public GameWebSocketServlet(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		GameWebSocket gameWebSocket = new GameWebSocket(nextUserId.incrementAndGet());
		gameRepository.addListener(gameWebSocket);
		return gameWebSocket;
	}

	private class GameWebSocket implements WebSocket.OnTextMessage, GameRepoListener {
		private volatile Connection connection;
		private final int userId;

		public GameWebSocket(int userId) {
			this.userId = userId;
		}

		@Override
		public void onOpen(Connection connection) {
			System.out.println("WS - user " + userId + " logged in");
			this.connection = connection;
			webSockets.add(this);
			this.sendMessageMap(ImmutableMap.of("userId", userId, "message", "User " + userId + " logged in"));
		}

		@Override
		public void onClose(int closeCode, String message) {
			System.out.println("CLOSING WS " + message);
			// TODO restore websocket on client side if closed, with info
			// TODO restore needed info when reloading page
			gameRepository.removeListener(this);
			webSockets.remove(this);
		}

		@Override
		public void onMessage(String message) {
			try {
				System.out.println("WS (user " + userId + ") : " + message);
				StringTokenizer tokens = new StringTokenizer(message);
				WSCommand wsCommand = WSCommand.valueOf(tokens.nextToken());
				switch (wsCommand) {
				case create:
					P4Game newGame = gameRepository.create(nextGameId());
					newGame.addPlayer(new WebP4Player("" + userId, this));
					break;
				case join:
					P4Game game = gameRepository.getGame(tokens.nextToken());
					game.addPlayer(new WebP4Player("" + userId, this));
					gameRepository.notifyGameUpdated(game);
				}
			} catch (Exception e) {
				sendMessage(WebSocketMessage.error(e.getMessage()));
			}
		}

		private String nextGameId() {
			return "" + nextGameId.incrementAndGet();
		}

		@Override
		public void gameUpdated(P4Game newGame) {
			sendMessage(WebSocketMessage.gameUpdate(newGame));
		}

		private void sendMessage(WebSocketMessage message) {
			try {
				System.out.println("sending to " + userId + " msg : " + new Gson().toJson(message));
				connection.sendMessage(new Gson().toJson(message));
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}

		private void sendMessageMap(Map<String, ? extends Object> message) {
			try {
				connection.sendMessage(new Gson().toJson(message));
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}

	}

	private static class WebP4Player implements P4Player {
		private final String name;
		private final transient GameWebSocket ws;

		public WebP4Player(String name, GameWebSocket ws) {
			this.name = name;
			this.ws = ws;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void yourTurn(P4Board board) {
			ws.sendMessageMap(ImmutableMap.of("yourTurn", true, "board", board));
		}

		@Override
		public void youWin(P4Board board) {
		}

		@Override
		public void youLoose(P4Board board) {
		}
	}

}