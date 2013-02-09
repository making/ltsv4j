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
