package gtardif;

import static net.gageot.test.rules.ServiceRule.*;
import static org.fest.assertions.Assertions.*;
import gtardif.commons.Shell;
import gtardif.commons.Shell.Result;
import gtardif.web.GameWebServer;
import net.gageot.test.rules.ServiceRule;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.base.Joiner;

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
		jsTest("src/test/js/testHomePage.js");
	}

	static void jsTest(String jsTest) {
		Result result = new Shell().execute(String.format("./mocha.sh %s %d", jsTest, gameServer.service().getPort()));
		assertThat(result.getStatus()).overridingErrorMessage(Joiner.on("\n").join(result.getLogs())).isEqualTo(0);
	}
}
