package gtardif.p4;

public interface P4Player {
	String getName();

	void yourTurn(P4Board board);

	void youWin(P4Board board);

	void youLoose(P4Board board);
}
