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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import am.ik.ltsv4j.exception.LTSVIOException;

/**
 * @author making
 * 
 */
public class LTSVIterator implements Iterator<Map<String, String>>,
		AutoCloseable {

	private final BufferedReader bufferedReader;
	private final LTSVParser parser;

	private final AtomicBoolean isClosed = new AtomicBoolean(false);

	private final Queue<Map<String, String>> nextQueue = new LinkedList<>();

	LTSVIterator(BufferedReader bufferedReader, LTSVParser parser) {
		this.bufferedReader = bufferedReader;
		this.parser = parser;
	}

	@Override
	public void close() throws LTSVIOException {
		if (!isClosed.getAndSet(true)) {
			synchronized (bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					throw new LTSVIOException(e);
				}
			}
		}
	}

	@Override
	public boolean hasNext() {
		if (isClosed.get()) {
			return false;
		}
		synchronized (bufferedReader) {
			if (!nextQueue.isEmpty()) {
				return true;
			}
			try {
				String line = bufferedReader.readLine();
				boolean hasNext = line != null;
				if (hasNext) {
					Map<String, String> next = parser.parseLine(line);
					nextQueue.add(next);
				}
				return hasNext;
			} catch (IOException e) {
				throw new LTSVIOException(e);
			}
		}
	}

	@Override
	public Map<String, String> next() {
		synchronized (bufferedReader) {
			try {
				Map<String, String> next;
				if (nextQueue.isEmpty()) {
					String line = bufferedReader.readLine();
					next = parser.parseLine(line);
				} else {
					next = nextQueue.poll();
				}
				return next;
			} catch (IOException e) {
				throw new LTSVIOException(e);
			}
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
