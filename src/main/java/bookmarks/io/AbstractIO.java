package bookmarks.io;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractIO implements IO {
	Queue<String> wordQueue = new LinkedList<>();

	@Override
	public void printf(String text, Object... args) {
		print(String.format(text, args));
	}

	@Override
	public int readInt(String prompt) {
		String str = readString(prompt);
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException | NullPointerException e) {
			return 0;
		}
	}

	@Override
	public String readLine(String prompt) {
		if (wordQueue.isEmpty()) {
			printPrompt(prompt);
			return readLine();
		}
		String res = String.join(" ", wordQueue);
		wordQueue.clear();
		print(prompt + res);
		return res;
	}

	@Override
	public String readString(String prompt) {
		if (wordQueue.isEmpty()) {
			printPrompt(prompt);
			return readString();
		}
		String res = wordQueue.remove();
		print(prompt + res);
		return res;
	}

	void printPrompt(String prompt) {
		print(prompt);
	}
	abstract String readLine();
	abstract String readString();
}
