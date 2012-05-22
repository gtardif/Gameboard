package gtardif.web;

import static gtardif.commons.JSTester.*;

import org.junit.Test;

public class GamesJsTest {
	@Test
	public void runTestGamesJs() {
		assertTestOK("src/test/js/testGames.js").isTrue();
	}
}
