package gtardif.web;

import static org.fest.assertions.Assertions.*;
import gtardif.commons.Shell;
import gtardif.commons.Shell.Result;

import org.junit.Test;

import com.google.common.base.Joiner;

public class GameJsTests {
	@Test
	public void runJsTests() {
		jsTest("src/test/js/testGames.js");
	}

	static void jsTest(String jsTest) {
		Result result = new Shell().execute(String.format("./mocha.sh %s", jsTest));
		assertThat(result.getStatus()).overridingErrorMessage(Joiner.on("\n").join(result.getLogs())).isEqualTo(0);
	}
}
