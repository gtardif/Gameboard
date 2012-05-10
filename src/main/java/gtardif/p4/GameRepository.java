package gtardif.p4;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Singleton;

import net.gageot.listmaker.ListMaker;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

@Singleton
public class GameRepository {
	private final Map<String, P4Game> games = Maps.newHashMap();
	private final List<GameRepoListener> listeners = new CopyOnWriteArrayList<GameRepoListener>();

	public List<P4Game> getGames() {
		return ListMaker.with(games.values()).sortOn(P4Game.TO_NAME).toList();
	}

	public synchronized P4Game create(String gameId) {
		Preconditions.checkArgument(!games.keySet().contains(gameId), "Game with id " + gameId + " already exist.");
		P4Game newGame = new P4Game(gameId);
		games.put(gameId, newGame);
		notifyGameUpdated(newGame);
		return newGame;
	}

	public void addListener(GameRepoListener listener) {
		listeners.add(listener);
	}

	public void notifyGameUpdated(P4Game newGame) {
		for (GameRepoListener listener : listeners) {
			listener.gameUpdated(newGame);
		}
	}

	public P4Game getGame(String gameId) {
		return games.get(gameId);
	}

	public void removeListener(GameRepoListener gameRepoListener) {
		listeners.remove(gameRepoListener);
	}
}
