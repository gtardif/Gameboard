package gtardif.sample;

import static net.gageot.test.rules.ServiceRule.*;
import gtardif.web.GameWebServer;
import net.gageot.test.rules.ServiceRule;
import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class FirstTestIntegration extends WebTester {
	@ClassRule
	public static ServiceRule<GameWebServer> gameServer = startWithRandomPort(GameWebServer.class);

	@Before
	public void setUp() {
		setBaseUrl("http://localhost:" + gameServer.service().getPort());
	}

	@Test
	public void canLoadCollecteForPresentationComponent() {
		beginAt("/hello/hello.html");

		assertTextPresent("Hello world 2");
	}
}
