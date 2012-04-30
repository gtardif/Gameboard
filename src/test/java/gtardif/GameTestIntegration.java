package gtardif;

import static org.fest.assertions.Assertions.*;
import gtardif.web.GameWebServer;
import net.gageot.test.utils.Shell;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		int returnCode = new Shell().execute(String.format("./mocha.sh %s %d", jsTest, 2080));
		assertThat(returnCode).isEqualTo(0);
	}
}
