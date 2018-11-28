package main;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import bookmarks.dao.*;
import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import bookmarks.io.ConsoleIO;
import bookmarks.io.IO;
import bookmarks.ui.App;

public class Main {

	public static void main(String[] args) {
		IO io = new ConsoleIO();
		new App(io, "bookmarks.db").run();
	}
}
