package bookmarks.io;

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
	public String readLine() {
		try {
			return scanner.nextLine().trim();
		} catch (NoSuchElementException e) {
			print("");
			scanner = new Scanner(System.in);
			return EndOfTransmission;
		}
	}
}
