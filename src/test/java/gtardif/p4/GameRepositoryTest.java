package gtardif.p4;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;
import gtardif.commons.AutoMockRunner;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AutoMockRunner.class)
public class GameRepositoryTest {
	private GameRepository gameRepository;
	private GameRepoListener mockGameRepoListener;

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

		gameRepository.create("toto");

		verify(mockGameRepoListener).gameCreated("toto");
	}

	@Test
	public void canGetGameById() {
		P4Game game = gameRepository.create("id");
		P4Game game2 = gameRepository.create("id2");

		assertThat(gameRepository.getGame("id")).isSameAs(game);
		assertThat(gameRepository.getGame("id2")).isSameAs(game2);
	}
}