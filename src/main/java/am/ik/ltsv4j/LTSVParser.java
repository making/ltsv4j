package am.ik.ltsv4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import am.ik.ltsv4j.exception.LTSVIOException;
import am.ik.ltsv4j.exception.LTSVParseException;

/**
 * Parser for LTSV file.
 * 
 * @author making
 * 
 */
public class LTSVParser {

	/**
	 * separator pattern.
	 */
	private static final Pattern SEPARATOR_PATTERN = Pattern
			.compile(LTSV.SEPARATOR);

	/**
	 * interface to create map which is an implementation of LTSV line
	 */
	public static interface MapFactory {
		Map<String, String> createMap();
	}

	/**
	 * 
	 */
	private static final MapFactory DFAULT_MAP_FACTORY = new MapFactory() {
		@Override
		public Map<String, String> createMap() {
			return new LinkedHashMap<>();
		}
	};

	/**
	 * 
	 */
	private Set<String> wants;

	/**
	 * 
	 */
	private Set<String> ignores;

	/**
	 * 
	 */
	private MapFactory mapFactory = DFAULT_MAP_FACTORY;

	/**
	 * 
	 */
	private boolean isStrict = false;

	/**
	 * 
	 */
	LTSVParser() {
	}

	/**
	 * @param wants
	 * @return
	 */
	public LTSVParser wants(String... wants) {
		this.wants = new HashSet<>(Arrays.asList(wants));
		return this;
	}

	/**
	 * @param ignores
	 * @return
	 */
	public LTSVParser ignores(String... ignores) {
		this.ignores = new HashSet<>(Arrays.asList(ignores));
		return this;
	}

	/**
	 * @param mapFactory
	 * @return
	 */
	public LTSVParser mapFactory(MapFactory mapFactory) {
		this.mapFactory = mapFactory;
		return this;
	}

	/**
	 * @param isStrict
	 * @return
	 */
	public LTSVParser strict(boolean isStrict) {
		this.isStrict = isStrict;
		return this;
	}

	/**
	 * @param reader
	 * @return
	 */
	public List<Map<String, String>> parseLines(Reader reader) {
		List<Map<String, String>> result = new ArrayList<>();
		try (LTSVIterator iterator = this.iterator(reader)) {
			while (iterator.hasNext()) {
				result.add(iterator.next());
			}
		}
		return result;
	}

	/**
	 * @param in
	 * @return
	 */
	public List<Map<String, String>> parseLines(InputStream in) {
		try (Reader reader = new InputStreamReader(in)) {
			return this.parseLines(reader);
		} catch (IOException e) {
			throw new LTSVIOException(e);
		}
	}

	/**
	 * @param file
	 * @return
	 */
	public List<Map<String, String>> parseLines(File file) {
		try (FileReader reader = new FileReader(file)) {
			return this.parseLines(reader);
		} catch (IOException e) {
			throw new LTSVIOException(e);
		}
	}

	/**
	 * @param filePath
	 * @return
	 */
	public List<Map<String, String>> parseLines(String filePath) {
		return parseLines(filePath, LTSV.DEFAULT_CHARSET);
	}

	/**
	 * @param filePath
	 * @param charsetName
	 * @return
	 */
	public List<Map<String, String>> parseLines(String filePath,
			String charsetName) {
		try (Reader reader = new InputStreamReader(
				new FileInputStream(filePath), charsetName)) {
			return this.parseLines(reader);
		} catch (IOException e) {
			throw new LTSVIOException(e);
		}
	}

	/**
	 * @param line
	 * @return
	 */
	public Map<String, String> parseLine(String line) {
		if (line == null) {
			throw new IllegalArgumentException("line must not be null.");
		}

		StringTokenizer tokenizer = new StringTokenizer(chomp(line), LTSV.TAB);
		Map<String, String> result = mapFactory.createMap();
		while (tokenizer.hasMoreTokens()) {
			String labeledValue = tokenizer.nextToken();
			String[] values = SEPARATOR_PATTERN.split(labeledValue);
			if (values.length < 2) {
				throw new LTSVParseException("label and value (" + labeledValue
						+ ") are not separated by " + LTSV.SEPARATOR);
			}
			String label = values[0];
			String value = labeledValue.substring(label.length() + 1);

			if (isStrict) {
				validateLabel(label);
				validateValue(value);
			}

			if ((ignores != null && ignores.contains(label))
					|| (wants != null && !wants.contains(label))) {
				continue;
			}
			result.put(label, value);
		}
		return Collections.unmodifiableMap(result);
	}

	/**
	 * @param value
	 */
	private void validateValue(String value) {

	}

	/**
	 * @param label
	 */
	private void validateLabel(String label) {

	}

	/**
	 * @param reader
	 * @return
	 */
	public LTSVIterator iterator(final Reader reader) {
		return new LTSVIterator(new BufferedReader(reader), LTSVParser.this);
	}

	/**
	 * @param in
	 * @return
	 */
	public LTSVIterator iterator(InputStream in) {
		return iterator(new InputStreamReader(in));
	}

	/**
	 * @param filePath
	 * @return
	 */
	public LTSVIterator iterator(String filePath) {
		return iterator(filePath, LTSV.DEFAULT_CHARSET);
	}

	/**
	 * @param filePath
	 * @param charsetName
	 * @return
	 */
	public LTSVIterator iterator(String filePath, String charsetName) {
		try {
			return iterator(new InputStreamReader(
					new FileInputStream(filePath), charsetName));
		} catch (IOException e) {
			throw new LTSVIOException(e);
		}
	}

	/**
	 * @param str
	 * @return
	 */
	private static String chomp(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		if (str.length() == 1) {
			char ch = str.charAt(0);
			if (ch == LTSV.CR || ch == LTSV.LF) {
				return "";
			}
			return str;
		}

		int lastIdx = str.length() - 1;
		char last = str.charAt(lastIdx);

		if (last == LTSV.LF) {
			if (str.charAt(lastIdx - 1) == LTSV.CR) {
				lastIdx--;
			}
		} else if (last != LTSV.CR) {
			lastIdx++;
		}
		return str.substring(0, lastIdx);
	}

}
