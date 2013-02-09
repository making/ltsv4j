/*
 * Copyright (C) 2013 Toshiaki Maki <makingx@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package am.ik.ltsv4j;

/**
 * public interface to manipulate LTSV file.
 * 
 * <h2>Simple Usage</h2>
 * <pre>
 * <code>
 *     Map<String, String> line = LTSV.parser().parseLine("hoge:foo\tbar:baz\n"); => {"hoge" : "foo", "bar" : "baz"}
 *     String line = LTSV.formatter().formatLine(line) => "hoge:foo\tbar:baz"
 *     // Vise versa
 *     Map<String, String> line = LTSV.parser().ignores("hoge").parseLine("hoge:foo\tbar:baz\n"); => {"bar" : "baz"}
 *     // Only want certain fields?
 *     Map<String, String> line = LTSV.parser().wants("hoge").parseLine("hoge:foo\tbar:baz\n"); => {"hoge" : "foo"}
 * 
 *     List<Map<String, String>> lines = LTSV.parser().parseLines("test.ltsv");
 *     LTSV.formatter.formatLines(lines, "foo.ltsv");
 * </code>
 * <h2>Read Stream</h2>
 * <pre>
 * <code>
 * try (LTSVIterator iterator = LTSV.parser().iterator("test.ltsv")) {
 *     while (iterator.hasNext()) { 
 *         Map<String, String> line = iterator.next();
 *     }
 * }
 * </code>
 * </pre>
 * </pre>
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
