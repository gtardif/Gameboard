package gtardif.web;

public class RequestResult {
	public static final RequestResult OK = new RequestResult(true, null);
	private final boolean success;
	private final String message;

	public static RequestResult error(String message) {
		return new RequestResult(false, message);
	}

	private RequestResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
}
