package gtardif.p4;

import static gtardif.p4.P4Game.*;
import gtardif.utils.Iter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameRepository {
	private final List<P4Game> games = new CopyOnWriteArrayList<P4Game>();
	private final List<GameRepoListener> listeners = new CopyOnWriteArrayList<GameRepoListener>();

	public List<P4Game> getGames() {
		return games;
	}

	public P4Game create(String gameId) {
		P4Game newGame = new P4Game(gameId);
		games.add(newGame);
		notifyGameCreated(gameId);
		return newGame;
	}

	public void addListener(GameRepoListener listener) {
		listeners.add(listener);
	}

	private void notifyGameCreated(String gameId) {
		for (GameRepoListener listener : listeners) {
			listener.gameCreated(gameId);
		}
	}

	public P4Game getGame(String gameId) {
		return Iter.with(games).indexBy(TO_NAME).get(gameId);
	}
}
