package bookmarks.io;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

public class ConsoleIO implements IO {
	private Scanner scanner = new Scanner(System.in);
	private Queue<String> wordQue = new LinkedList<String>();

	public void print(String toPrint) {
		System.out.println(toPrint);
	}

	public void printf(String text, Object... args) {
		print(String.format(text, args));
	}

	public String readString(String prompt) {
		if (wordQue.isEmpty()) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			for (String part : line.split(" ")) {
				wordQue.add(part);
			}
			return wordQue.remove();
		} else {
			String res = wordQue.remove();
			System.out.println(prompt + res);
			return res;
		}
	}

	public String readLine(String prompt) {
		if (wordQue.isEmpty()) {
			System.out.print(prompt);
			String line = scanner.nextLine().trim();
			return line;
		} else {
			String res = "";
			while(true) {
				res += wordQue.remove();
				if (wordQue.isEmpty()) break;
				else res += " ";
			}
			System.out.println(prompt + res);
			return res;
		}
	}

	public int readInt(String prompt) {
		String str = readString(prompt);
		try {
			 return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
