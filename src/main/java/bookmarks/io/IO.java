package bookmarks.io;

public interface IO {
	void print(String toPrint);

	void printf(String text, Object... args);

	int readInt(String prompt);

	String readLine(String prompt);

	String readWord(String prompt);
}
