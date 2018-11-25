package bookmarks.io;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StubIO implements IO {
	private final LinkedBlockingQueue<String> input = new LinkedBlockingQueue<>();
	private final LinkedBlockingQueue<String> output = new LinkedBlockingQueue<>();
	private final String nullStr = new String(new char[]{0});

	public void writeInput(String input) {
		if (input == null) {
			input = nullStr;
		}
		this.input.add(input);
	}

	public String readOutput() {
		try {
			String s = "";
			while (s != null && s.isEmpty()) {
				s = output.poll(500, TimeUnit.MILLISECONDS);
			}
			if (s == null) {
				throw new InterruptedException();
			}
			if (s.equals(nullStr)) {
				return null;
			}
			return s;
		} catch (InterruptedException e) {
			throw new RuntimeException("Reading app output timed out");
		}
	}

	public void print(String toPrint) {
		output.addAll(Arrays.asList(toPrint.split("\n")));
	}

	public void printf(String text, Object... args) {
		print(String.format(text, args));
	}

	private String readInput() throws InterruptedException {
		String val = input.poll(500, TimeUnit.MILLISECONDS);
		if (val == null) {
			throw new InterruptedException();
		} else if (val.equals(nullStr)) {
			return null;
		}
		return val;
	}

	public int readInt(String prompt) {
		print(prompt);
		try {
			return Integer.parseInt(Objects.requireNonNull(readInput()));
		} catch (InterruptedException e) {
			throw new RuntimeException("Reading app input timed out");
		}
	}

	public String readLine(String prompt) {
		print(prompt);
		try {
			return readInput();
		} catch (InterruptedException e) {
			throw new RuntimeException("Reading app input timed out");
		}
	}
}
