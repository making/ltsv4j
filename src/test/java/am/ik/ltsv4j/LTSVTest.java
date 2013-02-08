package am.ik.ltsv4j;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LTSVTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParseLine() {
		Map<String, String> ltsv = LTSV.parser().parseLine(
				"hoge:foo\tbar:baz\n");
		assertThat(ltsv.get("hoge"), is("foo"));
		assertThat(ltsv.get("bar"), is("baz"));
		assertThat(LTSV.formatter().formatLine(ltsv), is("hoge:foo\tbar:baz"));
	}

	@Test
	public void testParseLineWithColon() {
		Map<String, String> ltsv = LTSV
				.parser()
				.parseLine(
						"time:28/Feb/2013:12:00:00 +0900\thost:192.168.0.1\treq:GET /list HTTP/1.1\tstatus:200");
		assertThat(ltsv.get("time"), is("28/Feb/2013:12:00:00 +0900"));
		assertThat(ltsv.get("host"), is("192.168.0.1"));
		assertThat(ltsv.get("req"), is("GET /list HTTP/1.1"));
		assertThat(ltsv.get("status"), is("200"));
	}

	@Test
	public void testParseLineWithWants() {
		Map<String, String> ltsv = LTSV.parser().wants("hoge")
				.parseLine("hoge:foo\tbar:baz\n");
		assertThat(ltsv.get("hoge"), is("foo"));
		assertThat(ltsv.get("bar"), is(nullValue()));
		assertThat(LTSV.formatter().formatLine(ltsv), is("hoge:foo"));
	}

	@Test
	public void testParseLineWithIngnore() {
		Map<String, String> ltsv = LTSV.parser().ignores("hoge")
				.parseLine("hoge:foo\tbar:baz\n");
		assertThat(ltsv.get("hoge"), is(nullValue()));
		assertThat(ltsv.get("bar"), is("baz"));
		assertThat(LTSV.formatter().formatLine(ltsv), is("bar:baz"));
	}

	@Test
	public void testParseReader() {
		List<Map<String, String>> ltsv = LTSV.parser().parseLines(
				new StringReader("hoge:foo\tbar:baz\n"));
		assertThat(ltsv.size(), is(1));
		assertThat(ltsv.get(0).get("hoge"), is("foo"));
		assertThat(ltsv.get(0).get("bar"), is("baz"));
	}

	@Test
	public void testParseFile() throws Exception {
		List<Map<String, String>> ltsv = LTSV.parser().parseLines(
				new File("src/test/resources/test.ltsv"));

		assertThat(ltsv.size(), is(3));
		assertThat(ltsv.get(0).get("a"), is("1"));
		assertThat(ltsv.get(0).get("b"), is("2"));
		assertThat(ltsv.get(0).get("c"), is("3"));
		assertThat(ltsv.get(1).get("a"), is("4"));
		assertThat(ltsv.get(1).get("b"), is("5"));
		assertThat(ltsv.get(1).get("c"), is("6"));
		assertThat(ltsv.get(2).get("a"), is("7"));
		assertThat(ltsv.get(2).get("b"), is("8"));
		assertThat(ltsv.get(2).get("c"), is("9"));
	}

	@Test
	public void testParseFileUTF8() throws Exception {
		List<Map<String, String>> ltsv = LTSV.parser().parseLines(
				"src/test/resources/hoge.ltsv", "UTF-8");

		assertThat(ltsv.size(), is(3));
		assertThat(ltsv.get(0).get("hoge"), is("foo"));
		assertThat(ltsv.get(0).get("bar"), is("baz"));
		assertThat(ltsv.get(1).get("perl"), is("5.17.8"));
		assertThat(ltsv.get(1).get("ruby"), is("2.0"));
		assertThat(ltsv.get(1).get("python"), is("2.6"));
		assertThat(ltsv.get(2).get("sushi"), is("寿司"));
		assertThat(ltsv.get(2).get("tennpura"), is("天ぷら"));
		assertThat(ltsv.get(2).get("ramen"), is("ラーメン"));
		assertThat(ltsv.get(2).get("gyoza"), is("餃子"));
	}

	@Test
	public void testIteratorWithoutHasNext() throws Exception {
		try (LTSVIterator iterator = LTSV.parser().iterator(
				"src/test/resources/test.ltsv")) {

			{
				Map<String, String> line = iterator.next();
				assertThat(line.get("a"), is("1"));
				assertThat(line.get("b"), is("2"));
				assertThat(line.get("c"), is("3"));
			}
			{
				Map<String, String> line = iterator.next();
				assertThat(line.get("a"), is("4"));
				assertThat(line.get("b"), is("5"));
				assertThat(line.get("c"), is("6"));
			}
			{
				Map<String, String> line = iterator.next();
				assertThat(line.get("a"), is("7"));
				assertThat(line.get("b"), is("8"));
				assertThat(line.get("c"), is("9"));
			}
		}
	}

	@Test
	public void testIteratorWithHasNext() throws Exception {
		try (LTSVIterator iterator = LTSV.parser().iterator(
				"src/test/resources/test.ltsv")) {
			{
				assertThat(iterator.hasNext(), is(true));
				Map<String, String> line = iterator.next();
				assertThat(line.get("a"), is("1"));
				assertThat(line.get("b"), is("2"));
				assertThat(line.get("c"), is("3"));
			}
			{
				assertThat(iterator.hasNext(), is(true));
				Map<String, String> line = iterator.next();
				assertThat(line.get("a"), is("4"));
				assertThat(line.get("b"), is("5"));
				assertThat(line.get("c"), is("6"));
			}
			{
				assertThat(iterator.hasNext(), is(true));
				Map<String, String> line = iterator.next();
				assertThat(line.get("a"), is("7"));
				assertThat(line.get("b"), is("8"));
				assertThat(line.get("c"), is("9"));
			}
			assertThat(iterator.hasNext(), is(false));
			assertThat(iterator.hasNext(), is(false));
		}
	}

	@Test
	public void testFormat() {
		Map<String, String> ltsv = new LinkedHashMap<>();
		ltsv.put("hoge", "foo");
		ltsv.put("bar", "baz");
		assertThat(LTSV.formatter().formatLine(ltsv), is("hoge:foo\tbar:baz"));
	}

}
