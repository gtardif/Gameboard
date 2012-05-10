package gtardif.p4;

import static com.google.common.collect.Lists.*;
import static gtardif.p4.Pawn.*;

import java.util.List;

import net.gageot.listmaker.ListMaker;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class P4Board {
	@SuppressWarnings("unchecked")
	public static final List<List<Pawn>> EMPTY_BOARD = newArrayList(//
			col(), col(), col(), col(), col(), col(), col());
	private final P4Player player1;
	private final P4Player player2;
	private final List<List<Pawn>> columns;
	private final P4Player nextPlayer;
	private final P4Player winner;

	private Predicate<List<Pawn>> winningColumn(final Pawn pawn) {
		return new Predicate<List<Pawn>>() {
			@Override
			public boolean apply(List<Pawn> column) {
				return column.containsAll(Lists.newArrayList(pawn, pawn, pawn, pawn));
			}
		};
	}

	public P4Board(P4Player player1, P4Player player2, List<List<Pawn>> columns, P4Player nextPlayer) {
		this.player1 = player1;
		this.player2 = player2;
		this.columns = columns;
		this.nextPlayer = nextPlayer;
		winner = getWinner(columns);
	}

	private P4Player getWinner(List<List<Pawn>> columns) {
		if (ListMaker.with(columns).contains(winningColumn(RED))) {
			return player1;
		} else if (ListMaker.with(columns).contains(winningColumn(YELLOW))) {
			return player2;
		}
		return null;
	}

	public P4Board play(P4Player player, P4Move move) {
		List<Pawn> playedColumn = columns.get(move.getColumn());
		Pawn pawn = player == player1 ? RED : YELLOW;
		playedColumn.add(pawn);
		return new P4Board(player1, player2, columns, nextPlayer == player1 ? player2 : player1);
	}

	public P4Player nextPlayer() {
		return nextPlayer;
	}

	public P4Player getWinner() {
		return winner;
	}

	public List<List<Pawn>> getColumns() {
		return columns;
	}

	@VisibleForTesting
	static List<Pawn> col(Pawn... pawns) {
		return newArrayList(pawns);
	}
}
