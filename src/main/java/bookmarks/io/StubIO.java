package bookmarks.io;

import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StubIO extends AbstractIO {
	private final LinkedBlockingQueue<String> input = new LinkedBlockingQueue<>();
	private final LinkedBlockingQueue<String> output = new LinkedBlockingQueue<>();
	
	
	public void writeInput(String input) {
		this.input.add(input);
	}

	public boolean hasOutput() {
		return output.isEmpty();
	}
	
	public String readOutput() {
		try {
			String s = "";
			// We don't want to handle empty lines in the tests, so skip them here.
			while (s != null && s.isEmpty()) {
				s = output.poll(500, TimeUnit.MILLISECONDS);
			}
			if (s == null) {
				throw new RuntimeException("Polling for input timed out");
			}
			return s;
		} catch (InterruptedException e) {
			throw new RuntimeException("Reading app output interrupted");
		}
	}

	@Override
	public void print(String toPrint) {
		Collections.addAll(output, toPrint.split("\n"));
	}

	@Override
	public String readLine() {
		try {
			String val = input.poll(500, TimeUnit.MILLISECONDS);
			if (val == null) {
				throw new RuntimeException("Polling for input timed out");
			}
			return val;
		} catch (InterruptedException e) {
			throw new RuntimeException("Polling for input interrupted");
		}
	}
}
