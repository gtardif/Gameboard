package gtardif.web;

import static gtardif.p4.Status.*;
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
	private static final String USER_ID = "userId";
	private static final String STARTED_GAME = "startedGame";
	private static final String UPDATED_GAME = "updatedGame";
	private static final String SUCCESS = "success";
	private static final String MESSAGE = "message";
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
		this.sendMessage(ImmutableMap.of(USER_ID, userId, MESSAGE, "User " + userId + " logged in"));
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
				if (game.getStatus() == STARTED) {
					gameRepository.notifyGameStarted(game);
				}
				gameRepository.notifyGameUpdated(game);
			}
		} catch (Exception e) {
			sendMessage(ImmutableMap.of(MESSAGE, "Error : " + e.getMessage(), SUCCESS, false));
		}
	}

	@Override
	public void gameUpdated(P4Game game) {
		sendMessage(ImmutableMap.of(MESSAGE, "Game update :" + game.getName(), UPDATED_GAME, game));
	}

	@Override
	public void gameStarted(P4Game game) {
		sendMessage(ImmutableMap.of(MESSAGE, "Game started :" + game.getName(), STARTED_GAME, game));
	}

	private String nextGameId() {
		return "" + GameWebSocketServlet.nextGameId.incrementAndGet();
	}

	void sendMessage(Map<String, ? extends Object> data) {
		try {
			System.out.println("sending to " + userId + " msg : " + new Gson().toJson(data));
			connection.sendMessage(new Gson().toJson(data));
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}
}