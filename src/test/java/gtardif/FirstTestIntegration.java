package gtardif;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FirstTestIntegration extends WebTester {
	private static final int PORT = 2080;
	private static GameServer gameServer;

	@BeforeClass
	public static void setUpClass() throws Exception {
		gameServer = new GameServer(PORT);
		gameServer.start();
		while (true) {
			if (gameServer.isRunning()) {
				return;
			}
		}
	}

	@Before
	public void setUp() {
		setBaseUrl("http://localhost:" + PORT);
	}

	@Test
	public void canLoadCollecteForPresentationComponent() {
		beginAt("/hello/hello.html");

		assertTextPresent("Hello world 2");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		gameServer.stop();
	}
}
