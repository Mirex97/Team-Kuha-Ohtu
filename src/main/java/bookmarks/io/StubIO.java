package bookmarks.io;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import java.util.Queue;
import java.util.LinkedList;

public class StubIO implements IO {
	private final LinkedBlockingQueue<String> input = new LinkedBlockingQueue<>();
	private final LinkedBlockingQueue<String> output = new LinkedBlockingQueue<>();
	private final String nullStr = new String(new char[]{0});
	private final Queue<String> wordQue = new LinkedList<String>();


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

	public void print(String toPrint) {
		output.addAll(Arrays.asList(toPrint.split("\n")));
	}

	public void printf(String text, Object... args) {
		print(String.format(text, args));
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

	public String readString(String prompt) {
		if (wordQue.isEmpty()) {
                        print(prompt);
			String line = "";
			try {
				line = readInput();
			} catch (InterruptedException e) {
				return "Polling for input timed out";
			}

			if (line == null) return null;
                        for (String part : line.split(" ")) {
                                wordQue.add(part);
                        }
                        return wordQue.remove();
                } else {
                        String res = wordQue.remove();
                        print(prompt + res);
                        return res;
                }
	}

        public String readLine(String prompt) {
                if (wordQue.isEmpty()) {
                        print(prompt);
                        String line = "";
			try {
				line = readInput();
			} catch (InterruptedException e) {
				return "Polling for input timed out";
			}
                        return line;
                } else {
                        String res = "";
                        while(true) {
                                res += wordQue.remove();
                                if (wordQue.isEmpty()) break;
                                else res += " ";
                        }
                        print(prompt + res);
                        return res;
                }
        }

	public int readInt(String prompt) {
		String str = readString(prompt);
		try {
			return Integer.parseInt(str);
		} catch (NullPointerException e) {
			return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
