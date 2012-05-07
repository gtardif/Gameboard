package gtardif.p4;

import static com.google.common.base.Preconditions.*;
import static gtardif.p4.P4Board.*;
import static gtardif.p4.Status.*;

import com.google.common.annotations.VisibleForTesting;

public class P4Game {
	private P4Board board;
	private P4Player player1 = null;
	private P4Player player2 = null;
	private Status status = WAITING;
	private final String name;

	public P4Game(String name) {
		this.name = name;
	}

	public P4Game(String name, Status status) {
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void addPlayer(P4Player player) {
		checkState(status == WAITING, "Game already started");
		if (player1 == null) {
			player1 = player;
		} else if (player2 == null) {
			checkArgument(player1.getName() != player.getName(), "player has already joind the game");
			player2 = player;
			startGame();
		}
	}

	public void startGame() {
		status = STARTED;
		board = createBoard(player1, player2);
		player1.yourTurn(board);
	}

	public Status getStatus() {
		return status;
	}

	public void play(P4Player player, P4Move move) {
		board = board.play(player, move);
		if (board.getWinner() != null) {
			board.getWinner().youWin(board);
			P4Player looser = board.getWinner() == player1 ? player2 : player1;
			looser.youLoose(board);
		} else {
			board.nextPlayer().yourTurn(board);
		}
	}

	@VisibleForTesting
	P4Board createBoard(P4Player player1, P4Player player2) {
		return new P4Board(player1, player2, EMPTY_BOARD, player1);
	}
}
