package gtardif.web;

import gtardif.p4.P4Game;

public class WebSocketMessage {
	@SuppressWarnings("unused")
	private final String message;

	public WebSocketMessage(String message) {
		this.message = message;
	}

	public static WebSocketMessage error(String message) {
		return new Error("Error : " + message);
	}

	public static WebSocketMessage gameUpdate(final P4Game game) {
		return new GameUpdate("Game update : " + game.getName(), game);
	}

	private static final class GameUpdate extends WebSocketMessage {
		@SuppressWarnings("unused")
		private final P4Game updatedGame;

		private GameUpdate(String message, P4Game game) {
			super(message);
			updatedGame = game;
		}
	}

	private static final class Error extends WebSocketMessage {
		@SuppressWarnings("unused")
		private final boolean success = false;

		private Error(String message) {
			super(message);
		}
	}
}
