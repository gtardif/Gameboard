package gtardif.p4;

import static gtardif.p4.Status.*;
import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;
import net.gageot.test.mocks.AutoMockRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AutoMockRunner.class)
public class P4GameTest {
	private P4Game game;
	private P4Player mockPlayer1;
	private P4Player mockPlayer2;
	private P4Board mockBoard1;
	private P4Board mockBoard2;
	private P4Board mockBoard3;
	private P4Move mockMove1;
	private P4Move mockMove2;

	@Before
	public void setUp() {
		when(mockPlayer1.getName()).thenReturn("toto");
		when(mockPlayer2.getName()).thenReturn("titi");
		game = spy(new P4Game("test"));
		when(game.createBoard(mockPlayer1, mockPlayer2)).thenReturn(mockBoard1);
	}

	@Test
	public void a_game_is_waiting_for_players_to_join() {
		assertThat(game.getStatus()).isEqualTo(WAITING);
	}

	@Test
	public void a_game_with_1_player_is_waiting() {
		game.addPlayer(mockPlayer1);

		assertThat(game.getStatus()).isEqualTo(WAITING);
	}

	@Test
	public void a_game_with_2_player_is_started() {
		game.addPlayer(mockPlayer1);
		game.addPlayer(mockPlayer2);

		assertThat(game.getStatus()).isEqualTo(STARTED);
	}

	@Test(expected = IllegalStateException.class)
	public void cannot_add_more_than_2_players() {
		game.addPlayer(mockPlayer1);
		game.addPlayer(mockPlayer2);
		game.addPlayer(mock(P4Player.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannot_add_same_player_twice() {
		when(mockPlayer2.getName()).thenReturn("toto");
		game.addPlayer(mockPlayer1);
		game.addPlayer(mockPlayer2);
	}

	@Test
	public void player1_receives_notification_when_game_started() {
		game.addPlayer(mockPlayer1);
		game.addPlayer(mockPlayer2);

		verify(mockPlayer1).yourTurn(mockBoard1);
	}

	@Test
	public void player2_receives_notification_when_player1_has_played() {
		when(mockBoard1.play(mockPlayer1, mockMove1)).thenReturn(mockBoard2);
		when(mockBoard2.nextPlayer()).thenReturn(mockPlayer2);
		game.addPlayer(mockPlayer1);
		game.addPlayer(mockPlayer2);

		game.play(mockPlayer1, mockMove1);

		verify(mockPlayer2).yourTurn(mockBoard2);
	}

	@Test
	public void player1_receives_notification_after_player2_has_played() {
		when(mockBoard1.play(mockPlayer1, mockMove1)).thenReturn(mockBoard2);
		when(mockBoard2.nextPlayer()).thenReturn(mockPlayer2);
		when(mockBoard2.play(mockPlayer2, mockMove2)).thenReturn(mockBoard3);
		when(mockBoard3.nextPlayer()).thenReturn(mockPlayer1);
		game.addPlayer(mockPlayer1);
		game.addPlayer(mockPlayer2);

		game.play(mockPlayer1, mockMove1);
		game.play(mockPlayer2, mockMove2);

		verify(mockPlayer1).yourTurn(mockBoard3);
	}

	@Test
	public void all_players_receive_notifications_when_someone_wins() {
		when(mockBoard1.play(mockPlayer1, mockMove1)).thenReturn(mockBoard2);
		when(mockBoard2.getWinner()).thenReturn(mockPlayer1);
		game.addPlayer(mockPlayer1);
		game.addPlayer(mockPlayer2);

		game.play(mockPlayer1, mockMove1);

		verify(mockPlayer1, never()).yourTurn(mockBoard2);
		verify(mockPlayer1).youWin(mockBoard2);
		verify(mockPlayer2).youLoose(mockBoard2);
	}
}
