package gtardif.p4;

public interface GameRepoListener {
	public void gameUpdated(P4Game game);

	public void gameStarted(P4Game game);
}
