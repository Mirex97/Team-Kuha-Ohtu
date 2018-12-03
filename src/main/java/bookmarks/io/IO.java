package bookmarks.io;

import java.io.IOException;

public interface IO {
	void print(String toPrint);

	void printf(String text, Object... args);

	void writeFile(String file, String text) throws IOException;

	int readInt(String prompt);

	String readLine(String prompt);

	String readWord(String prompt);
}
