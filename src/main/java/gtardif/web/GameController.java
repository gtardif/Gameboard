package gtardif.web;

import gtardif.p4.GameRepository;
import gtardif.p4.P4Game;
import gtardif.web.utils.JsonView;

import java.util.List;

import javax.inject.Inject;

import org.zdevra.guice.mvc.annotations.Controller;
import org.zdevra.guice.mvc.annotations.Path;

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
}
