package gtardif;

import static com.google.common.collect.Lists.*;
import static gtardif.p4.Status.*;
import gtardif.p4.P4Game;
import gtardif.web.JsonView;
import gtardif.web.RequestResult;

import org.zdevra.guice.mvc.annotations.Controller;
import org.zdevra.guice.mvc.annotations.Path;
import org.zdevra.guice.mvc.annotations.RequestParameter;

@Controller
public class GameController {
	@Path("/listGames")
	public JsonView getGameList() {
		return new JsonView(newArrayList(new P4Game("first"), new P4Game("second", ready)));
	}

	@Path("/joinGame")
	public JsonView joinGame(@RequestParameter(value = "id") String gameId) {
		return new JsonView(RequestResult.OK);
	}
}
