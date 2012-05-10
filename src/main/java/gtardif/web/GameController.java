package gtardif.web;

import gtardif.p4.GameRepository;
import gtardif.p4.P4Game;

import javax.inject.Inject;

import org.zdevra.guice.mvc.annotations.Controller;
import org.zdevra.guice.mvc.annotations.Path;
import org.zdevra.guice.mvc.annotations.UriParameter;

@Controller
public class GameController {
	private final GameRepository games;

	@Inject
	public GameController(GameRepository games) {
		this.games = games;
	}

	@Path("/list")
	public JsonView getGameList() {
		return new JsonView(games.getGames());
	}

	@Path("/join/([^/]+)")
	public JsonView joinGame(@UriParameter(1) String gameId) {
		P4Game game = games.getGame(gameId);
		return new JsonView(RequestResult.OK);
	}

	@Path("/create/([^/]+)")
	public JsonView createGame(@UriParameter(1) String gameId) {
		games.create(gameId);
		return new JsonView(RequestResult.OK);
	}
}
