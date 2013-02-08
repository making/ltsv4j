# LTSV4J

Labeled Tab Separated Value(http://ltsv.org/) manipulator for Java

This library is inspired by https://metacpan.org/module/Text::LTSV.

## Usage

### simple

    Map<String, String> line = LTSV.parser().parseLine("hoge:foo\tbar:baz\n"); => {"hoge" : "foo", "bar" : "baz"}
    String line = LTSV.formatter().formatLine(line) => "hoge:foo\tbar:baz"

    Map<String, String> line = LTSV.parser().ignores("hoge").parseLine("hoge:foo\tbar:baz\n"); => {"bar" : "baz"}
    Map<String, String> line = LTSV.parser().wants("hoge").parseLine("hoge:foo\tbar:baz\n"); => {"hoge" : "foo"}

    List<Map<String, String>> lines = LTSV.parser().parseLines("test.ltsv");
    LTSV.formatter.formatLines(lines, "foo.ltsv");

strict mode

Strictly label must be `%x30-39 / %x41-5A / %x61-7A / "_" / "." / "-"`
and field must be `%x01-08 / %x0B / %x0C / %x0E-FF`.

By default, parser does not check this rule. To enable this, use strict() method.

    LTSV.parser().strict().parseLine("@@:foo"); => throw LTSVParseException 

### iterator

test.ltsv

    a:1 b:2 c:3
    a:4 b:5 c:6
    a:7 b:8 c:9

Iterator interface is available to read ltsv file

    try (LTSVIterator iterator = LTSV.parser().iterator("test.ltsv")) {
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
    }

Note that `LTSVIterator` must be closed finally. `LTSVIterator` extends `java.lang.AutoCloseable`, so you can use try-with-resources statement supported from Java7.

## License

Licensed under the Apache License, Version 2.0.