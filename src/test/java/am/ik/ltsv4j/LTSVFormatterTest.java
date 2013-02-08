package am.ik.ltsv4j;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class LTSVFormatterTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFormat() {
		Map<String, String> ltsv = new LinkedHashMap<>();
		ltsv.put("hoge", "foo");
		ltsv.put("bar", "baz");
		assertThat(LTSV.formatter().formatLine(ltsv), is("hoge:foo\tbar:baz"));
	}

}
