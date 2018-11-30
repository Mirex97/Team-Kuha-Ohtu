package bookmarks.io;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleIO extends AbstractIO {
	private Scanner scanner = new Scanner(System.in);

	@Override
	public void print(String toPrint) {
		System.out.println(toPrint);
	}

	@Override
	public void printPrompt(String prompt) {
		System.out.print(prompt);
	}

	@Override
	public String readString() {
		String line;
		try {
			line = scanner.nextLine().trim();
		} catch (NoSuchElementException e) {
			print("");
			scanner = new Scanner(System.in);
			return null;
		}

		Collections.addAll(wordQueue, line.split(" "));
		return wordQueue.remove();
	}

	@Override
	public String readLine() {
		try {
			return scanner.nextLine().trim();
		} catch (NoSuchElementException e) {
			print("");
			scanner = new Scanner(System.in);
			return null;
		}
	}
}
