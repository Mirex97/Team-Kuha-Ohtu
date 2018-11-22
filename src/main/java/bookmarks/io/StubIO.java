package bookmarks.io;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class StubIO implements IO {
	private Queue<String> lines = new ArrayDeque<>();
	private List<String> prints = new ArrayList<>();

	public void write(String input) {
		lines.add(input);
	}

	public void print(String toPrint) {
		prints.add(toPrint);
	}

	public int readInt(String prompt) {
		print(prompt);
		if (lines.isEmpty()) {
			throw new RuntimeException("Not enough input lines provided");
		}
		return Integer.parseInt(lines.poll());
	}

	public List<String> getPrints() {
		return prints;
	}

	public String readLine(String prompt) {
		print(prompt);

		if (lines.isEmpty()) {
			throw new RuntimeException("Not enough input lines provided");
		}
		return lines.poll();
	}
}
