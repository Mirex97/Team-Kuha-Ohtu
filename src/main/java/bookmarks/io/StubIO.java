package bookmarks.io;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StubIO implements IO {
	private final LinkedBlockingQueue<String> input = new LinkedBlockingQueue<>();
	private final LinkedBlockingQueue<String> output = new LinkedBlockingQueue<>();

	public void writeInput(String input) {
		this.input.add(input);
	}

	public String readOutput() {
		try {
			String s = "";
			while (s != null && s.isEmpty()) {
				s = output.poll(200, TimeUnit.MILLISECONDS);
			}
			if (s == null) {
				throw new InterruptedException();
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
		String val = input.poll(200, TimeUnit.MILLISECONDS);
		if (val == null) {
			throw new InterruptedException();
		}
		return val;
	}

	public int readInt(String prompt) {
		print(prompt);
		try {
			return Integer.parseInt(readInput());
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
