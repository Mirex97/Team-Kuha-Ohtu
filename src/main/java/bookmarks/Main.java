package bookmarks;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import bookmarks.dao.*;
import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import bookmarks.io.ConsoleIO;
import bookmarks.io.IO;

public class Main {

	private EntryDao entryDao;
	private TagDao tagDao;
	private IO io;

	public Main(IO io) {
		this.io = io;
		Database database = new Database("jdbc:sqlite:bookmarks.db");
		database.getConnection();
		database.createNewTables();

		tagDao = new TagDao(database);
		EntryTagDao entryTagDao = new EntryTagDao(database, tagDao);
		EntryMetadataDao entryMetadataDao = new EntryMetadataDao(database);
		entryDao = new EntryDao(database, entryTagDao, entryMetadataDao);
	}

	public void addCommand() {
		Map<String, String> metadata = new HashMap<>();
		String[] fields = new String[]{"type", "title", "author", "description", "comment"};
		for (String field : fields) {
			String label = field.substring(0, 1).toUpperCase() + field.substring(1);
			String val = io.readLine(String.format("%s: ", label));
			if (val == null) {
				io.print("Adding cancelled");
				return;
			}
			metadata.put(field, val);
		}
		String tagsStr = io.readLine("Tags: ");
		if (tagsStr == null) {
			io.print("Adding cancelled");
			return;
		}
		Set<Tag> tags = Arrays.stream(
			tagsStr.split(","))
			.map(s -> new Tag("tag", s.trim()))
			.collect(Collectors.toSet());

		try {
			entryDao.save(new Entry(tags, metadata));
			io.print("Entry created");
		} catch (Exception e) {
			io.print("Failed to save :(");
			e.printStackTrace();
		}
	}

	public void editCommand() {
		Entry entry = getEntryTo("edit");
		if (entry == null) {
			return;
		}

		Map<String, String> metadata = entry.getMetadata();
		for (Map.Entry<String, String> meta : metadata.entrySet()) {
			String key = meta.getKey();
			key = key.substring(0, 1).toUpperCase() + key.substring(1);
			String newVal = io.readLine(String.format("%s [%s]: ", key, meta.getValue()));
			if (newVal == null) {
				io.print("Editing cancelled");
				return;
			}
			if (!newVal.isEmpty()) {
				metadata.put(meta.getKey(), newVal);
			}
		}
		String newTags = io.readLine(String.format("Tags [%s]: ", entry
			.getTags().stream()
			.map(Tag::getName)
			.collect(Collectors.joining(", "))));
		if (newTags == null) {
			io.print("Editing cancelled");
			return;
		}
		if (!newTags.isEmpty()) {
			entry.setTags(Arrays.stream(
				newTags.split(","))
				.map(s -> new Tag("tag", s.trim()))
				.collect(Collectors.toSet()));
		}

		try {
			entryDao.save(entry);
			io.print("Entry updated");
		} catch (SQLException e) {
			io.print("Failed to save entry :(");
			e.printStackTrace();
		}
	}

	private Entry getEntryTo(String action) {
		int id = io.readInt("ID of entry to " + action + ": ");
		if (id <= 0) {
			io.print("Invalid entry ID");
			return null;
		}
		Entry entry;
		try {
			entry = entryDao.findOne(id);
		} catch (SQLException e) {
			io.print("Failed to fetch entry :(");
			e.printStackTrace();
			return null;
		}
		if (entry == null) {
			io.print("Entry not found");
			return null;
		}
		return entry;
	}

	public void deleteCommand() {
		Entry entry = getEntryTo("delete");
		if (entry == null) {
			return;
		}
		io.print(entry.toString());
		String confirmation = io.readLine("Are you sure you want to delete the entry [y/N]? ").toLowerCase();
		if (!confirmation.startsWith("y")) {
			io.print("Deletion cancelled");
			return;
		}

		try {
			entryDao.delete(entry.getID());
			io.print("Entry deleted successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			io.print("Failed to delete entry :(");
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

	public void helpHomeCommand() {
		io.print("add  - add a new entry");
		io.print("edit - edit an existing entry");
		io.print("delete - delete an existing entry");
		io.print("list - list all entries");
		io.print("tags - takes you to tag section");
		io.print("quit - exits the program");
		io.print("help - print this screen");
	}

	public void helpTagCommand() {
		io.print("delete - delete an existing tag");
		io.print("list - list all tags");
		io.print("return - return back to home");
		io.print("help - print this screen");
	}

	public void run() {
		io.print("bookmarks v0.1.0");
		helpHomeCommand();
		while (true) {
			String comm = io.readLine("> ");
			if (comm == null) {
				return;
			}
			switch (comm.toLowerCase()) {
				case "":
					continue;
				case "add":
				case "a":
					addCommand();
					break;
				case "edit":
				case "e":
					editCommand();
					break;
				case "delete":
				case "d":
					deleteCommand();
					break;
				case "list":
				case "ls":
				case "l":
					listCommand();
					break;
				case "tags":
				case "t":
					tagsSubSection();
					break;
				case "quit":
				case "exit":
				case "q":
					return;
				case "help":
				case "h":
					helpHomeCommand();
					break;
				default:
					io.print("Unrecognized command");
			}
			io.print("");
		}
	}

	public void listTags() {
		try {
			List<Tag> tags = tagDao.findAll();
			if (tags.isEmpty()) {
				io.print("No tags");
			}
			for (Tag tag : tags) {
				io.print(tag.toString());
			}
		} catch (Exception e) {
			io.print("Failed to get list :(");
			e.printStackTrace();
		}
	}

	public void deleteTag() {
		int id = io.readInt("ID of tag to delete: ");
		Tag tag;
		try {
			tag = tagDao.findOne(id);
		} catch (SQLException e) {
			io.print("Failed to fetch tag :(");
			e.printStackTrace();
			return;
		}
		if (tag == null) {
			io.print("Tag not found");
			return;
		}

		io.print(tag.toString());
		String confirmation = io.readLine("Are you sure you want to delete the tag [y/N]? ").toLowerCase();
		if (!confirmation.startsWith("y")) {
			io.print("Deletion cancelled");
			return;
		}

		try {
			tagDao.delete(id);
			io.print("Tag deleted successfully");
		} catch (Exception e) {
			e.printStackTrace();
			io.print("Failed to delete tag :(");
		}
	}

	public void tagsSubSection() {
		while (true) {
			String comm = io.readLine("tags> ");
			if (comm == null) {
				return;
			}
			switch (comm.toLowerCase()) {
				case "":
					continue;
				case "list":
				case "ls":
				case "l":
					listTags();
					break;
				case "delete":
				case "d":
					deleteTag();
					break;
				case "return":
				case "exit":
				case "r":
					return;
				case "help":
				case "h":
					helpTagCommand();
					break;
				default:
					io.print("Unrecognized command");
			}
			io.print("");
		}
	}

	public static void main(String[] args) {
		IO io = new ConsoleIO();
		new Main(io).run();
	}
}
