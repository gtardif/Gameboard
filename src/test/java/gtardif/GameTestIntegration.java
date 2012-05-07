package gtardif;

import static org.fest.assertions.Assertions.*;
import gtardif.utils.Shell;
import gtardif.utils.Shell.Result;
import gtardif.web.GameWebServer;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Joiner;

public class GameTestIntegration extends WebTester {
	private static final int PORT = 2080;
	private static GameWebServer gameServer;

	@BeforeClass
	public static void setUpClass() throws Exception {
		gameServer = new GameWebServer(PORT);
		gameServer.start();
		while (true) {
			if (gameServer.isRunning()) {
				return;
			}
			Thread.sleep(100);
		}
	}

	@Before
	public void setUp() {
		setBaseUrl("http://localhost:" + PORT);
		setScriptingEnabled(false);
	}

	@Test
	public void canOpenPage() {
		beginAt("/games.html");

		assertTitleEquals("Games");
	}

	@Test
	public void canUseGamePage() {
		jsTest("src/test/js/testHomePage.js");
	}

	@Test
	public void canListGames() {
		beginAt("/game/listGames");

		assertTextPresent("\"name\":\"first\"");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		gameServer.stop();
	}

	static void jsTest(String jsTest) {
		Result result = new Shell().execute(String.format("./mocha.sh %s %d", jsTest, 2080));
		int returnCode = result.getStatus();
		assertThat(returnCode).overridingErrorMessage(Joiner.on("\n").join(result.getLogs())).isEqualTo(0);
	}
}
