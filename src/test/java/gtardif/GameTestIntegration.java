package gtardif;

import static gtardif.commons.JSTester.*;
import static net.gageot.test.rules.ServiceRule.*;
import gtardif.web.GameWebServer;
import net.gageot.test.rules.ServiceRule;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.ClassRule;
import org.junit.Test;

public class GameTestIntegration extends WebTester {
	@ClassRule
	public static ServiceRule<GameWebServer> gameServer = startWithRandomPort(GameWebServer.class);

	@Test
	public void canOpenGamePage() {
		setBaseUrl("http://localhost:" + gameServer.service().getPort());
		setScriptingEnabled(false);

		beginAt("/games.html");

		assertTitleEquals("Games");
	}

	@Test
	public void canUseGamePage() {
		assertTestOK("src/test/js/testHomePage.js", gameServer.service().getPort()).isTrue();
	}

	@Test
	public void canUseCanvas() {
		assertTestOK("src/test/js/testCanvas.js").isTrue();
	}
}
