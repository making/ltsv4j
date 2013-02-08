package am.ik.ltsv4j.exception;

@SuppressWarnings("serial")
public class LTSVException extends RuntimeException {

	public LTSVException() {
		super();
	}

	public LTSVException(String message, Throwable cause) {
		super(message, cause);
	}

	public LTSVException(String message) {
		super(message);
	}

	public LTSVException(Throwable cause) {
		super(cause);
	}

}
