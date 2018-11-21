package bookmarks;

import java.util.*;
import java.util.stream.Collectors;

import bookmarks.dao.*;
import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import bookmarks.io.ConsoleIO;
import bookmarks.io.IO;

public class Main {
	private EntryDao entryDao;
	private IO io;

	public Main(IO io) {
		this.io = io;
		Database database = new Database("jdbc:sqlite:bookmarks.db");
		database.getConnection();
		database.createNewTables();

		TagDao tagDao = new TagDao(database);
		EntryTagDao entryTagDao = new EntryTagDao(database, tagDao);
		EntryMetadataDao entryMetadataDao = new EntryMetadataDao(database);
		entryDao = new EntryDao(database, entryTagDao, entryMetadataDao);
	}

	public void addCommand() {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("title", io.readLine("Title: "));
		metadata.put("type", io.readLine("Type: "));
		metadata.put("author", io.readLine("Author: "));
		metadata.put("description", io.readLine("Description: "));
		metadata.put("comment", io.readLine("Comment: "));
		Set<Tag> tags = Arrays.stream(
			io.readLine("Tags: ")
				.split(","))
			.map(s -> new Tag("tag", s.trim()))
			.collect(Collectors.toSet());

		try {
			entryDao.save(new Entry(tags, metadata));
			io.print("Entry saved");
		} catch (Exception e) {
			io.print("Failed to save :(");
			e.printStackTrace();
		}
	}

	public void listCommand() {
		try {
			List<Entry> entries = entryDao.findAll();
			if (entries.isEmpty()) {
				io.print("No entries :(");
			}
			for (Entry entry : entries) {
				io.print(entry.toString());
			}
		} catch (Exception e) {
			io.print("Failed to get list :(");
			e.printStackTrace();
		}
	}

	public void helpCommand() {
		io.print("add  - add a new entry");
		io.print("list - list all entries");
		io.print("quit - exits the program");
		io.print("help - print this screen");
	}

	public void run() {
		io.print("program started");
		helpCommand();
		while (true) {
			String comm = io.readLine("> ").trim();
			switch (comm) {
				case "":
					break;
				case "add":
					addCommand();
					break;
				case "list":
					listCommand();
					break;
				case "quit":
					return;
				case "help":
					helpCommand();
					break;
				default:
					io.print("unrecognized command");
			}
		}
	}

	public static void main(String[] args) {
		IO io = new ConsoleIO();
		new Main(io).run();
	}
}
