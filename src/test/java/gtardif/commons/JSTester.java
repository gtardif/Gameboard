package gtardif.commons;

import static org.fest.assertions.Assertions.*;
import gtardif.commons.Shell.Result;

import org.fest.assertions.BooleanAssert;

import com.google.common.base.Joiner;

public class JSTester {
	public static BooleanAssert assertTestOK(String jsTest) {
		Result result = new Shell().execute(String.format("./mocha.sh %s", jsTest));
		return assertThat(result.getStatus() == 0).overridingErrorMessage(Joiner.on("\n").join(result.getLogs()));
	}

	public static BooleanAssert assertTestOK(String jsTest, int serverPort) {
		Result result = new Shell().execute(String.format("./mocha.sh %s %d", jsTest, serverPort));
		return assertThat(result.getStatus() == 0).overridingErrorMessage(Joiner.on("\n").join(result.getLogs()));
	}
}
