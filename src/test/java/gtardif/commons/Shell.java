package gtardif.commons;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Shell {
	@SuppressWarnings("unchecked")
	public Result execute(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command, null, new File("."));
			process.waitFor();
			List<String> lines = IOUtils.readLines(process.getErrorStream());
			List<String> infoLines = IOUtils.readLines(process.getInputStream());
			lines.add("EXEC OUTPUT : ");
			lines.addAll(infoLines);
			return new Result(process.exitValue(), lines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static class Result {
		private final int status;
		private final List<String> logs;

		Result(int status, List<String> logs) {
			this.status = status;
			this.logs = logs;
		}

		public List<String> getLogs() {
			return logs;
		}

		public int getStatus() {
			return status;
		}
	}
}
