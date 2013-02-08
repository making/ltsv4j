package am.ik.ltsv4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import am.ik.ltsv4j.exception.LTSVIOException;

/**
 * @author making
 * 
 */
public class LTSVFormatter {
	/**
	 * 
	 */
	LTSVFormatter() {
	}

	/**
	 * @param line
	 * @return
	 */
	public String formatLine(Map<String, String> line) {
		if (line == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Entry<String, String> e : line.entrySet()) {
			sb.append(e.getKey()).append(LTSV.SEPARATOR).append(e.getValue());
			if (++i < line.size()) {
				sb.append(LTSV.TAB);
			}
		}
		return sb.toString();
	}

	/**
	 * @param lines
	 * @param filepath
	 */
	public void formatLines(List<Map<String, String>> lines, String filepath) {
		this.formatLines(lines, filepath, LTSV.DEFAULT_CHARSET);
	}

	public void formatLines(List<Map<String, String>> lines, String filepath,
			String charsetName) {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(
				filepath), charsetName)) {
			this.formatLines(lines, writer);
		} catch (IOException e) {
			throw new LTSVIOException(e);
		}
	}

	/**
	 * @param lines
	 * @param file
	 */
	public void formatLines(List<Map<String, String>> lines, File file) {
		try (FileWriter writer = new FileWriter(file)) {
			this.formatLines(lines, writer);
		} catch (IOException e) {
			throw new LTSVIOException(e);
		}
	}

	/**
	 * @param lines
	 * @param outputStream
	 */
	public void formatLines(List<Map<String, String>> lines,
			OutputStream outputStream) {
		try (Writer writer = new OutputStreamWriter(outputStream)) {
			this.formatLines(lines, writer);
		} catch (IOException e) {
			throw new LTSVIOException(e);
		}
	}

	/**
	 * @param lines
	 * @param writer
	 */
	public void formatLines(List<Map<String, String>> lines, Writer writer) {
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(writer))) {
			for (Map<String, String> line : lines) {
				pw.println(formatLine(line));
			}
		}
	}
}
