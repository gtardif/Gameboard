package gtardif.web;

import gtardif.p4.GameRepository;
import gtardif.p4.P4Game;
import gtardif.web.utils.JsonView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.zdevra.guice.mvc.annotations.Controller;
import org.zdevra.guice.mvc.annotations.Path;

import com.google.common.collect.ImmutableMap;

@Controller
public class GameController {
	private final GameRepository games;
	private final GameWebSocketServlet webSockets;

	@Inject
	public GameController(GameRepository games, GameWebSocketServlet webSockets) {
		this.games = games;
		this.webSockets = webSockets;
	}

	@Path("/list")
	@JsonView
	public List<P4Game> getGameList() {
		return games.getGames();
	}

	@Path("/reset")
	@JsonView
	public Map<String, String> reset() {
		System.out.println("RESET");
		games.reset();
		webSockets.reset();
		return ImmutableMap.of("success", "true");
	}
}
