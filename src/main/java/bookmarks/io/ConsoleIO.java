package bookmarks.io;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleIO implements IO {
	private Scanner scanner = new Scanner(System.in);

	public void print(String toPrint) {
		System.out.println(toPrint);
	}

	public void printf(String text, Object... args) {
		print(String.format(text, args));
	}

	public int readInt(String prompt) {
		System.out.print(prompt);
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NoSuchElementException | NumberFormatException e) {
			print("");
			scanner = new Scanner(System.in);
			return 0;
		}
	}

	public String readLine(String prompt) {
		System.out.print(prompt);
		try {
			return scanner.nextLine().trim();
		} catch (NoSuchElementException e) {
			print("");
			scanner = new Scanner(System.in);
			return null;
		}
	}
}
