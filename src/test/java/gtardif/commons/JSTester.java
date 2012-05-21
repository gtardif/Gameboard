package gtardif.commons;

import static org.fest.assertions.Assertions.*;
import gtardif.commons.Shell.Result;

import com.google.common.base.Joiner;

public class JSTester {
	public static void jsTest(String jsTest) {
		Result result = new Shell().execute(String.format("./mocha.sh %s", jsTest));
		assertThat(result.getStatus()).overridingErrorMessage(Joiner.on("\n").join(result.getLogs())).isEqualTo(0);
	}

	public static void jsTest(String jsTest, int serverPort) {
		Result result = new Shell().execute(String.format("./mocha.sh %s %d", jsTest, serverPort));
		assertThat(result.getStatus()).overridingErrorMessage(Joiner.on("\n").join(result.getLogs())).isEqualTo(0);
	}
}
