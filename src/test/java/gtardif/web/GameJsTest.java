package gtardif.web;

import static gtardif.commons.JSTester.*;

import org.junit.Test;

public class GameJsTest {
	@Test
	public void runTestGamesJs() {
		assertTestOK("src/test/js/testGames.js").isTrue();
	}
}
