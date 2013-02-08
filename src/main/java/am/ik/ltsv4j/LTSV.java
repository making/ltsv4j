package am.ik.ltsv4j;

/**
 * public interface to manipulate LTSV file.
 * 
 * @author making
 * 
 */
public final class LTSV {

	/**
	 * TAB.
	 */
	public static final String TAB = "\t";

	/**
	 * LF.
	 */
	public static final char LF = '\n';

	/**
	 * CR.
	 */
	public static final char CR = '\r';

	/**
	 * separator of LTSV
	 */
	public static final String SEPARATOR = ":";

	/**
	 * default charset to read/write ltsv file.
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * constructor
	 */
	private LTSV() {
	}

	/**
	 * create {@link LTSVParser}
	 * 
	 * @return parser
	 */
	public static LTSVParser parser() {
		return new LTSVParser();
	}

	/**
	 * create {@link LTSVFormatter}
	 * 
	 * @return formatter
	 */
	public static LTSVFormatter formatter() {
		return new LTSVFormatter();
	}
}
