package gtardif.p4;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import net.gageot.test.mocks.AutoMockRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AutoMockRunner.class)
public class GameRepositoryTest {
	private GameRepository gameRepository;
	private GameRepoListener mockGameRepoListener;
	private P4Game mockGame;

	@Before
	public void setUp() {
		gameRepository = new GameRepository();
	}

	@Test
	public void shouldContainZeroGamesByDefault() {
		List<P4Game> games = gameRepository.getGames();

		assertThat(games).isEmpty();
	}

	@Test
	public void canCreateGame() {
		P4Game game = gameRepository.create("id");

		assertThat(game.getName()).isEqualTo("id");
	}

	@Test
	public void canListCreatedGames() {
		P4Game game1 = gameRepository.create("id");
		P4Game game2 = gameRepository.create("id2");

		List<P4Game> games = gameRepository.getGames();

		assertThat(games).containsExactly(game1, game2);
	}

	@Test
	public void canNotifyWhenGameCreated() {
		gameRepository.addListener(mockGameRepoListener);

		P4Game newGame = gameRepository.create("toto");

		verify(mockGameRepoListener).gameUpdated(newGame);
	}

	@Test
	public void canNotifyGameUpdated() {
		gameRepository.addListener(mockGameRepoListener);

		gameRepository.notifyGameUpdated(mockGame);

		verify(mockGameRepoListener).gameUpdated(mockGame);
	}

	@Test
	public void canNotifyGameStarted() {
		gameRepository.addListener(mockGameRepoListener);

		gameRepository.notifyGameStarted(mockGame);

		verify(mockGameRepoListener).gameStarted(mockGame);
	}

	@Test
	public void canRemoveListener() {
		gameRepository.addListener(mockGameRepoListener);
		gameRepository.removeListener(mockGameRepoListener);

		gameRepository.create("toto");

		verifyZeroInteractions(mockGameRepoListener);
	}

	@Test
	public void canGetGameById() {
		P4Game game = gameRepository.create("id");
		P4Game game2 = gameRepository.create("id2");

		assertThat(gameRepository.getGame("id")).isSameAs(game);
		assertThat(gameRepository.getGame("id2")).isSameAs(game2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsExceptionIfGameIdAreadyExist() {
		gameRepository.create("ID1");
		gameRepository.create("ID1");
	}

	@Test
	public void canResetRepositoryGameList() {
		gameRepository.create("id1");

		gameRepository.reset();

		assertThat(gameRepository.getGame("id1")).isNull();
	}

	@Test
	public void canResetRepositoryGameListeners() {
		gameRepository.addListener(mockGameRepoListener);

		gameRepository.reset();

		gameRepository.notifyGameUpdated(mockGame);
		verifyZeroInteractions(mockGameRepoListener);
	}
}