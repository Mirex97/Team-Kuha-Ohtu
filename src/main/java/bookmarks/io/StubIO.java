package bookmarks.io;

import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StubIO extends AbstractIO {
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

	@Override
	public void print(String toPrint) {
		Collections.addAll(output, toPrint.split("\n"));
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

	@Override
	public String readString() {
		String line;
		try {
			line = readInput();
		} catch (InterruptedException e) {
			throw new RuntimeException("Polling for input timed out");
		}

		if (line == null) return null;
		Collections.addAll(wordQueue, line.split(" "));
		return wordQueue.remove();
	}

	@Override
	public String readLine() {
		try {
			return readInput();
		} catch (InterruptedException e) {
			throw new RuntimeException("Polling for input timed out");
		}
	}
}
