package gtardif.web;

import static gtardif.web.RequestResult.*;
import gtardif.p4.GameRepository;
import gtardif.p4.P4Game;
import gtardif.web.utils.JsonView;

import java.util.List;

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
	@JsonView
	public List<P4Game> getGameList() {
		return games.getGames();
	}

	@Path("/join/([^/]+)")
	@JsonView
	public RequestResult joinGame(@UriParameter(1) String gameId) {
		P4Game game = games.getGame(gameId);
		return OK;
	}

	@Path("/create/([^/]+)")
	@JsonView
	public P4Game createGame(@UriParameter(1) String gameId) {
		return games.create(gameId);
	}
}
