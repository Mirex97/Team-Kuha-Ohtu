package bookmarks.io;

import java.util.ArrayList;
import java.util.List;

public class StubIO implements IO {
	private List<String> lines;
	private int i = 0;
	private List<String> prints = new ArrayList<>();

	public StubIO(List<String> values) {
		this.lines = values;
	}

	public void print(String toPrint) {
		prints.add(toPrint);
	}

	public int readInt(String prompt) {
		print(prompt);
		return Integer.parseInt(lines.get(i++));
	}

	public List<String> getPrints() {
		return prints;
	}

	public String readLine(String prompt) {
		print(prompt);

		if (i < lines.size()) {
			return lines.get(i++);
		}
		return "";
	}
}
