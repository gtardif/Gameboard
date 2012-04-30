package gtardif;

import static com.google.common.collect.Lists.*;
import static gtardif.P4Board.*;
import static gtardif.Pawn.*;
import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;
import gtardif.commons.AutoMockRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AutoMockRunner.class)
@SuppressWarnings("unchecked")
public class P4BoardTest {
	private P4Player mockPlayer1;
	private P4Player mockPlayer2;
	private P4Move mockMove;

	@Test
	public void board_after_player1_move_contains_one_red_cell_in_played_column() {
		P4Board board = new P4Board(mockPlayer1, mockPlayer2, EMPTY_BOARD, mockPlayer1);
		when(mockMove.getColumn()).thenReturn(3);

		P4Board newBoard = board.play(mockPlayer1, mockMove);

		assertThat(newBoard.getColumns()).isEqualTo(newArrayList(col(), col(), col(), col(RED), col(), col(), col()));
	}

	@Test
	public void next_player_after_player1_is_player2() {
		P4Board board = new P4Board(mockPlayer1, mockPlayer2, EMPTY_BOARD, mockPlayer1);
		when(mockMove.getColumn()).thenReturn(3);

		P4Board newBoard = board.play(mockPlayer1, mockMove);

		assertThat(newBoard.nextPlayer()).isEqualTo(mockPlayer2);
	}

	@Test
	public void board_afyter_player_2_move_contains_Yellow_cell_added_to_column() {
		P4Board board = new P4Board(mockPlayer1, mockPlayer2, //
				newArrayList(col(), col(), col(), col(RED), col(), col(), col()), mockPlayer2);
		when(mockMove.getColumn()).thenReturn(3);

		P4Board newBoard = board.play(mockPlayer2, mockMove);

		assertThat(newBoard.getColumns()).isEqualTo(newArrayList(col(), col(), col(), col(RED, YELLOW), col(), col(), col()));
	}

	@Test
	public void player1_wins_when_aligning_4_vertical_pawns() {
		P4Board board = new P4Board(mockPlayer1, mockPlayer2, //
				newArrayList(col(), col(RED, RED, RED), col(), col(), col(), col(), col()), mockPlayer1);
		when(mockMove.getColumn()).thenReturn(1);

		P4Board newBoard = board.play(mockPlayer1, mockMove);

		assertThat(newBoard.getWinner()).isEqualTo(mockPlayer1);
	}
}
