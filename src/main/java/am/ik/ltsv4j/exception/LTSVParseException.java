package am.ik.ltsv4j.exception;

@SuppressWarnings("serial")
public class LTSVParseException extends LTSVException {

	public LTSVParseException() {
		super();
	}

	public LTSVParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LTSVParseException(String message) {
		super(message);
	}

	public LTSVParseException(Throwable cause) {
		super(cause);
	}

}
