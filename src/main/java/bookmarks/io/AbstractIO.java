package bookmarks.io;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractIO implements IO {

	// CTRL+D aka End of Transmission, char code 4. Used as a shortcut for exit.
	public static final String EndOfTransmission = new String(new char[]{4});
	Queue<String> wordQueue = new LinkedList<>();

	@Override
	public void printf(String text, Object... args) {
		print(String.format(text, args));
	}

	@Override
	public int readInt(String prompt) {
		String str = readWord(prompt);
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public String readLine(String prompt) {
		if (wordQueue.isEmpty()) {
			printPrompt(prompt);
			return readLine();
		}
		String res;
		try {
			res = String.join(" ", wordQueue);
		} catch (NullPointerException e) {
			return null;
		}
		if (res.isEmpty()) {
			return null;
		}
		wordQueue.clear();
		print(prompt + res);
		return res;
	}

	@Override
	public String readWord(String prompt) {
		if (wordQueue.isEmpty()) {
			printPrompt(prompt);
			Collections.addAll(wordQueue, readLine().split(" "));
			return wordQueue.remove();
		}
		String res = wordQueue.remove();
		print(prompt + res);
		return res;
	}

	void printPrompt(String prompt) {
		print(prompt);
	}

	abstract String readLine();
}
