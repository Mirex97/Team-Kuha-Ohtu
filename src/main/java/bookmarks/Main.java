package bookmarks;

import bookmarks.io.ConsoleIO;
import bookmarks.io.IO;
import bookmarks.ui.App;

public class Main {
	public static void main(String[] args) {
		IO io = new ConsoleIO();
		new App(io, "bookmarks.db").run();
	}
}
