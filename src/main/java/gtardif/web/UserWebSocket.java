package gtardif.web;

import gtardif.p4.GameRepoListener;
import gtardif.p4.GameRepository;
import gtardif.p4.P4Game;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jetty.websocket.WebSocket;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

class UserWebSocket implements WebSocket.OnTextMessage, GameRepoListener {
	private volatile Connection connection;
	private final int userId;
	private final GameRepository gameRepository;

	public UserWebSocket(int userId, GameRepository gameRepository) {
		this.userId = userId;
		this.gameRepository = gameRepository;
		gameRepository.addListener(this);
	}

	@Override
	public void onOpen(Connection connection) {
		System.out.println("WS - user " + userId + " logged in");
		this.connection = connection;
		this.sendMessageMap(ImmutableMap.of("userId", userId, "message", "User " + userId + " logged in"));
	}

	@Override
	public void onClose(int closeCode, String message) {
		System.out.println("CLOSING WS " + message + " - " + userId);
		// TODO restore websocket on client side if closed, with info
		// TODO restore needed info when reloading page
		gameRepository.removeListener(this);
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

	@Override
	public void gameUpdated(P4Game newGame) {
		sendMessage(WebSocketMessage.gameUpdate(newGame));
	}

	private String nextGameId() {
		return "" + GameWebSocketServlet.nextGameId.incrementAndGet();
	}

	private void sendMessage(WebSocketMessage message) {
		try {
			System.out.println("sending to " + userId + " msg : " + new Gson().toJson(message));
			connection.sendMessage(new Gson().toJson(message));
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}

	void sendMessageMap(Map<String, ? extends Object> message) {
		try {
			System.out.println("sending to " + userId + " msg : " + new Gson().toJson(message));
			connection.sendMessage(new Gson().toJson(message));
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}
}